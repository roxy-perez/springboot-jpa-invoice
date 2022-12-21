package com.spring.datajpa.app.springbootdatajpa.controllers;

import java.util.Map;

import javax.validation.Valid;

import com.spring.datajpa.app.springbootdatajpa.models.entity.Customer;
import com.spring.datajpa.app.springbootdatajpa.models.entity.Product;
import com.spring.datajpa.app.springbootdatajpa.models.entity.Invoice;
import com.spring.datajpa.app.springbootdatajpa.models.entity.InvoiceItem;
import com.spring.datajpa.app.springbootdatajpa.models.service.ICustomerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/invoice")
@SessionAttributes("invoice")
public class InvoiceController {

    @Autowired
    private ICustomerService customerService;

    @Autowired
	private MessageSource messageSource;

    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable(value = "id") Long id,
            Model model, RedirectAttributes flash, Locale locale) {

        Invoice invoice = customerService.fetchByIdWithCustomerWithItemsWithProduct(id);

        if(invoice == null){
            flash.addFlashAttribute("error", messageSource.getMessage("text.invoice.flash.db.error", null, locale));
            return "redirect:/list";
        }    

        model.addAttribute("invoice", invoice);
        model.addAttribute("title", String.format(messageSource.getMessage("text.invoice.detail.title", null, locale), 
            invoice.getDescription()));

        return "invoice/invoice_detail";
    }

    @GetMapping("/form/{customerId}")
    public String create(@PathVariable(value = "customerId") Long customerId, Map<String, Object> model,
            RedirectAttributes flash, Locale locale) {

        Customer customer = customerService.findById(customerId);
        if (customer == null) {
            flash.addFlashAttribute("error", messageSource.getMessage("text.customer.flash.db.error", null, locale));
            return "redirect:/list";
        }

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);

        model.put("invoice", invoice);
        model.put("title", messageSource.getMessage("text.invoice.form.title", null, locale));

        return "invoice/form";
    }

    @GetMapping(value = "/load-products/{term}", produces = { "application/json" })
    public @ResponseBody List<Product> loadProducts(@PathVariable String term) {
        return customerService.findByName(term);

    }

    @PostMapping("/form")
    public String save(@Valid Invoice invoice, BindingResult result,
            @RequestParam(name = "item_id[]", required = false) Long[] itemId, Model model,
            @RequestParam(name = "quantity[]", required = false) Integer[] quantity, RedirectAttributes flash,
            SessionStatus status, Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("title", messageSource.getMessage("text.invoice.form.title", null, locale));
            return "invoice/form";
        }

        if (itemId == null || itemId.length == 0) {
            model.addAttribute("title", messageSource.getMessage("text.invoice.form.title", null, locale));
			model.addAttribute("error", messageSource.getMessage("text.invoice.flash.lines.error", null, locale));

            return "invoice/form";
        }

        for (int i = 0; i < itemId.length; i++) {
            Product product = customerService.findProductById(itemId[i]);

            InvoiceItem line = new InvoiceItem();
            line.setQuantity(quantity[i]);
            line.setProduct(product);
            invoice.addInvoiceItem(line);

            log.info("ID: " + itemId[i].toString() + ", Cantidad: " + quantity[i].toString());
        }

        customerService.saveInvoice(invoice);
        status.setComplete();

        flash.addFlashAttribute("success", messageSource.getMessage("text.invoice.flash.create.success", null, locale));

        return "redirect:/detail/" + invoice.getCustomer().getId();

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes flash, Locale locale){

        Invoice invoice = customerService.findInvoiceById(id);

        if(invoice != null){
            customerService.deleteInvoice(id);
            flash.addFlashAttribute("success", messageSource.getMessage("text.invoice.flash.delete.success", null, locale));

            return "redirect:/detail/" + invoice.getCustomer().getId();
        }

        flash.addFlashAttribute("error", messageSource.getMessage("text.invoice.flash.db.error", null, locale));

        return "redirect:/list";

    }

}

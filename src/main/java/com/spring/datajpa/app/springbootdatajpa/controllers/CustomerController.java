package com.spring.datajpa.app.springbootdatajpa.controllers;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.spring.datajpa.app.springbootdatajpa.models.entity.Customer;
import com.spring.datajpa.app.springbootdatajpa.models.service.ICustomerService;
import com.spring.datajpa.app.springbootdatajpa.models.service.IUploadFileService;
import com.spring.datajpa.app.springbootdatajpa.util.paginator.PageRender;
import com.spring.datajpa.app.springbootdatajpa.view.xml.CustomerList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("customer")
public class CustomerController {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private ICustomerService cusService;

    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private MessageSource messageSource;

    @Secured({ "ROLE_USER", "ROLE_ADMIN" })
    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<UrlResource> detailImage(@PathVariable String filename) {

        UrlResource resource = null;
        try {
            resource = uploadFileService.load(filename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                resource.getFilename() + "\"").body(resource);

    }

    //Método Listar Clientes REST
    //Se puede usar http://localhost:8080/list-rest?format=json o http://localhost:8080/list-rest?format=xml
    @GetMapping(value = "/list-rest")
    public @ResponseBody CustomerList customerRest() {
        return new CustomerList (cusService.findAll());
    }

    //Método Listar Clientes
    @RequestMapping(value = { "/list", "/" }, method = RequestMethod.GET)
    public String customerList(@RequestParam(name = "page", defaultValue = "0") int page,
            Model model, Authentication authentication, HttpServletRequest request, Locale locale) {

        if (authentication != null) {
            logger.info("Usuario atenticado: ".concat(authentication.getName()));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("Usuario atenticado (de forma estática): ".concat(auth.getName()));
        }

        if (hasRole("ROLE_ADMIN")) {
            logger.info("Hola, ".concat(auth.getName().concat(" tienes acceso")));
        } else {
            logger.info("Hola, ".concat(auth.getName().concat(" NO tienes acceso")));
        }

        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request,
                "ROLE_");

        if (securityContext.isUserInRole("ADMIN")) {
            logger.info("Usuario atenticado (Con SecurityContextWrapper): "
                    .concat(auth.getName().concat(" tienes acceso")));
        } else {
            logger.info("Usuario atenticado (Con SecurityContextWrapper): "
                    .concat(auth.getName().concat(" NO tienes acceso")));
        }

        if (request.isUserInRole("ROLE_ADMIN")) {
            logger.info(
                    "Usuario atenticado (Con HttpServletRequest): ".concat(auth.getName().concat(" tienes acceso")));
        } else {
            logger.info(
                    "Usuario atenticado (Con HttpServletRequest): ".concat(auth.getName().concat(" NO tienes acceso")));
        }

        Pageable pageRequest = PageRequest.of(page, 4);

        Page<Customer> cusPage = cusService.findAll(pageRequest);

        PageRender<Customer> pageRender = new PageRender<>("/list", cusPage);
        model.addAttribute("title", messageSource.getMessage("text.customer.list.title", null, locale));
        model.addAttribute("customers", cusPage);
        model.addAttribute("page", pageRender);

        return "list";

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash,
            Locale locale) {
        Customer customer = cusService.fetchByIdWithInvoices(id);

        if (customer == null) {
            flash.addFlashAttribute("error", messageSource.getMessage("text.customer.flash.db.error", null, locale));
            return "redirect:/list";
        }

        model.put("customer", customer);
        model.put("title", messageSource.getMessage("text.customer.detail.title", null, locale).concat(": ")
                .concat(customer.getName()));

        return "customer_detail";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/form")
    public String create(Map<String, Object> model, Locale locale) {
        Customer customer = new Customer();

        model.put("customer", customer);
        model.put("title", messageSource.getMessage("text.customer.form.title.create", null, locale));

        return "form";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String save(@Valid Customer customer, BindingResult result, Model model,
            @RequestParam(name = "file") MultipartFile image, RedirectAttributes flash, SessionStatus status,
            Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("title", messageSource.getMessage("text.customer.form.title", null, locale));
            return "form";
        }

        // Si el cliente tiene imagen
        if (!image.isEmpty()) {

            if (customer.getId() != null
                    && customer.getId() > 0
                    && customer.getImage() != null
                    && customer.getImage().length() > 0) {

                uploadFileService.delete(customer.getImage());

            }

            String uniqueFileName = null;
            try {
                uniqueFileName = uploadFileService.copy(image);

            } catch (Exception e) {
                e.printStackTrace();
            }

            flash.addFlashAttribute("info",
                    messageSource.getMessage("text.customer.flash.image.upload.success", null, locale) + "'"
                            + uniqueFileName + "'");

            customer.setImage(uniqueFileName);

        }

        String messageFlash = (customer.getId() != null)
                ? messageSource.getMessage("text.cliente.flash.editar.success", null, locale)
                : messageSource.getMessage("text.customer.flash.create.success", null, locale);

        cusService.save(customer);
        status.setComplete();
        flash.addFlashAttribute("success", messageFlash);

        return "redirect:/list";

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/form/{id}")
    public String edit(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash,
            Locale locale) {

        Customer customer = null;

        if (id > 0) {
            customer = cusService.findById(id);
            if (customer == null) {
                flash.addFlashAttribute("error",
                        messageSource.getMessage("text.customer.flash.db.error", null, locale));
                return "redirect:/list";
            }
        } else {
            flash.addFlashAttribute("error", messageSource.getMessage("text.customer.flash.id.error", null, locale));
            return "redirect:/list";
        }

        model.put("customer", customer);
        model.put("title", "Editar cliente");
        return "form";

    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/delete/{id}")
    public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale) {
        if (id > 0) {
            Customer customer = cusService.findById(id);

            cusService.delete(id);
            flash.addFlashAttribute("success",
                    messageSource.getMessage("text.customer.flash.delete.success", null, locale));

            if (uploadFileService.delete(customer.getImage())) { // Si se eliminó la foto
                String messageImageToDelete = String.format(
                        messageSource.getMessage("text.customer.flash.delete.success", null, locale),
                        customer.getImage());
                flash.addFlashAttribute("info", messageImageToDelete);
            }

        }

        return "redirect:/list";
    }

    public boolean hasRole(String role) {

        SecurityContext context = SecurityContextHolder.getContext();

        if (context == null) {
            return false;
        }

        Authentication auth = context.getAuthentication();

        if (auth == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        return authorities.contains(new SimpleGrantedAuthority(role));

        /*
         * for (GrantedAuthority authority : authorities) {
         * if(role.equals(authority.getAuthority())){
         * logger.info("Hola, usuario: ".concat(auth.getName().concat(" tu rol es: ").
         * concat(authority.getAuthority())));
         * return true;
         * }
         * }
         */

        // return false;

    }

}

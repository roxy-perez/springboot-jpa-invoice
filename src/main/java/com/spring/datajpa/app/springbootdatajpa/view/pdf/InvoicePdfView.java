package com.spring.datajpa.app.springbootdatajpa.view.pdf;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.spring.datajpa.app.springbootdatajpa.models.entity.Invoice;
import com.spring.datajpa.app.springbootdatajpa.models.entity.InvoiceItem;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

@Component("invoice/invoice_detail")
public class InvoicePdfView extends AbstractPdfView{

    @Autowired
    private MessageSource messageSource;
	
	@Autowired
    private LocaleResolver localeResolver;

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
                Invoice invoice = (Invoice) model.get("invoice");
                
                Locale locale = localeResolver.resolveLocale(request);
		
                MessageSourceAccessor messages =  getMessageSourceAccessor();

                PdfPCell cell = null;

                PdfPTable tableCus = new PdfPTable(1);
                tableCus.setSpacingAfter(20);
                cell = new PdfPCell(new Phrase(messageSource.getMessage("text.invoice.detail.data.customer", null, locale)));
                cell.setBackgroundColor(new Color(51, 162, 255));
                cell.setPadding(8f);
                tableCus.addCell(cell);
                tableCus.addCell(invoice.getCustomer().getName() + " " + invoice.getCustomer().getSurname());
                tableCus.addCell(invoice.getCustomer().getEmail());

                PdfPTable tableInv = new PdfPTable(1);
                tableInv.setSpacingAfter(20);
                cell = new PdfPCell(new Phrase(messageSource.getMessage("text.invoice.detail.data.invoice", null, locale)));
                cell.setBackgroundColor(new Color(51, 162, 255));
                cell.setPadding(8f);
                tableInv.addCell(cell);

                tableInv.addCell(messages.getMessage("text.customer.invoice.sheet") + ": " + invoice.getId());
                tableInv.addCell(messages.getMessage("text.customer.invoice.description") + ": " + invoice.getDescription());
                tableInv.addCell(messages.getMessage("text.customer.invoice.date") + ": " + invoice.getCreateAt());
                
                
                document.add(tableCus);
                document.add(tableInv);

                PdfPTable tableLines = new PdfPTable(4);
                tableLines.setWidths(new float[] {3.5f, 1f, 1f, 1f});
                tableLines.setSpacingAfter(20);
                tableLines.addCell(messages.getMessage("text.invoice.form.item.name"));
                tableLines.addCell(messages.getMessage("text.invoice.form.item.price"));
                tableLines.addCell(messages.getMessage("text.invoice.form.item.quantity"));
                tableLines.addCell(messages.getMessage("text.invoice.form.item.total"));
                

                for (InvoiceItem item : invoice.getItems()) {
                    tableLines.addCell(item.getProduct().getName());
                    tableLines.addCell(item.getProduct().getPrice().toString());
                    cell = new PdfPCell(new Phrase(item.getQuantity().toString()));
                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    tableLines.addCell(cell);
                    tableLines.addCell(item.getAmount().toString());
                }

                cell = new PdfPCell(new Phrase(messages.getMessage("text.invoice.form.total") + ": "));
                cell.setColspan(3);
                cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                tableLines.addCell(cell);
                tableLines.addCell(invoice.getTotalInvoice().toString());

                document.add(tableLines);
    }

}
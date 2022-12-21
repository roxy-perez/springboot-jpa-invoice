package com.spring.datajpa.app.springbootdatajpa.view.xlsx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.spring.datajpa.app.springbootdatajpa.models.entity.Invoice;
import com.spring.datajpa.app.springbootdatajpa.models.entity.InvoiceItem;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

@Component("invoice/invoice_detail.xlsx")
public class InvoiceXlsxView extends AbstractXlsxView{

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

                MessageSourceAccessor messages =  getMessageSourceAccessor();

                response.setHeader("Content-Disposition", "attachment; filename=\"factura_view.xlsx\"");
                
                Invoice invoice = (Invoice) model.get("invoice");
                Sheet sheet = workbook.createSheet("Detalles de la factura");
                
                Row row = sheet.createRow(0);
                Cell cell = row.createCell(0);
                sheet.setColumnWidth(0, 8000); 

                //Estilos cabecera secciones
                CellStyle sectionStyle = workbook.createCellStyle();
                sectionStyle.setFillForegroundColor(IndexedColors.BLUE1.index);
                sectionStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font font = workbook.createFont();
                font.setFontName("Times");
                font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
                sectionStyle.setFont(font);
                
                cell.setCellValue(messages.getMessage("text.invoice.detail.data.customer"));
                cell.setCellStyle(sectionStyle);

                row = sheet.createRow(1);
                cell = row.createCell(0);
               
                cell.setCellValue(invoice.getCustomer().getName() + " " + invoice.getCustomer().getSurname());


                row = sheet.createRow(2);
                cell = row.createCell(0);
                cell.setCellValue(invoice.getCustomer().getEmail());

                cell = sheet.createRow(4).createCell(0);
                cell.setCellValue(messages.getMessage("text.invoice.detail.data.invoice"));
                cell.setCellStyle(sectionStyle);
                

                sheet.createRow(5).createCell(0).setCellValue(messages.getMessage("text.customer.invoice.sheet") + ": " + invoice.getId());
                sheet.createRow(6).createCell(0).setCellValue(messages.getMessage("text.customer.invoice.description") + ": " + invoice.getDescription());
                sheet.createRow(7).createCell(0).setCellValue(messages.getMessage("text.customer.invoice.date") + ": " + invoice.getCreateAt());

                //Estilos
                CellStyle theaderStyle = workbook.createCellStyle();
                theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
                theaderStyle.setBorderTop(BorderStyle.MEDIUM);
                theaderStyle.setBorderRight(BorderStyle.MEDIUM);
                theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
                theaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
                theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                CellStyle tbodyStyle = workbook.createCellStyle();
                tbodyStyle.setBorderBottom(BorderStyle.THIN);
                tbodyStyle.setBorderTop(BorderStyle.THIN);
                tbodyStyle.setBorderRight(BorderStyle.THIN);
                tbodyStyle.setBorderLeft(BorderStyle.THIN);

                Row header = sheet.createRow(9);
                header.createCell(0).setCellValue(messages.getMessage("text.invoice.form.item.name"));
                header.createCell(1).setCellValue(messages.getMessage("text.invoice.form.item.price"));
                header.createCell(2).setCellValue(messages.getMessage("text.invoice.form.item.quantity"));
                header.createCell(3).setCellValue(messages.getMessage("text.invoice.form.item.total"));

                header.getCell(0).setCellStyle(theaderStyle);
                header.getCell(1).setCellStyle(theaderStyle);
                header.getCell(2).setCellStyle(theaderStyle);
                header.getCell(3).setCellStyle(theaderStyle);

                int rownum = 10;
                for (InvoiceItem item : invoice.getItems()) {
                    Row rowItem = sheet.createRow(rownum ++);
                    cell = rowItem.createCell(0);
                    cell.setCellValue(item.getProduct().getName());
                    cell.setCellStyle(tbodyStyle);

                    cell = rowItem.createCell(1);
                    cell.setCellValue(item.getProduct().getPrice());
                    cell.setCellStyle(tbodyStyle);
                    
                    cell = rowItem.createCell(2);
                    cell.setCellValue(item.getQuantity());
                    sheet.setColumnWidth(2, 4000); 
                    cell.setCellStyle(tbodyStyle);

                    cell = rowItem.createCell(3);
                    cell.setCellValue(item.getAmount());
                    cell.setCellStyle(tbodyStyle);
                }
                
                Row rowTotal = sheet.createRow(rownum);
                cell = rowTotal.createCell(2);
                cell.setCellValue(messages.getMessage("text.invoice.form.total") + ": ");
                cell.setCellStyle(sectionStyle);

                cell = rowTotal.createCell(3);
                cell.setCellValue(invoice.getTotalInvoice());
                cell.setCellStyle(tbodyStyle);

        
    }
    
}

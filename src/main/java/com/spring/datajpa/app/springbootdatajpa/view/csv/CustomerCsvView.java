package com.spring.datajpa.app.springbootdatajpa.view.csv;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.spring.datajpa.app.springbootdatajpa.models.entity.Customer;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

@Component("list.csv")
public class CustomerCsvView extends AbstractView {

    public CustomerCsvView() {
        setContentType("text/csv");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"Clientes.csv\"");
        response.setContentType(getContentType());

        Page<Customer> customers = (Page<Customer>) model.get("customers");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] header = {"id", "name", "surname", "email", "createAt"};
        csvWriter.writeHeader(header);

        for (Customer customer : customers) {
            csvWriter.write(customer, header);
        }

        csvWriter.close();

    }

    @Override
    protected boolean generatesDownloadContent() {
        return super.generatesDownloadContent();
    }

}

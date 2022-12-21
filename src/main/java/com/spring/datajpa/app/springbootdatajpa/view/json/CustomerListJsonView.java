package com.spring.datajpa.app.springbootdatajpa.view.json;

import java.util.Map;

import com.spring.datajpa.app.springbootdatajpa.models.entity.Customer;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Component("list.json")
public class CustomerListJsonView extends MappingJackson2JsonView{

    @SuppressWarnings("unchecked")
    @Override
    protected Object filterModel(Map<String, Object> model) {

        model.remove("title");
        model.remove("page");

        Page<Customer> customers = (Page<Customer>) model.get("customers");
        model.remove("customers");

        model.put("customers", customers.getContent());

        return super.filterModel(model);
    }
    
    
}

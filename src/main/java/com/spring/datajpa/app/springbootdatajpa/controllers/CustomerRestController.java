package com.spring.datajpa.app.springbootdatajpa.controllers;

import com.spring.datajpa.app.springbootdatajpa.models.service.ICustomerService;
import com.spring.datajpa.app.springbootdatajpa.view.xml.CustomerList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/customers")
public class CustomerRestController {

    @Autowired
    private ICustomerService cusService;

    //Se puede usar http://localhost:8080/api/customers/list?format=json o http://localhost:8080/api/customers/list?format=xml
    @GetMapping(value = "/list")
    public CustomerList list() {
        return new CustomerList (cusService.findAll());
    }

    
}

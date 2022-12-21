package com.spring.datajpa.app.springbootdatajpa.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.spring.datajpa.app.springbootdatajpa.models.entity.Customer;

@XmlRootElement(name="customersList")
public class CustomerList {

    @XmlElement(name = "customer")
    public List<Customer> customers;

    public CustomerList() {}

    public CustomerList(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Customer> getCustomers() {
        return customers;
    }
    
}

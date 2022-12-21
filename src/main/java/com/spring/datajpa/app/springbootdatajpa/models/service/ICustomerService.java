package com.spring.datajpa.app.springbootdatajpa.models.service;

import java.util.List;

import com.spring.datajpa.app.springbootdatajpa.models.entity.Customer;
import com.spring.datajpa.app.springbootdatajpa.models.entity.Invoice;
import com.spring.datajpa.app.springbootdatajpa.models.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICustomerService {

    public List<Customer> findAll();

    public Page<Customer> findAll(Pageable pageable);

    public void save(Customer customer);

    public Customer findById(Long id);

    public Customer fetchByIdWithInvoices(Long id);

    public void delete(Long id);

    public Invoice findInvoiceById(Long id);

    public Invoice fetchByIdWithCustomerWithItemsWithProduct(Long id);

    public void saveInvoice(Invoice invoice);

    public void deleteInvoice(Long id);

    public Product findProductById(Long id);

    public List<Product> findByName(String term);

}

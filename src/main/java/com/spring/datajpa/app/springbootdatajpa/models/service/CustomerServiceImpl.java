package com.spring.datajpa.app.springbootdatajpa.models.service;

import java.util.List;

import com.spring.datajpa.app.springbootdatajpa.models.dao.ICustomerDao;
import com.spring.datajpa.app.springbootdatajpa.models.dao.IInvoiceDao;
import com.spring.datajpa.app.springbootdatajpa.models.dao.IProductDao;
import com.spring.datajpa.app.springbootdatajpa.models.entity.Customer;
import com.spring.datajpa.app.springbootdatajpa.models.entity.Invoice;
import com.spring.datajpa.app.springbootdatajpa.models.entity.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private ICustomerDao customerDao;

    @Autowired
    private IProductDao productDao;

    @Autowired
    private IInvoiceDao invoiceDao;

    @Override
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return (List<Customer>) customerDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> findAll(Pageable pageable) {
        return customerDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return customerDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer fetchByIdWithInvoices(Long id) {
        return customerDao.fetchByIdWithInvoices(id);
    }

    @Override
    @Transactional
    public void save(Customer customer) {
        customerDao.save(customer);

    }

    @Override
    @Transactional
    public void delete(Long id) {
        customerDao.deleteById(id);
    }

    @Override
    @Transactional
    public void saveInvoice(Invoice invoice) {
        invoiceDao.save(invoice);

    }

    @Override
    @Transactional(readOnly = true)
    public Invoice findInvoiceById(Long id) {
        return invoiceDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) {
        invoiceDao.deleteById(id);
        ;
    }

    @Override
    @Transactional(readOnly = true)
    public Invoice fetchByIdWithCustomerWithItemsWithProduct(Long id) {
        return invoiceDao.fetchByIdWithCustomerWithItemsWithProduct(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findProductById(Long id) {
        return productDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByName(String term) {

        return productDao.findByNameLikeIgnoreCase("%" + term + "%");
    }

}

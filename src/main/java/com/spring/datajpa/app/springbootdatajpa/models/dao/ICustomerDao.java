package com.spring.datajpa.app.springbootdatajpa.models.dao;


import com.spring.datajpa.app.springbootdatajpa.models.entity.Customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ICustomerDao extends PagingAndSortingRepository<Customer, Long>{
    
    @Query("select c from Customer c left join fetch c.invoices f where c.id=?1")
    public Customer fetchByIdWithInvoices(Long id);

}

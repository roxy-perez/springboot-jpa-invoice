package com.spring.datajpa.app.springbootdatajpa.models.dao;

import com.spring.datajpa.app.springbootdatajpa.models.entity.Invoice;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IInvoiceDao extends CrudRepository<Invoice, Long> {

    @Query("select f from Invoice f join fetch f.customer c  join fetch f.items l join fetch l.product p where f.id=?1")
    public Invoice fetchByIdWithCustomerWithItemsWithProduct(Long id);

}

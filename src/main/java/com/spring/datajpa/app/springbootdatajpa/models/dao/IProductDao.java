package com.spring.datajpa.app.springbootdatajpa.models.dao;

import java.util.List;

import com.spring.datajpa.app.springbootdatajpa.models.entity.Product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface IProductDao extends CrudRepository<Product, Long>{

    @Query(value = "select * from products p where p.name like %?%", nativeQuery = true)
    public List<Product> findByName(String term);

    public List<Product> findByNameLikeIgnoreCase(String term);

    
}

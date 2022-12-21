package com.spring.datajpa.app.springbootdatajpa.models.dao;

import com.spring.datajpa.app.springbootdatajpa.models.entity.User;

import org.springframework.data.repository.CrudRepository;

public interface IUserDao extends CrudRepository<User, Long>{
    
    public User findByUsername(String username);
    
}

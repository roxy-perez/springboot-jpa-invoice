package com.spring.datajpa.app.springbootdatajpa.models.service;


import java.util.List;
import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;

import com.spring.datajpa.app.springbootdatajpa.models.dao.IUserDao;
import com.spring.datajpa.app.springbootdatajpa.models.entity.Role;
import com.spring.datajpa.app.springbootdatajpa.models.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;



@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserDao userDao;

    private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User usuario = userDao.findByUsername(username);

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        
        for(Role role: usuario.getAuthorities()) {
        	logger.info("Rol: ".concat(role.getAuthority()));
        	authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        
        if(authorities.isEmpty()) {
        	logger.error("Error en el Login: Usuario '" + username + "' no tiene roles asignados!");
        	throw new UsernameNotFoundException("Error en el Login: usuario '" + username + "' no tiene roles asignados!");
        }
        

        return new org.springframework.security.core.userdetails.User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
 
    }

}

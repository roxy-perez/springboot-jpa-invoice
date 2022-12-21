package com.spring.datajpa.app.springbootdatajpa.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "customers")
public class Customer implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String surname;

    @NotEmpty
    @Email
    private String email;

    @NotNull
    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createAt;

    private String image;

    @OneToMany(mappedBy="customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Invoice> invoices;


    public Customer() {
        invoices = new ArrayList<Invoice>();
    }

    public Long getId() {
        return id;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //Al crear un cliente podemos agregarle una factura
    public void addInvoice(Invoice invoice){
        this.invoices.add(invoice);
    }

    @Override
    public String toString() {
        return  name + " " + surname ;
    }


    private static final long serialVersionUID = 1L;

}

package com.salvador.customer.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "customer")
@NamedQueries(@NamedQuery(name = "findAll", query = "SELECT c from Customer as c"))
public class Customer implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID", nullable = false)
	public long customerId;

    @Basic
    @Column(name = "NAME", nullable = false, length = 45)
	public String name;

    @Basic
    @Column(name = "ADDRESS", nullable = false)
	public String address;

    @Basic
    @Column(name = "CREATED_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
	public Date createdDate;
	
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
}
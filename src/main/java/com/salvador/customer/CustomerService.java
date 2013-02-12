package com.salvador.customer;

import com.salvador.customer.model.Customer;
import com.salvador.database.CrudService;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class CustomerService {

    @Inject
    CrudService crudService;

	public void addCustomer(Customer customer){
        customer.setCreatedDate(new Date());
        crudService.create(customer);
	}

    public void delete(long customerId) {
        final Customer customer = crudService.find(Customer.class, customerId);
        crudService.delete(customer);
    }
 
	public List<Customer> findAllCustomer(){
        return crudService.findWithNamedQuery("findAll");
	}
}
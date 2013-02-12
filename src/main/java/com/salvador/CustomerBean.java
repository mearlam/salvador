package com.salvador;

import com.salvador.customer.CustomerService;
import com.salvador.customer.model.Customer;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class CustomerBean implements Serializable{
 
	@Inject
    transient CustomerService customerService;
	
	private String name;
    private String address;
	
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
 
	//get all customer data from database
	public List<Customer> getCustomerList(){
		return customerService.findAllCustomer();
	}
	
	//add a new customer data into database
	public String addCustomer(){
		
		Customer cust = new Customer();
		cust.setName(name);
		cust.setAddress(address);

        customerService.addCustomer(cust);
		
		clearForm();
		
		return "";
	}

    public String delete(long customerId) {
        FacesContext.getCurrentInstance().addMessage("",new FacesMessage(String.format("Customer %d deleted",customerId)));
        customerService.delete(customerId);
        return "";
    }
	
	//clear form values
	private void clearForm(){
		setName("");
		setAddress("");
	}
	
}

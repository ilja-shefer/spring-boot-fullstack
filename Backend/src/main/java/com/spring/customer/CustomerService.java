package com.spring.customer;

import com.spring.exceptions.DuplicateResourceException;
import com.spring.exceptions.NotFoundException;
import com.spring.exceptions.RequestValidationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerService {


    private final CustomerDao customerDao;


    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomer() {
        return  customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new NotFoundException("Customer with [%s] not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        String email = customerRegistrationRequest.email();
        if(customerDao.existsPersonWithEmail(email)){
            throw new DuplicateResourceException("Customer with email [%s] already exists".formatted(email));
        }

        //add
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        );
        customerDao.insertCustomer(customer);

    }

    public void removeCustomer(Integer id) {

        if(!customerDao.existsPersonWithId(id)) {
            throw new NotFoundException("Customer with id [%s]  not found ".formatted(id));
        }
        customerDao.deleteCustomer(id);
    }

    public void updateCustomer(Integer id, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = getCustomer(id);

        boolean changes = false;

        if(customerUpdateRequest.name() != null && !customerUpdateRequest.name().equals(customer.getName())) {
            customer.setName(customerUpdateRequest.name());
            changes = true;
        }

        if(customerUpdateRequest.age() != null && !customerUpdateRequest.age().equals(customer.getAge())) {
            customer.setAge(customerUpdateRequest.age());
            changes = true;
        }

        if(customerUpdateRequest.email() != null && !customerUpdateRequest.email().equals(customer.getEmail())) {
            if(customerDao.existsPersonWithEmail(customerUpdateRequest.email())){
                throw new DuplicateResourceException("Customer with email [%s] already exists".formatted(customerUpdateRequest.email()));
            }
            customer.setEmail(customerUpdateRequest.email());
            changes = true;
        }

        if(!changes) {
            throw new RequestValidationException("no data changes found");
        }
        customerDao.updateCustomer(customer);
    }
}

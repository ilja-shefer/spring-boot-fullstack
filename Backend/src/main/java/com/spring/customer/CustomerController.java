package com.spring.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomer();
    }

    @GetMapping("{id}")
    public Customer getCustomerWithId(@PathVariable("id") Integer id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable("id") Integer id){
        customerService.removeCustomer(id);
    }

    @PutMapping("{id}")
    public void updateCustomer(@PathVariable("id") Integer id,
                                @RequestBody CustomerUpdateRequest customer) {
        customerService.updateCustomer(id, customer);
    }
}

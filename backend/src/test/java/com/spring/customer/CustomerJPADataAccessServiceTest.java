package com.spring.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {
    private  CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock private  CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //When
        underTest.selectAllCustomers();

        //Then
        verify(customerRepository)
                .findAll();
    }

    @Test
    void selectCustomerById() {
        //Given
        int id = 1;

        //When
        underTest.selectCustomerById(id);

        //Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        //Given
        Customer actual = new Customer(
                1,
                "fake fake",
                "test@gmail.com",
                20,
                Gender.MALE);

        //When
        underTest.insertCustomer(actual);

        //Then
        verify(customerRepository).save(actual);
    }

    @Test
    void existsPersonWithEmail() {
        //Given
        String email = "test@gmail.com";

        //When
        underTest.existsPersonWithEmail(email);

        //Then
        verify(customerRepository)
                .existsCustomerByEmail(email);
    }

    @Test
    void existsPersonWithId() {
        //Given
        Integer id = 1;

        //When
        underTest.existsPersonWithId(id);

        //Then
        verify(customerRepository)
                .existsCustomerById(id);
    }

    @Test
    void deleteCustomer() {
        //Given
        Integer id = 1;

        //When
        underTest.deleteCustomer(id);

        //Then
        verify(customerRepository)
                .deleteById(id);
    }

    @Test
    void updateCustomer() {
        //Given
        Customer actual = new Customer(
                1,
                "fake fake",
                "test@gmail.com",
                20,
                Gender.MALE);

        //When
        underTest.updateCustomer(actual);

        //Then
        verify(customerRepository)
                .save(actual);
    }
}
package com.spring.customer;

import com.spring.exceptions.DuplicateResourceException;
import com.spring.exceptions.NotFoundException;
import com.spring.exceptions.RequestValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private  CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomer() {
        //When
        underTest.getAllCustomer();

        //Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@gmail.com",
                19
        );

        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        //When
        Customer actual = underTest.getCustomer(id);

        //Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        //Given
        int id = 10;

        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.empty());

        //When

        //Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(
                        "Customer with [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {
        //Given
        String email = "alex@gmail.com";

        when(customerDao.existsPersonWithEmail(email))
                .thenReturn(false);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex", email, 19
        );

        //When
        underTest.addCustomer(request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer captureCustomer = customerArgumentCaptor.getValue();

        assertThat(captureCustomer.getId()).isNull();
        assertThat(captureCustomer.getName()).isEqualTo(request.name());
        assertThat(captureCustomer.getEmail()).isEqualTo(request.email());
        assertThat(captureCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingACustomer() {
        //Given
        String email = "alex@gmail.com";

        when(customerDao.existsPersonWithEmail(email))
                .thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex", email, 19
        );

        //When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Customer with email [%s] already exists".formatted(email));

        //Then
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void removeCustomer() {
        //Given
        int id = 10;
        when(customerDao.existsPersonWithId(id))
                .thenReturn(true);

        //When
        underTest.removeCustomer(id);

        //Then
        verify(customerDao).deleteCustomer(id);
    }

    @Test
    void willThrowDeleteCustomerByIdNotFound() {
        //Given
        int id = 10;
        when(customerDao.existsPersonWithId(id))
                .thenReturn(false);

        //When
        assertThatThrownBy(() -> underTest.removeCustomer(id))
                .isInstanceOf(NotFoundException.class)
                        .hasMessage("Customer with id [%s]  not found ".formatted(id));

        //Then
        verify(customerDao, never()).deleteCustomer(id);
    }

    @Test
    void canUpdateAllCustomersProperties() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@gmail.com",
                19
        );

        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        String newEmail = "alexandro@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Alexandro",
                newEmail,
                23);

        when(customerDao.existsPersonWithEmail(newEmail))
                .thenReturn(false);

        //When
        underTest.updateCustomer(id, updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@gmail.com",
                19
        );

        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Alexandro",
                null,
                null
                );

        //When
        underTest.updateCustomer(id, updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@gmail.com",
                19
        );

        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        String newEmail = "alexandro@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null,
                newEmail,
                null);

        when(customerDao.existsPersonWithEmail(newEmail))
                .thenReturn(false);

        //When
        underTest.updateCustomer(id, updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@gmail.com",
                19
        );

        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null,
                null,
                20);

        //When
        underTest.updateCustomer(id, updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void willThrowWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@gmail.com",
                19
        );

        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        String newEmail = "alexandro@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null,
                newEmail,
                null);

        when(customerDao.existsPersonWithEmail(newEmail))
                .thenReturn(true);

        //When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Customer with email [%s] already exists".formatted(updateRequest.email()));

        //Then
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "alex@gmail.com",
                19
        );

        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getAge());

        //When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        //Then
        verify(customerDao, never()).updateCustomer(any());
    }
}
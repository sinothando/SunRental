package za.ac.cput.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.domain.Customer;
import za.ac.cput.factory.CustomerFactory;
import za.ac.cput.repository.CustomerRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    private static Customer customer;

    @Mock
    private CustomerRepository repository;

    @Test
    @Order(1)
    void setup() {
        customer = CustomerFactory.buildCustomer(1L, "Kelly", "Khoza", "kelly123@gmail.com", "password123", "0712345678");
        assertNotNull(customer);
        System.out.println(customer);
    }

    @Test
    @Order(2)
    void create() {
        Customer created = customerService.create(customer);
        assertNotNull(created);
        assertEquals("Kelly", created.getFirstName());
        assertEquals("Khoza", created.getLastName());
        System.out.println("Created: " + created);
    }

    @Test
    @Order(3)
    void update() {
        Customer newCustomer = new Customer.Builder().copy(customer).SetFirstName("Ntsako").build();
        customerService.create(customer);
        Customer updated = customerService.update(newCustomer);
        assertNotNull(updated);
        assertEquals("Ntsako", updated.getFirstName());
        System.out.println("Updated: " + updated);
    }

    @Test
    @Order(4)
    void findByEmail() {
        customerService.create(customer);
        Customer found = customerService.findByEmail("kelly123@gmail.com");
        assertNotNull(found);
        assertEquals("kelly123@gmail.com", found.getEmail());
        System.out.println("Found by email: " + found);
    }

    @Test
    @Order(5)
    void register() {
        when(repository.findByEmail("kelly123@gmail.com")).thenReturn(null);
        when(repository.save(any(Customer.class))).thenReturn(customer);
        Customer registeredCustomer = customerService.register(customer);
        assertNotNull(registeredCustomer);
        assertEquals(customer.getEmail(), registeredCustomer.getEmail());
        System.out.println("Registered: " + registeredCustomer);
    }

    @Test
    @Order(6)
    void testLoginSuccess() {
        when(repository.findByEmail("kelly123@gmail.com")).thenReturn(customer);
        Customer loggedInCustomer = customerService.login("kelly123@gmail.com", "password123");
        assertNotNull(loggedInCustomer);
        assertEquals(customer.getEmail(), loggedInCustomer.getEmail());
        System.out.println("Logged in: " + loggedInCustomer);
    }

    @Test
    @Order(7)
    void testLoginFailure() {
        when(repository.findByEmail("kelly123@gmail.com")).thenReturn(customer);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.login("kelly123@gmail.com", "wrongpassword");
        });
        assertEquals("Invalid email or password", exception.getMessage());
        System.out.println("Login failure: " + exception.getMessage());
    }
}
package com.spring;

import com.github.javafaker.Faker;
import com.spring.customer.Customer;
import com.spring.customer.CustomerRepository;
import com.spring.customer.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;


@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            Faker faker = new Faker();
            String firstName = faker.name().firstName().toLowerCase();
            String lastName = faker.name().lastName().toLowerCase();
            Random random = new Random();
            int age = random.nextInt(19, 69);
            Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;


            Customer customer = new Customer(firstName + " " + lastName,
                                            firstName + "." + lastName + "@customer.com",
                                                age, gender);

            customerRepository.save(customer);
        };
    }
}

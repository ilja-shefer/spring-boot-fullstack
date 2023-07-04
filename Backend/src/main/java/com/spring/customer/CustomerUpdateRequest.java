package com.spring.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}

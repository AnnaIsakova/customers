package me.annaisakova.customers.services;


import me.annaisakova.customers.entities.Customer;

import java.util.List;

public interface CustomerService {
    void create(Customer customer);
    void update(Customer customer);
    void delete(Customer customer);
    List<Customer> findAll();
}

package me.annaisakova.customers.services.impl;


import me.annaisakova.customers.entities.Customer;
import me.annaisakova.customers.repositories.CustomerRepository;
import me.annaisakova.customers.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public void create(Customer customer) {
        customerRepository.saveAndFlush(customer);
    }

    @Override
    public void update(Customer customer) {
        Customer cusToUpdate = customerRepository.getOne(customer.getId());
        cusToUpdate.setName(customer.getName());
        cusToUpdate.setSurname(customer.getSurname());
        cusToUpdate.setPhone(customer.getPhone());
        customerRepository.saveAndFlush(cusToUpdate);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}

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
        Customer cusToUpdate = customerRepository.findOne(customer.getId());
        cusToUpdate.setName(customer.getName());
        cusToUpdate.setSurname(customer.getSurname());
        cusToUpdate.setPhone(customer.getPhone());
        customerRepository.saveAndFlush(cusToUpdate);
    }

    @Override
    public void delete(long id) {
        Customer cusToDelete = customerRepository.findOne(id);
        customerRepository.delete(cusToDelete);
    }

    @Override
    public void deleteAll() {
        customerRepository.deleteAll();
    }

    @Override
    public Customer findById(long id) {
        return customerRepository.findOne(id);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public boolean exists(Customer customer) {
        return customerRepository.findByPhone(customer.getPhone()) != null;
    }
}

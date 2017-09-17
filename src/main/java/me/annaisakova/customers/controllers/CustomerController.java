package me.annaisakova.customers.controllers;


import me.annaisakova.customers.entities.Customer;
import me.annaisakova.customers.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Customer>> getAll(){
        List<Customer> customers = customerService.findAll();
        if (customers == null || customers.isEmpty()){
            return new ResponseEntity<List<Customer>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Customer> getById(@PathVariable("id") int id){
        Customer customer = customerService.findById(id);
        if (customer == null){
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> add(Customer customer){
        if (customerService.exists(customer)){
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        customerService.create(customer);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> edit(@PathVariable int id, Customer customer){
        Customer currCustomer = customerService.findById(id);
        if (currCustomer == null){
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        customerService.update(customer);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable int id){
        Customer currCustomer = customerService.findById(id);
        if (currCustomer == null){
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        customerService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

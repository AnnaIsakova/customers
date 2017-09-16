package me.annaisakova.customers.controllers;


import me.annaisakova.customers.entities.Customer;
import me.annaisakova.customers.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @RequestMapping(value = "/all")
    @ResponseBody
    public ResponseEntity<List<Customer>> getAll(){
        return new ResponseEntity<List<Customer>>(customerService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<Void> add(Customer customer){
        customerService.create(customer);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public ResponseEntity<Void> edit(Customer customer){
        customerService.update(customer);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(Customer customer){
        customerService.delete(customer);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

package me.annaisakova.customers.mockito.controllers;


import me.annaisakova.customers.entities.Customer;
import me.annaisakova.customers.services.CustomerService;
import me.annaisakova.customers.validators.CustomerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/customers")
public class CustomerController {

    private final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerValidator validator;

    //  ========================GET========================
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Customer>> getAll(){
        LOG.info("getting all customers");
        List<Customer> customers = customerService.findAll();
        if (customers == null || customers.isEmpty()){
            LOG.info("no customers found");
            return new ResponseEntity<List<Customer>>(HttpStatus.NO_CONTENT);
        }
        LOG.info(customers.toString());
        return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Customer> getById(@PathVariable("id") int id){
        LOG.info("getting customer by id {}", id);
        Customer customer = customerService.findById(id);
        if (customer == null){
            LOG.info("no customer with id {} found", id);
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }

    //    ========================ADD========================
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> add(@RequestBody Customer customer, BindingResult bindingResult){
        LOG.info("adding customer {}", customer);

        validator.validate(customer, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        if (customerService.exists(customer)){
            LOG.info("customer with phone {} already exists", customer.getPhone());
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        customerService.create(customer);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    //    ========================UPDATE========================
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> edit(@RequestBody Customer customer, BindingResult bindingResult){
        LOG.info("editing customer {}", customer);


        Customer currCustomer = customerService.findById(customer.getId());

        if (currCustomer == null){
            LOG.info("customer with id {} not found", customer.getId());
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        } else if (customerService.exists(customer) && !currCustomer.getPhone().equals(customer.getPhone())){
            LOG.info("customer with phone {} already exists", customer.getPhone());
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        validator.validate(customer, bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        customerService.update(customer);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    //    ========================DELETE========================
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable int id){
        LOG.info("deleting customer with id {}", id);
        Customer currCustomer = customerService.findById(id);
        if (currCustomer == null){
            LOG.info("customer with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        customerService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}

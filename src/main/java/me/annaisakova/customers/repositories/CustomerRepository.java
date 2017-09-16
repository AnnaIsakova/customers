package me.annaisakova.customers.repositories;


import me.annaisakova.customers.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long>{
    //no need to define new methods for CRUD, the already defined in JpaRepository
}

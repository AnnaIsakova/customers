package me.annaisakova.customers.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.annaisakova.customers.entities.Customer;
import me.annaisakova.customers.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    List<Customer> customers = new ArrayList<>();

    @Before
    public void init(){
        Customer c = new Customer("Bill", "Clinton", "044 000 00 00");
        c.setId(1);
        customers.add(c);
        c = new Customer("Dimebag", "Darrell", "050 111 11 11");
        c.setId(2);
        customers.add(c);

        this.mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    //===================GET ALL===================
    @Test
    public void getAllCustomersSuccess() throws Exception {
        when(customerService.findAll()).thenReturn(customers);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Bill")))
                .andExpect(jsonPath("$[0].surname", is("Clinton")))
                .andExpect(jsonPath("$[0].phone", is("044 000 00 00")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Dimebag")))
                .andExpect(jsonPath("$[1].surname", is("Darrell")))
                .andExpect(jsonPath("$[1].phone", is("050 111 11 11")));
        verify(customerService, times(1)).findAll();
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void getAllCustomersNoContent() throws Exception {
        when(customerService.findAll()).thenReturn(null);
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isNoContent());

        when(customerService.findAll()).thenReturn(new ArrayList<Customer>());
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isNoContent());

        verify(customerService, times(2)).findAll();
        verifyNoMoreInteractions(customerService);
    }

    //===================GET ONE===================
    @Test
    public void getCustomerByIdSuccess() throws Exception {
        when(customerService.findById(1)).thenReturn(customers.get(0));

        mockMvc.perform(get("/api/customers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Bill")))
                .andExpect(jsonPath("$.surname", is("Clinton")))
                .andExpect(jsonPath("$.phone", is("044 000 00 00")));
        verify(customerService, times(1)).findById(1);
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void getCustomerByIdNotFound() throws Exception {
        when(customerService.findById(1)).thenReturn(null);

        mockMvc.perform(get("/api/customers/{id}", 1))
                .andExpect(status().isNotFound());
        verify(customerService, times(1)).findById(1);
        verifyNoMoreInteractions(customerService);
    }

    //===================CREATE===================
    @Test
    public void createCustomerSuccess() throws Exception {
        Customer customer = new Customer("Dave", "Mustaine", "123 321");
        when(customerService.exists(customer)).thenReturn(false);
        mockMvc.perform(post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(customer)))
                .andExpect(status().isCreated());
        verify(customerService, times(1)).exists(customer);
        verify(customerService, times(1)).create(customer);
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void createCustomerConflict() throws Exception {
        Customer customer = new Customer("Dee", "Snider", "1408");
        when(customerService.exists(customer)).thenReturn(true);
        mockMvc.perform(post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(customer)))
                .andExpect(status().isConflict());
        verify(customerService, times(1)).exists(customer);
        verifyNoMoreInteractions(customerService);
    }

    //===================UPDATE===================
    @Test
    public void updateCustomerSuccess() throws Exception {
        Customer customer = new Customer("John", "Petrucci", "999");
        customer.setId(1);
        when(customerService.findById(customer.getId())).thenReturn(customer);
        when(customerService.exists(customer)).thenReturn(false);
        mockMvc.perform(put("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(customer)))
                .andExpect(status().isOk());
        verify(customerService, times(1)).findById(customer.getId());
        verify(customerService, times(1)).exists(customer);
        verify(customerService, times(1)).update(customer);
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void updateCustomerNotFound() throws Exception {
        Customer customer = new Customer("James", "Hetfield", "999");
        customer.setId(1);
        when(customerService.findById(customer.getId())).thenReturn(null);
        mockMvc.perform(put("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customer)))
                .andExpect(status().isNotFound());
        verify(customerService, times(1)).findById(customer.getId());
        verifyNoMoreInteractions(customerService);
    }

    public void updateCustomerConflict() throws Exception {
        Customer customer = new Customer("Ronnie James", "Dio", "999");
        customer.setId(1);
        when(customerService.findById(customer.getId())).thenReturn(customer);
        when(customerService.exists(customer)).thenReturn(true);
        mockMvc.perform(put("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customer)))
                .andExpect(status().isConflict());
        verify(customerService, times(1)).findById(customer.getId());
        verify(customerService, times(1)).exists(customer);
        verifyNoMoreInteractions(customerService);
    }

    //===================DELETE===================
    @Test
    public void deleteCustomerSuccess() throws Exception {
        Customer customer = new Customer("Chuck", "Billy", "999");
        customer.setId(1);
        when(customerService.findById(customer.getId())).thenReturn(customer);
        mockMvc.perform(delete("/api/customers/{id}", customer.getId()))
                .andExpect(status().isOk());
        verify(customerService, times(1)).findById(customer.getId());
        verify(customerService, times(1)).delete(customer.getId());
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void deleteCustomerNotFound() throws Exception {
        when(customerService.findById(1)).thenReturn(null);
        mockMvc.perform(delete("/api/customers/{id}", 1))
                .andExpect(status().isNotFound());
        verify(customerService, times(1)).findById(1);
        verifyNoMoreInteractions(customerService);
    }


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

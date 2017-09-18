package me.annaisakova.customers.cucumber.steps;


import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import me.annaisakova.customers.CustomersApplication;
import me.annaisakova.customers.entities.Customer;
import me.annaisakova.customers.services.CustomerService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CustomersApplication.class, loader = SpringBootContextLoader.class)
@WebAppConfiguration
public class CrudStepsDefinition {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ResultActions resultActions;

    @Autowired
    CustomerService customerService;

    @Before
    public void init(){
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    @Given("^the db is empty$")
    public void clearDb() throws Throwable {
        customerService.deleteAll();
    }

    @Given("^the following customers exist:$")
    public void createBooks(DataTable customers) throws Throwable {
        List<Customer> customerList = customers.asList(Customer.class);
        System.out.println("\nCUSTOMERS" + customerList);
        for (Customer c:customerList) {
            System.out.println("GGGGG: " + c);
            customerService.create(c);
        }
    }

    @When("^client request POST (.*) with json data:$")
    public void performPostOnResourceUriWithJsonData(String resourceUri, String jsonData) throws Exception {
        this.resultActions = this.mockMvc.perform(post(resourceUri).contentType(MediaType.APPLICATION_JSON) //
                .content(jsonData.getBytes()));
    }

    @When("^client request PUT (.*) with json data:$")
    public void performPutOnResourceUriWithJsonData(String resourceUri, String jsonData) throws Exception {
        this.resultActions = this.mockMvc.perform(put(resourceUri).contentType(MediaType.APPLICATION_JSON) //
                .content(jsonData.getBytes()));
    }

    @When("^client request GET (.*)$")
    public void performGetOnResourceUri(String resourceUri) throws Exception {
        this.resultActions = this.mockMvc.perform(get(resourceUri));
    }

    @When("^client request DELETE (.*)$")
    public void performDeleteOnResourceUri(String resourceUri) throws Exception {
        this.resultActions = this.mockMvc.perform(delete(resourceUri));
    }

    @Then("^the response code should be (\\d*)$")
    public void checkResponse(int statusCode) throws Exception {
        resultActions.andExpect(status().is(statusCode));
    }

    @Then("^the result json should be:$")
    public void checkResponseJsonMatch(String jsonString) throws Exception {
        resultActions.andExpect(content().json(jsonString));
    }
}

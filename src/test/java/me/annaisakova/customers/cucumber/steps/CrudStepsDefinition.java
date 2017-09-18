package me.annaisakova.customers.cucumber.steps;


import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import me.annaisakova.customers.CustomersApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private HttpHeaders httpHeaders = new HttpHeaders();

    @Before
    public void init(){
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    @When("^client request POST (.*) with json data:$")
    public void performPostOnResourceUriWithJsonData(String resourceUri, String jsonData) throws Exception {
        this.resultActions = this.mockMvc.perform(post(resourceUri).contentType(MediaType.APPLICATION_JSON) //
                .content(jsonData.getBytes()));
    }

    @Then("^the response code should be (\\d*)$")
    public void checkResponse(int statusCode) throws Exception {
        resultActions.andExpect(status().is(statusCode));
    }
}

package me.annaisakova.customers.mockito;

import me.annaisakova.customers.mockito.controllers.CustomerControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		CustomerControllerTest.class
})
public class CustomersApplicationTests {

	@Test
	public void contextLoads() {
	}

}

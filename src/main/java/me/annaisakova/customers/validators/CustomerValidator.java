package me.annaisakova.customers.validators;


import me.annaisakova.customers.entities.Customer;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CustomerValidator implements Validator{

    private static final String VALID_PHONE_REGEX_1 = "\\d{10}"; //1234567890

    @Override
    public boolean supports(Class<?> aClass) {
        return Customer.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Customer customer = (Customer) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty");
        if (!isValidPhone(customer.getPhone())){
            errors.rejectValue("phone", "Invalid.phone");
        }
    }

    private boolean isValidPhone(String phone) {
        if (phone.matches("\\d{10}")) return true;
        return false;
    }
}

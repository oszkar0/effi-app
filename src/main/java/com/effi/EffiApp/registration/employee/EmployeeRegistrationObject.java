package com.effi.EffiApp.registration.employee;

import com.effi.EffiApp.entity.Company;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmployeeRegistrationObject {
    @NotBlank(message = "User first name must not be empty")
    @Size(min = 1, max = 50, message = "User first name size must be between 1 and 50")
    private String firstName;
    @NotBlank(message = "User last name must not be empty")
    @Size(min = 1, max = 50, message = "User last name size must be between 1 and 50")
    private String lastName;
    @Email(message = "Invalid email")
    @NotBlank(message = "Email must not be empty")
    @Size(min = 1, max = 50, message = "Email size must be between 1 and 50")
    private String email;
    private String role;
    private String password;
    private Company company;

    public EmployeeRegistrationObject() {
    }

    public EmployeeRegistrationObject(String firstName, String lastName, String email, String password, Company company, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.company = company;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

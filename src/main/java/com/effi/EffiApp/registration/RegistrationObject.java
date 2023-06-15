package com.effi.EffiApp.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationObject {
    @NotBlank(message = "Company name must not be empty")
    @Size(min = 1, max = 50, message = "Company name size must be between 1 and 50")
    private String companyName;
    @NotBlank(message = "User first name must not be empty")
    @Size(min = 1, max = 50, message = "User first name size must be between 1 and 50")
    private String userFirstName;
    @NotBlank(message = "User last name must not be empty")
    @Size(min = 1, max = 50, message = "User last name size must be between 1 and 50")
    private String userLastName;
    @Email(message = "Invalid email")
    @NotBlank(message = "Email must not be empty")
    @Size(min = 1, max = 50, message = "Email size must be between 1 and 50")
    private String userEmail;
    @NotBlank(message = "Password must not be empty")
    @Size(min = 10, max = 50, message = "Password size must be between 10 and 50")
    private String userPassword;

    public RegistrationObject() {
    }

    public RegistrationObject(String companyName, String userFirstName, String userLastName, String userEmail, String userPassword) {
        this.companyName = companyName;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


}

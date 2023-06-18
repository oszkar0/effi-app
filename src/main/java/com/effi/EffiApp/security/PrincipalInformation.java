package com.effi.EffiApp.security;

import com.effi.EffiApp.entity.Company;
import jakarta.persistence.Column;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class PrincipalInformation extends User {
    private String firstName;
    private String lastName;
    private Company company;

    public PrincipalInformation(String username, String password, Collection<? extends GrantedAuthority> authorities,
                                String firstName, String lastName, Company company) {
        super(username, password, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}

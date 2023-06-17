package com.effi.EffiApp.security;

import com.effi.EffiApp.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    //bcrypt bean
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //authenticantion provider bean
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserService userService){
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setPasswordEncoder(bCryptPasswordEncoder());
        daoAuthProvider.setUserDetailsService(userService);
        return daoAuthProvider;
    }

    //configure security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/").hasRole("EMPLOYEE") //config endpoints permisions
                        .requestMatchers("/register/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .loginProcessingUrl("/process-login")
                        .defaultSuccessUrl("/main-page")
                        .permitAll()) //set login page etc.
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .permitAll()) //permit all for logging out
                .exceptionHandling(configurer -> configurer
                        .accessDeniedPage("/access-denied")); //set access denied page

        return httpSecurity.build();
    }

}

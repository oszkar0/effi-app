package com.effi.EffiApp.security;

import com.effi.EffiApp.endpoints.Endpoints;
import com.effi.EffiApp.service.UserService;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private static final String ROLE_MANAGER = "MANAGER";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_EMPLOYEE = "EMPLOYEE";
    
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
                .requestMatchers(Endpoints.EMPLOYEE_EMPLOYEES_LIST).hasAnyRole(ROLE_MANAGER)
                .requestMatchers(Endpoints.EMPLOYEE_USER_DELETE).hasRole(ROLE_ADMIN)
                .requestMatchers(Endpoints.EMPLOYEE_NEW_EMPLOYEE_FORM).hasRole(ROLE_ADMIN)
                .requestMatchers(Endpoints.EMPLOYEE_NEW_EMPLOYEE_PDF).hasRole(ROLE_ADMIN)
                .requestMatchers(Endpoints.EMPLOYEE_NEW_EMPLOYEE_PROCESSING).hasRole(ROLE_ADMIN)
                .requestMatchers(Endpoints.EMPLOYEE_NEW_EMPLOYEE_INFO).hasRole(ROLE_ADMIN)
                .requestMatchers(Endpoints.REGISTRATION_PREFIX + "/**").permitAll()
                .requestMatchers(Endpoints.EXCEPTION_PREFIX + "/**").permitAll()
                .requestMatchers(Endpoints.TASKS_AND_PROFILES_MAIN_PAGE).hasRole(ROLE_EMPLOYEE)
                .requestMatchers(Endpoints.TASKS_AND_PROFILES_TASK_DETAILS).hasRole(ROLE_EMPLOYEE)
                .requestMatchers(Endpoints.TASKS_AND_PROFILES_USER_PROFILE).hasRole(ROLE_EMPLOYEE)
                .requestMatchers(Endpoints.TASKS_AND_PROFILES_TASK_UPDATE).hasRole(ROLE_EMPLOYEE)
                .requestMatchers(Endpoints.TASKS_AND_PROFILES_TASK_DELETE).hasRole(ROLE_MANAGER)
                .requestMatchers(Endpoints.TASKS_AND_PROFILES_MY_PROFILE).hasRole(ROLE_EMPLOYEE)
                .requestMatchers(Endpoints.TASKS_AND_PROFILES_NEW_TASK_FORM).hasRole(ROLE_MANAGER)
                .requestMatchers(Endpoints.TASKS_AND_PROFILES_NEW_TASK_PROCESSING).hasRole(ROLE_MANAGER)
                .requestMatchers("/logo/**").permitAll()
                .requestMatchers("/css/**").permitAll()
                .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage(Endpoints.LOGIN_LOGGING)
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .loginProcessingUrl(Endpoints.LOGIN_LOGGING_PROCESSING)
                        .defaultSuccessUrl(Endpoints.TASKS_AND_PROFILES_MAIN_PAGE)
                        .permitAll()) //set login page etc.
                .logout(logout -> logout
                        .logoutUrl(Endpoints.LOGIN_LOGOUT)
                        .permitAll())//permit all for logging out
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage(Endpoints.EXCEPTION_ACCESS_DENIED));
        return httpSecurity.build();
    }

}

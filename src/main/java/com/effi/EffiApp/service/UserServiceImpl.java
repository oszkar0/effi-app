package com.effi.EffiApp.service;

import com.effi.EffiApp.dao.RoleDao;
import com.effi.EffiApp.dao.UserDao;
import com.effi.EffiApp.entity.Company;
import com.effi.EffiApp.entity.Role;
import com.effi.EffiApp.entity.User;
import com.effi.EffiApp.registration.RegistrationObject;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private RoleDao roleDao;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User findUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    //we are passing email to load User by username since we use email as an username
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findUserByEmail(email);

        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }
    //function to map roles to SimpleGrantedAuthority class (maps Role class to that class using stream)
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean userExists(String email) {
        return userDao.userExists(email);
    }

    @Override
    @Transactional
    public void save(RegistrationObject registrationObject) {
        User user = new User();

        //add user fields
        user.setFirstName(registrationObject.getUserFirstName());
        user.setLastName(registrationObject.getUserLastName());
        user.setEmail(registrationObject.getUserEmail());
        user.setPassword(bCryptPasswordEncoder.encode(registrationObject.getUserPassword()));


        //give user all roles since only company owner/admin registers new company, if he wants
        //to add employees he adds them when he is logged
        user.setRoles(Arrays.asList(roleDao.findRoleByName("ROLE_ADMIN"), roleDao.findRoleByName("ROLE_MANAGER"),
                roleDao.findRoleByName("ROLE_EMPLOYEE")));

        //set user company, we create new company, since it is the first user for this company (owner/admin)
        user.setCompany(new Company(registrationObject.getCompanyName()));

        userDao.save(user);
    }
}

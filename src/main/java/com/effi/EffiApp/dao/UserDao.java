package com.effi.EffiApp.dao;

import com.effi.EffiApp.entity.User;

import java.util.List;

public interface UserDao {
    User findUserByEmail(String email);
    boolean userExists(String email);
    void save(User user);
    User findUserAndHisTasksById(int id);
    void deleteUserById(int id);
    User findUserById(int id);
    List<User> findUserByCompanyId(int companyId);
}

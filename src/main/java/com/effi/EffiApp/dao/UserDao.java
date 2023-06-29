package com.effi.EffiApp.dao;

import com.effi.EffiApp.entity.User;

public interface UserDao {
    User findUserByEmail(String email);
    boolean userExists(String email);
    void save(User user);
    User findUserAndHisTasksByEmail(String email);
}

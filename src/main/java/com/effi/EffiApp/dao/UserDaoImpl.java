package com.effi.EffiApp.dao;

import com.effi.EffiApp.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao{
    private EntityManager entityManager;

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User findUserByEmail(String email) {
        //query to get user by email
        TypedQuery<User> userQuery = entityManager.createQuery("from User where email=:userEmail",
                User.class);
        userQuery.setParameter("userEmail", email);

        //execute query
        User user = null;
        try {
            user = userQuery.getSingleResult();
        } catch (Exception e) {
            user = null;
        }

        return user;
    }

    @Override
    public boolean userExists(String email) {
        User user = findUserByEmail(email);

        if(user == null){
            return false;
        }

        return true;
    }

    @Override
    public void save(User user) {
        entityManager.merge(user);
    }

    @Override
    public User findUserAndHisTasksById(int id) {
        TypedQuery<User> query = entityManager.createQuery(
                "select u from User u " +
                    "LEFT JOIN FETCH u.tasks " +
                    "where u.id =: id", User.class);
        query.setParameter("id", id);

        User user = query.getSingleResult();

        return user;
    }

    @Override
    public void deleteUserById(int id) {
        User userToDelete = entityManager.find(User.class, id);

        entityManager.remove(userToDelete);
    }
}

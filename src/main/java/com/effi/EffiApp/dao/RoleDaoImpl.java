package com.effi.EffiApp.dao;

import com.effi.EffiApp.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl implements RoleDao{
    private EntityManager entityManager;

    @Autowired
    public RoleDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Role findRoleByName(String name) {
        //create query to read role/name from database
        TypedQuery<Role> roleQuery = entityManager.createQuery("from Role where name=:roleName",
                Role.class);
        roleQuery.setParameter("roleName", name);

        //execute query
        Role role = null;
        try {
            role = roleQuery.getSingleResult();
        } catch (Exception e) {
            role = null;
        }

        return role;
    }
}

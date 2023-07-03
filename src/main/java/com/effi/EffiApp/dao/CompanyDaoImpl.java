package com.effi.EffiApp.dao;

import com.effi.EffiApp.entity.Company;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDaoImpl implements CompanyDao{
    private EntityManager entityManager;

    @Autowired
    public CompanyDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Company findCompanyById(int id) {
        return entityManager.find(Company.class, id);
    }
}

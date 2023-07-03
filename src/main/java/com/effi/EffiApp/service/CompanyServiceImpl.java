package com.effi.EffiApp.service;

import com.effi.EffiApp.dao.CompanyDao;
import com.effi.EffiApp.entity.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService{
    CompanyDao companyDao;

    @Autowired
    public CompanyServiceImpl(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public Company findCompanyById(int id) {
        return companyDao.findCompanyById(id);
    }
}

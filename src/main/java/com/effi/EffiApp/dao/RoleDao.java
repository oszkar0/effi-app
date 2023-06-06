package com.effi.EffiApp.dao;

import com.effi.EffiApp.entity.Role;

public interface RoleDao {
    Role findRoleByName(String name);
}

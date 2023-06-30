package com.effi.EffiApp.dao;

import com.effi.EffiApp.entity.Task;

public interface TaskDao {
    Task findTaskById(int id);
    void save(Task task);
}

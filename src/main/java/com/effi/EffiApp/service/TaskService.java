package com.effi.EffiApp.service;

import com.effi.EffiApp.entity.Task;

public interface TaskService {
    Task findTaskById(int id);
    void save(Task task);
}

package com.effi.EffiApp.dao;

import com.effi.EffiApp.entity.Task;

import java.util.List;

public interface TaskDao {
    Task findTaskById(int id);
    void save(Task task);
    List<Task> findTaskByUserId(int userId);
    void deleteTaskById(int id);
}

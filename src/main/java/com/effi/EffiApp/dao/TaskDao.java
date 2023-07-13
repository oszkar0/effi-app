package com.effi.EffiApp.dao;

import com.effi.EffiApp.entity.Task;

import java.util.Date;
import java.util.List;

public interface TaskDao {
    Task findTaskById(int id);
    void save(Task task);
    List<Task> findTaskByUserId(int userId);
    List<Task> findTaskByUserIdAndDeadline(int userId, Date deadline);
    void deleteTaskById(int id);
}

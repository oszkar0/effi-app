package com.effi.EffiApp.service;

import com.effi.EffiApp.entity.Task;

import java.util.Date;
import java.util.List;

public interface TaskService {
    Task findTaskById(int id);
    void save(Task task);
    List<Task> findTaskByUserId(int userId);
    void deleteTaskById(int id);
    List<Task> findTaskByUserIdAndDeadline(int userId, Date deadline);
}

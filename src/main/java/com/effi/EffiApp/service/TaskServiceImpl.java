package com.effi.EffiApp.service;

import com.effi.EffiApp.dao.TaskDao;
import com.effi.EffiApp.entity.Task;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    TaskDao taskDao;

    @Autowired
    public TaskServiceImpl(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public Task findTaskById(int id) {
        return taskDao.findTaskById(id);
    }

    @Override
    @Transactional
    public void save(Task task) {
        taskDao.save(task);
    }

    @Override
    public List<Task> findTaskByUserId(int userId) {
        return taskDao.findTaskByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteTaskById(int id) {
        taskDao.deleteTaskById(id);
    }
}

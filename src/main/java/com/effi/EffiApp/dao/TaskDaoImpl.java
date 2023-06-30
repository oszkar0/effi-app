package com.effi.EffiApp.dao;

import com.effi.EffiApp.entity.Task;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TaskDaoImpl implements TaskDao{
    EntityManager entityManager;

    @Autowired
    public TaskDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Task findTaskById(int id) {
        return entityManager.find(Task.class, id);
    }

    @Override
    public void save(Task task) {
        entityManager.merge(task);
    }
}

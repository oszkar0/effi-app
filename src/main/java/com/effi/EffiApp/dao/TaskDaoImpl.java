package com.effi.EffiApp.dao;

import com.effi.EffiApp.entity.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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

    @Override
    public List<Task> findTaskByUserId(int userId) {
        TypedQuery<Task> query = entityManager.createQuery("SELECT t FROM Task t " +
                "WHERE t.user.id =: userId", Task.class);

        query.setParameter("userId", userId);

        List<Task> tasks = query.getResultList();

        return tasks;
     }

    @Override
    public void deleteTaskById(int id) {
        Task taskToDelete = findTaskById(id);

        entityManager.remove(taskToDelete);
    }

    @Override
    public List<Task> findTaskByUserIdAndDeadline(int userId, java.util.Date deadline) {
        TypedQuery<Task> query = entityManager.createQuery("SELECT t FROM Task t " +
                "WHERE t.user.id =: userId AND t.deadline =: deadline", Task.class);

        query.setParameter("userId", userId);
        query.setParameter("deadline",new java.sql.Date(deadline.getTime()));

        List<Task> tasks = query.getResultList();

        return tasks;
    }

    @Override
    public List<Task> findTaskByCompanyId(int companyId) {
        TypedQuery<Task> query = entityManager.createQuery("SELECT t FROM Task t " +
                "WHERE t.user.company.id =: companyId", Task.class);

        query.setParameter("companyId", companyId);

        List<Task> tasks = query.getResultList();

        return tasks;
    }
}

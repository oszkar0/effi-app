package com.effi.EffiApp.registration.task;

import com.effi.EffiApp.entity.Task;
import com.effi.EffiApp.validators.deadline.DateNotBeforeToday;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.sql.Date;

public class TaskRegistrationObject {
    private int userId;
    @NotNull
    @NotBlank(message = "User first name must not be empty")
    @Size(min = 1, max = 150, message = "User first name size must be between 1 and 50")
    private String title;
    @NotNull
    @NotBlank(message = "User first name must not be empty")
    @Size(min = 1, max = 500, message = "User first name size must be between 1 and 50")
    private String description;
    private Task.TaskStatus status;
    @NotNull
    @DateNotBeforeToday
    private Date deadline;

    public TaskRegistrationObject() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task.TaskStatus getStatus() {
        return status;
    }

    public void setStatus(Task.TaskStatus status) {
        this.status = status;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}

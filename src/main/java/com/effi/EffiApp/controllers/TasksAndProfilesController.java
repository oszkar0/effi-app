package com.effi.EffiApp.controllers;

import com.effi.EffiApp.endpoints.Endpoints;
import com.effi.EffiApp.entity.Task;
import com.effi.EffiApp.entity.User;
import com.effi.EffiApp.registration.task.TaskRegistrationObject;
import com.effi.EffiApp.security.PrincipalInformation;
import com.effi.EffiApp.service.CompanyService;
import com.effi.EffiApp.service.RoleService;
import com.effi.EffiApp.service.TaskService;
import com.effi.EffiApp.service.UserService;
import com.effi.EffiApp.utils.common.AccessCheckers;
import com.effi.EffiApp.utils.common.PrincipalGetter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.effi.EffiApp.utils.common.PrincipalGetter.getPrincipalInformation;

@Controller
@RequestMapping(Endpoints.TASKS_AND_PROFILES_PREFIX)
public class TasksAndProfilesController {
    private UserService userService;
    private TaskService taskService;
    private CompanyService companyService;
    private RoleService roleService;
    @Autowired
    public TasksAndProfilesController(UserService userService, TaskService taskService, CompanyService companyService,
                          RoleService roleService) {
        this.userService = userService;
        this.taskService = taskService;
        this.companyService = companyService;
        this.roleService = roleService;
    }
    @GetMapping(Endpoints.MAIN_PAGE)
    public String getTmpMain(Model model){
        PrincipalInformation principalInformation = PrincipalGetter.getPrincipalInformation(userService);

        List<Task> todaysTasks = taskService.findTaskByUserIdAndDeadline(principalInformation.getId().intValue(),
                Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        model.addAttribute("tasks", todaysTasks);

        return "main-page";
    }
    @GetMapping(Endpoints.USER_PROFILE)
    public String getUserTasks(@RequestParam("userId") int userId, Model model) throws Exception{
        PrincipalInformation principalInformation = PrincipalGetter.getPrincipalInformation(userService);

        AccessCheckers.checkProfileAccess(userService, principalInformation, userId);

        User user = userService.findUserAndHisTasksById(userId);

        model.addAttribute("user", user);
        model.addAttribute("tasks", user.getTasks());

        return "users-tasks-page";
    }

    @GetMapping(Endpoints.TASK_DETAILS)
    public String getTaskDetails(@RequestParam("taskId") int taskId, Model model) {
        PrincipalInformation principalInformation = getPrincipalInformation(userService);

        AccessCheckers.checkTaskAccess(taskService, principalInformation, taskId);

        Task task = taskService.findTaskById(taskId);

        model.addAttribute("task", task);
        model.addAttribute("taskUpdated", task);

        return "task-details";
    }

    @PostMapping(Endpoints.TASK_UPDATE)
    public String updateTask(@Valid @ModelAttribute("taskUpdated") Task taskUpdated){
        Task task = taskService.findTaskById(taskUpdated.getId());

        task.setDeadline(taskUpdated.getDeadline());
        task.setStatus(taskUpdated.getStatus());

        taskService.save(task);
        return "redirect:" + Endpoints.TASKS_AND_PROFILES_TASK_DETAILS + "?taskId=" + task.getId();
    }


    @GetMapping(Endpoints.MY_PROFILE)
    public String redirectToUsersTasks(){
        PrincipalInformation principalInformation = PrincipalGetter.getPrincipalInformation(userService);

        return "redirect:" + Endpoints.TASKS_AND_PROFILES_USER_PROFILE +"?userId=" + principalInformation.getId();
    }


    @GetMapping(Endpoints.TASK_DELETE)
    public String deleteTask(@RequestParam("taskId") int taskId){
        PrincipalInformation principalInformation = PrincipalGetter.getPrincipalInformation(userService);

        AccessCheckers.checkTaskAccess(taskService, principalInformation, taskId);

        //find user id to redirect to users tasks
        Long userId = taskService.findTaskById(taskId).getUser().getId();

        taskService.deleteTaskById(taskId);

        return "redirect:" + Endpoints.TASKS_AND_PROFILES_USER_PROFILE + "?userId=" + userId;
    }


    @GetMapping(Endpoints.NEW_TASK_FORM)
    public String showNewTaskForm(@RequestParam("userId") int userId, Model model){
        TaskRegistrationObject task = new TaskRegistrationObject();
        task.setUserId(userId);

        model.addAttribute("task", task);

        return "task-form";
    }

    @PostMapping(Endpoints.NEW_TASK_PROCESSING)
    public String processNewTask(
            @Valid @ModelAttribute("task") TaskRegistrationObject task,
            BindingResult bindingResult){

        //check if errors occurred, if occurred return filled form with error messages
        if(bindingResult.hasErrors()){
            return "task-form";
        }

        //retrieve the user
        User user = userService.findUserAndHisTasksById(task.getUserId());

        //create new task
        Task newTask = new Task();
        newTask.setTitle(task.getTitle());
        newTask.setDescription(task.getDescription());
        newTask.setDeadline(task.getDeadline());
        newTask.setStatus(Task.TaskStatus.NOT_DONE);

        //create connection user-task
        newTask.setUser(user);
        user.addTask(newTask);

        //save
        userService.save(user);

        return "redirect:" + Endpoints.TASKS_AND_PROFILES_USER_PROFILE + "?userId=" + task.getUserId();
    }
}

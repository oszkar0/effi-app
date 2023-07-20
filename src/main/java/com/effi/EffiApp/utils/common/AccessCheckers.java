package com.effi.EffiApp.utils.common;

import com.effi.EffiApp.security.PrincipalInformation;
import com.effi.EffiApp.service.TaskService;
import com.effi.EffiApp.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AccessCheckers {
    public static void checkTaskAccess(TaskService taskService, PrincipalInformation principalInformation, int taskId) throws AccessDeniedException {
        //check if task user is trying to access is from the users company
        boolean isTaskFromTheSameCompany = taskService.findTaskByCompanyId(principalInformation.getCompany().getId())
                .stream()
                .map(task -> task.getId())
                .anyMatch(id -> id.equals(taskId));

        if(!isTaskFromTheSameCompany){
            throw new AccessDeniedException("Access denied");
        }

        //check if task is users task
        boolean isTaskUsersTask = taskService.findTaskByUserId(principalInformation.getId().intValue())
                .stream()
                .map(task -> task.getId())
                .anyMatch(id -> id.equals(taskId));

        boolean isUserManager =
                principalInformation.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"));

        //check if user is a manager -> manager can access all tasks among company
        //if user is not manager -> he can only access his own tasks
        if(!isUserManager && !isTaskUsersTask){
            throw new AccessDeniedException("Access denied");
        }
    }

    public static void checkProfileAccess(UserService userService, PrincipalInformation principalInformation, int userId) throws Exception{
        //check if user is trying to access his own profile
        boolean isProfileUsersProfile = (userId == principalInformation.getId());

        if(isProfileUsersProfile){
            return;
        }

        //check if accessed userId is from the same company our logged user is
        boolean isProfileFromTheSameCompany = userService.findUserByCompanyId(principalInformation.getCompany().getId())
                .stream()
                .map(user -> user.getId())
                .anyMatch(id -> (id.intValue() == userId));

        if(!isProfileFromTheSameCompany) {
            throw new AccessDeniedException("Access denied");
        }

        //if we are at that point, it means that user wants to access not his profile, but somebodys from
        //the same company, only manager  can access profiles of other employees
        boolean isUserManager =
                principalInformation.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"));

        if(!isUserManager){
            throw new AccessDeniedException("Access denied");
        }
    }
}

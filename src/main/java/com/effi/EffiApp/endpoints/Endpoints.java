package com.effi.EffiApp.endpoints;

public class Endpoints {
    //employee controller endpoints
    public static final String EMPLOYEE_PREFIX = "/employees";

    public static final String EMPLOYEES_LIST = "/employees-list";
    public static final String NEW_EMPLOYEE_FORM = "/show-new-employee-form";
    public static final String NEW_EMPLOYEE_PROCESSING = "/process-new-employee";
    public static final String NEW_EMPLOYEE_INFO = "/show-new-employee-info";
    public static final String NEW_EMPLOYEE_PDF = "/create-new-employee-pdf";
    public static final String USER_DELETE = "/delete-user";

    public static final String EMPLOYEE_EMPLOYEES_LIST =  EMPLOYEE_PREFIX + EMPLOYEES_LIST;
    public static final String EMPLOYEE_NEW_EMPLOYEE_FORM = EMPLOYEE_PREFIX + NEW_EMPLOYEE_FORM;
    public static final String EMPLOYEE_NEW_EMPLOYEE_PROCESSING = EMPLOYEE_PREFIX + NEW_EMPLOYEE_PROCESSING;
    public static final String EMPLOYEE_NEW_EMPLOYEE_INFO = EMPLOYEE_PREFIX + NEW_EMPLOYEE_INFO;
    public static final String EMPLOYEE_NEW_EMPLOYEE_PDF = EMPLOYEE_PREFIX + NEW_EMPLOYEE_PDF;
    public static final String EMPLOYEE_USER_DELETE = EMPLOYEE_PREFIX + USER_DELETE;

    //exceptions controller endpoints
    public static final String EXCEPTION_PREFIX = "/error";

    public static final String ACCESS_DENIED = "/access-denied";
    public static final String EXCEPTION_ACCESS_DENIED = EXCEPTION_PREFIX + ACCESS_DENIED;;



    //login controller endpoints
    public static final String LOGIN_PREFIX = "";

    public static final String LOGGING = "/login";
    public static final String LOGGING_PROCESSING = "/process-logging";
    public static final String LOGOUT = "/logout";

    public static final String LOGIN_LOGGING_PROCESSING = LOGIN_PREFIX + LOGGING_PROCESSING;
    public static final String LOGIN_LOGGING = LOGIN_PREFIX + LOGGING;
    public static final String LOGIN_LOGOUT = LOGIN_PREFIX + LOGOUT;

    //registration controller
    public static final String REGISTRATION_PREFIX = "/register";

    public static final String NEW_COMPANY_FORM = "/show-new-company-form";
    public static final String NEW_COMPANY_PROCESSING = "/process-new-company";

    public static final String REGISTRATION_NEW_COMPANY_FORM = REGISTRATION_PREFIX + NEW_COMPANY_FORM;
    public static final String REGISTRATION_NEW_COMPANY_PROCESSING = REGISTRATION_PREFIX + NEW_COMPANY_PROCESSING;
    //tasks and profiles controller
    public static final String TASKS_AND_PROFILES_PREFIX = "/tasks-and-profiles";

    public static final String MAIN_PAGE = "/main-page";
    public static final String TASK_DETAILS = "/task-details";
    public static final String USER_PROFILE = "/user-profile";
    public static final String TASK_UPDATE = "/task-update";
    public static final String TASK_DELETE = "/delete-task";
    public static final String MY_PROFILE = "/my-profile";
    public static final String NEW_TASK_FORM = "/show-new-task-form";
    public static final String NEW_TASK_PROCESSING = "/process-new-task";


    public static final String TASKS_AND_PROFILES_MAIN_PAGE = TASKS_AND_PROFILES_PREFIX + MAIN_PAGE;
    public static final String TASKS_AND_PROFILES_TASK_DETAILS = TASKS_AND_PROFILES_PREFIX + TASK_DETAILS;
    public static final String TASKS_AND_PROFILES_USER_PROFILE = TASKS_AND_PROFILES_PREFIX + USER_PROFILE;
    public static final String TASKS_AND_PROFILES_TASK_UPDATE = TASKS_AND_PROFILES_PREFIX + TASK_UPDATE;
    public static final String TASKS_AND_PROFILES_TASK_DELETE = TASKS_AND_PROFILES_PREFIX + TASK_DELETE;
    public static final String TASKS_AND_PROFILES_MY_PROFILE = TASKS_AND_PROFILES_PREFIX + MY_PROFILE;
    public static final String TASKS_AND_PROFILES_NEW_TASK_FORM = TASKS_AND_PROFILES_PREFIX + NEW_TASK_FORM;
    public static final String TASKS_AND_PROFILES_NEW_TASK_PROCESSING = TASKS_AND_PROFILES_PREFIX + NEW_TASK_PROCESSING;
 }

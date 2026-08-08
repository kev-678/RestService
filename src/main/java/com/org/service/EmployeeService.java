package com.org.service;

import com.org.model.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> findAll();
    Employee save(Employee employee);
}

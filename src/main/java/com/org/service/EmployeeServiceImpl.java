package com.org.service;

import com.org.dao.EmployeeRepository;
import com.org.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{


    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee save(Employee employee) {
      return   employeeRepository.save(employee);
    }

    @Override
    @Cacheable(value = "employees", key = "#id")
    public Employee getEmployeeById(Long id) {
        System.out.println("Fetching from DB for id: " + id);

        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }



//    @CacheEvict(value = "employees", key = "#id")
//    public Employee updateEmployee(Long id, Employee employee) {
//        // update DB
//    }

    @CacheEvict(value = "employees", key = "#id")
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}

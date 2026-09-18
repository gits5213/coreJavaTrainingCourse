package com.sdet.projects.employees;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

    void add(Employee employee);

    Optional<Employee> findById(int id);

    List<Employee> listAll();
}

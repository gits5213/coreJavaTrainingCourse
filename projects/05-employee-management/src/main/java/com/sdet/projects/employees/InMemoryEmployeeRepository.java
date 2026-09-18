package com.sdet.projects.employees;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class InMemoryEmployeeRepository implements EmployeeRepository {

    private final List<Employee> employees = new ArrayList<>();

    @Override
    public void add(Employee employee) {
        Objects.requireNonNull(employee, "employee");
        if (findById(employee.getId()).isPresent()) {
            throw new IllegalArgumentException("Duplicate id: " + employee.getId());
        }
        employees.add(employee);
    }

    @Override
    public Optional<Employee> findById(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return Optional.of(employee);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Employee> listAll() {
        return List.copyOf(employees);
    }
}

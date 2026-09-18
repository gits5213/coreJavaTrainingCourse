package com.sdet.projects.employees;

public class Employee {

    private final int id;
    private final String name;
    private final String role;
    private final double salary;

    public Employee(int id, String name, String role, double salary) {
        if (id <= 0) {
            throw new IllegalArgumentException("id must be positive");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("role is required");
        }
        if (salary < 0) {
            throw new IllegalArgumentException("salary cannot be negative");
        }
        this.id = id;
        this.name = name;
        this.role = role;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return id + " | " + name + " | " + role + " | " + salary;
    }
}

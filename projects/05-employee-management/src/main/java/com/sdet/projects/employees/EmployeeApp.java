package com.sdet.projects.employees;

public class EmployeeApp {

    public static void main(String[] args) {
        EmployeeRepository repository = new InMemoryEmployeeRepository();
        repository.add(new Employee(1, "Aisha Khan", "SDET", 95_000));
        repository.add(new Employee(2, "Ben Ortiz", "QA Lead", 110_000));
        repository.add(new Employee(3, "Chen Li", "Automation Engineer", 88_000));

        System.out.println("All employees:");
        for (Employee employee : repository.listAll()) {
            System.out.println("  " + employee);
        }

        repository.findById(2).ifPresentOrElse(
                employee -> System.out.println("Found id 2: " + employee.getName()),
                () -> System.out.println("Found id 2: not found")
        );

        repository.findById(99).ifPresentOrElse(
                employee -> System.out.println("Missing id 99: " + employee.getName()),
                () -> System.out.println("Missing id 99: not found")
        );
    }
}

package com.sdet.projects.employees;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryEmployeeRepositoryTest {

    @Test
    void addThenListAll() {
        EmployeeRepository repository = new InMemoryEmployeeRepository();
        Employee aisha = new Employee(1, "Aisha Khan", "SDET", 95_000);

        repository.add(aisha);

        List<Employee> all = repository.listAll();
        assertEquals(1, all.size());
        assertEquals("Aisha Khan", all.get(0).getName());
    }

    @Test
    void findByIdReturnsMatch() {
        EmployeeRepository repository = new InMemoryEmployeeRepository();
        repository.add(new Employee(2, "Ben Ortiz", "QA Lead", 110_000));

        Optional<Employee> found = repository.findById(2);

        assertTrue(found.isPresent());
        assertEquals("Ben Ortiz", found.get().getName());
    }

    @Test
    void findByIdReturnsEmptyWhenMissing() {
        EmployeeRepository repository = new InMemoryEmployeeRepository();

        Optional<Employee> found = repository.findById(99);

        assertTrue(found.isEmpty());
    }

    @Test
    void duplicateIdIsRejected() {
        EmployeeRepository repository = new InMemoryEmployeeRepository();
        repository.add(new Employee(1, "Aisha Khan", "SDET", 95_000));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.add(new Employee(1, "Duplicate", "Intern", 40_000))
        );

        assertEquals("Duplicate id: 1", exception.getMessage());
    }
}

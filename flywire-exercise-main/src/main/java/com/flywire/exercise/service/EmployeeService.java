package com.flywire.exercise.service;

import com.flywire.exercise.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private static final String DATA_FILE = "data/employees.json";
    private final ObjectMapper objectMapper;
    private List<Employee> employees = new ArrayList<>();

    public EmployeeService() {
        this.objectMapper = new ObjectMapper();
    }

    public void initializeData() {
        System.out.println(DATA_FILE);
        try {
            File file = new File(DATA_FILE);
            System.out.println(file.exists());
            if (file.exists()) {
                employees = Arrays.asList(objectMapper.readValue(file, Employee[].class));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize employee data", e);
        }
    }

    public List<Employee> getAllEmployees() {
        return employees.stream()
                .filter(Employee::isActive)
                .sorted(Comparator.comparing(e -> e.getLastName() + e.getFirstName()))
                .collect(Collectors.toList());
    }

    public Employee getEmployeeById(String id) {
        return employees.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public List<Employee> getEmployeesByDateRange(LocalDate startDate, LocalDate endDate) {
        return employees.stream()
                .filter(Employee::isActive)
                .filter(e -> e.getDateHired().isAfter(startDate) && e.getDateHired().isBefore(endDate))
                .sorted(Comparator.comparing(Employee::getDateHired).reversed())
                .collect(Collectors.toList());
    }

    public Employee createEmployee(Employee employee) {
        if (employees.stream().anyMatch(e -> e.getId().equals(employee.getId()))) {
            throw new RuntimeException("Employee ID already exists");
        }
        employees.add(employee);
        saveData();
        return employee;
    }

    public void deactivateEmployee(String id) {
        Employee employee = getEmployeeById(id);
        employee.setActive(false);
        saveData();
    }

    private void saveData() {
        try {
            objectMapper.writeValue(new File(DATA_FILE), employees.toArray(new Employee[0]));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save employee data", e);
        }
    }
}


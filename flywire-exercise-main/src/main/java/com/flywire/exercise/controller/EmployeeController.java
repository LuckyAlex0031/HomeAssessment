package com.flywire.exercise.controller;

import com.flywire.exercise.model.Employee;
import com.flywire.exercise.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);
        List<Employee> directReports = employeeService.getAllEmployees()
                .stream()
                .filter(e -> e.getManagerId() != null && e.getManagerId().equals(id))
                .collect(Collectors.toList());
        
        employee.setDirectReports(directReports);
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/hired")
    public ResponseEntity<List<Employee>> getEmployeesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(employeeService.getEmployeesByDateRange(startDate, endDate));
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateEmployee(@PathVariable String id) {
        employeeService.deactivateEmployee(id);
        return ResponseEntity.noContent().build();
    }
}

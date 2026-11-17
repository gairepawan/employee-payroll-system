package com.portfolio.payroll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/** In-memory CRUD-lite directory to demonstrate HashMap and sorting. */
public final class EmployeeDirectory {
    private final Map<String, Employee> employees = new HashMap<>();

    public void add(Employee employee) {
        if (employee == null) throw new IllegalArgumentException("Employee is required");
        if (employees.putIfAbsent(employee.id(), employee) != null)
            throw new IllegalArgumentException("Duplicate employee ID: " + employee.id());
    }

    public Employee find(String id) {
        Employee employee = employees.get(id);
        if (employee == null) throw new NoSuchElementException("Employee not found: " + id);
        return employee;
    }

    public List<Employee> byName() {
        List<Employee> list = new ArrayList<>(employees.values());
        list.sort(Comparator.comparing(Employee::name, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Employee::id));
        return list;
    }

    public BigDecimal totalGrossPay(Map<String, BigDecimal> weeklyHours, BigDecimal withholdingRate) {
        BigDecimal total = BigDecimal.ZERO;
        for (Employee employee : employees.values()) {
            BigDecimal hours = weeklyHours.get(employee.id());
            if (hours == null) throw new IllegalArgumentException("Hours missing for " + employee.id());
            total = total.add(PayrollCalculator.weeklyPay(employee, hours, withholdingRate, BigDecimal.ZERO).grossPay());
        }
        return total;
    }

    public int size() { return employees.size(); }
}
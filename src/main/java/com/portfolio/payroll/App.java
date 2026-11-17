package com.portfolio.payroll;

import java.math.BigDecimal;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        EmployeeDirectory directory = new EmployeeDirectory();
        directory.add(new Employee("EMP-101", "Alex Rivera", Employee.PayType.HOURLY, new BigDecimal("20.00")));
        directory.add(new Employee("EMP-102", "Jordan Lee", Employee.PayType.SALARIED, new BigDecimal("1200.00")));
        directory.add(new Employee("EMP-103", "Taylor Morgan", Employee.PayType.HOURLY, new BigDecimal("25.00")));
        Map<String, BigDecimal> hours = Map.of("EMP-101", new BigDecimal("45"),
                "EMP-102", new BigDecimal("40"), "EMP-103", new BigDecimal("38"));
        BigDecimal exampleWithholding = new BigDecimal("0.10"); // illustration, NOT actual tax rate
        System.out.println("WEEKLY PAYROLL (illustrative 10% withholding)");
        for (Employee employee : directory.byName()) {
            Paycheck check = PayrollCalculator.weeklyPay(employee, hours.get(employee.id()),
                    exampleWithholding, new BigDecimal("25.00"));
            System.out.printf("%s | %s | gross $%s | net $%s%n", employee.id(), employee.name(),
                    check.grossPay(), check.netPay());
        }
        System.out.println("TOTAL GROSS: $" + directory.totalGrossPay(hours, exampleWithholding));
    }
}
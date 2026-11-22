package com.portfolio.payroll;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

class PayrollCalculatorTest {
    private static Employee hourly(String id, String name, String rate) {
        return new Employee(id, name, Employee.PayType.HOURLY, new BigDecimal(rate));
    }
    @Test void hourlyRegularPay() {
        Paycheck p = PayrollCalculator.weeklyPay(hourly("1", "Alex", "20.00"),
                new BigDecimal("40"), new BigDecimal("0.10"), new BigDecimal("20"));
        assertEquals(new BigDecimal("800.00"), p.grossPay());
        assertEquals(new BigDecimal("80.00"), p.withholding());
        assertEquals(new BigDecimal("700.00"), p.netPay());
    }
    @Test void overtimeAfterFortyHours() {
        Paycheck p = PayrollCalculator.weeklyPay(hourly("1", "Alex", "20.00"),
                new BigDecimal("45"), BigDecimal.ZERO, BigDecimal.ZERO);
        assertEquals(new BigDecimal("950.00"), p.grossPay());
    }
    @Test void salaryIsFixedWeeklyAmount() {
        Employee employee = new Employee("2", "Sam", Employee.PayType.SALARIED, new BigDecimal("1200"));
        Paycheck p = PayrollCalculator.weeklyPay(employee, new BigDecimal("43"),
                BigDecimal.ZERO, BigDecimal.ZERO);
        assertEquals(new BigDecimal("1200.00"), p.grossPay());
    }
    @Test void decimalPayRoundsAtCent() {
        Paycheck p = PayrollCalculator.weeklyPay(hourly("3", "Jane", "19.99"),
                new BigDecimal("1.25"), BigDecimal.ZERO, BigDecimal.ZERO);
        assertEquals(new BigDecimal("24.99"), p.grossPay());
    }
    @Test void rejectsInvalidInputs() {
        Employee e = hourly("1", "Alex", "20");
        assertThrows(IllegalArgumentException.class,
                () -> PayrollCalculator.weeklyPay(e, new BigDecimal("-1"), BigDecimal.ZERO, BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> PayrollCalculator.weeklyPay(e, BigDecimal.ZERO, new BigDecimal("1.5"), BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> PayrollCalculator.weeklyPay(e, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("", "Alex", Employee.PayType.HOURLY, BigDecimal.ONE));
    }
    @Test void employeeDirectoryFindsAndOrders() {
        EmployeeDirectory directory = new EmployeeDirectory();
        directory.add(hourly("2", "Zoe", "20"));
        directory.add(hourly("1", "Amy", "25"));
        assertEquals("Amy", directory.byName().getFirst().name());
        assertEquals("Zoe", directory.find("2").name());
        assertThrows(IllegalArgumentException.class, () -> directory.add(hourly("2", "Other", "22")));
        assertThrows(NoSuchElementException.class, () -> directory.find("missing"));
    }
    @Test void calculatesCompanyGrossTotal() {
        EmployeeDirectory directory = new EmployeeDirectory();
        directory.add(hourly("1", "Alex", "20"));
        directory.add(new Employee("2", "Sam", Employee.PayType.SALARIED, new BigDecimal("1200")));
        assertEquals(new BigDecimal("2000.00"), directory.totalGrossPay(
                Map.of("1", new BigDecimal("40"), "2", BigDecimal.ZERO), BigDecimal.ZERO));
    }

    @Test void removesEmployeesAndRejectsMissingIds() {
        EmployeeDirectory directory = new EmployeeDirectory();
        directory.add(hourly("E1", "Alex", "20"));
        assertEquals("Alex", directory.remove("E1").name());
        assertEquals(0, directory.size());
        assertThrows(NoSuchElementException.class, () -> directory.find("E1"));
        assertThrows(NoSuchElementException.class, () -> directory.remove("E1"));
    }
}
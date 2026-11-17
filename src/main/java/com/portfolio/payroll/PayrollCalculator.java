package com.portfolio.payroll;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Educational payroll only: fixed sample withholding, not an actual tax calculator. */
public final class PayrollCalculator {
    private static final BigDecimal FORTY = new BigDecimal("40");
    private static final BigDecimal OVERTIME_MULTIPLIER = new BigDecimal("1.5");
    private PayrollCalculator() {}

    public static Paycheck weeklyPay(Employee employee, BigDecimal hours, BigDecimal withholdingRate,
                                     BigDecimal otherDeductions) {
        Objects.requireNonNull(employee, "employee");
        Objects.requireNonNull(hours, "hours");
        Objects.requireNonNull(withholdingRate, "withholdingRate");
        Objects.requireNonNull(otherDeductions, "otherDeductions");
        if (hours.signum() < 0) throw new IllegalArgumentException("Hours cannot be negative");
        if (withholdingRate.signum() < 0 || withholdingRate.compareTo(BigDecimal.ONE) > 0)
            throw new IllegalArgumentException("Withholding must be between 0 and 1");
        if (otherDeductions.signum() < 0) throw new IllegalArgumentException("Deductions cannot be negative");

        BigDecimal gross;
        if (employee.payType() == Employee.PayType.SALARIED) {
            gross = employee.rate(); // defined as a fixed WEEKLY salary for this demo
        } else {
            BigDecimal regular = hours.min(FORTY);
            BigDecimal overtime = hours.subtract(FORTY).max(BigDecimal.ZERO);
            gross = regular.multiply(employee.rate())
                    .add(overtime.multiply(employee.rate()).multiply(OVERTIME_MULTIPLIER));
        }
        gross = money(gross);
        BigDecimal withholding = money(gross.multiply(withholdingRate));
        BigDecimal deductions = money(otherDeductions);
        BigDecimal net = gross.subtract(withholding).subtract(deductions);
        if (net.signum() < 0) throw new IllegalArgumentException("Deductions exceed gross pay");
        return new Paycheck(employee.id(), employee.name(), gross, withholding, deductions, net);
    }

    private static BigDecimal money(BigDecimal value) { return value.setScale(2, RoundingMode.HALF_UP); }
}
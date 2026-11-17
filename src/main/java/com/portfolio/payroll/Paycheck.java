package com.portfolio.payroll;

import java.math.BigDecimal;

/** Weekly summary; all monetary values rounded to two decimal places. */
public record Paycheck(String employeeId, String employeeName, BigDecimal grossPay,
                        BigDecimal withholding, BigDecimal deductions, BigDecimal netPay) {}
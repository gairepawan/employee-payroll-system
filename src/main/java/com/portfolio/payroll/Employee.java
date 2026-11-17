package com.portfolio.payroll;

import java.math.BigDecimal;
import java.util.Objects;

/** Immutable employee record. Rate is hourly or weekly salary depending on type. */
public record Employee(String id, String name, PayType payType, BigDecimal rate) {
    public enum PayType { HOURLY, SALARIED }
    public Employee {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Employee ID required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        Objects.requireNonNull(payType, "Pay type required");
        Objects.requireNonNull(rate, "Pay rate required");
        if (rate.signum() < 0) throw new IllegalArgumentException("Rate cannot be negative");
        id = id.trim();
        name = name.trim();
    }
}
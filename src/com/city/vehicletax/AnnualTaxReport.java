package com.city.vehicletax;

import java.math.BigDecimal;

import java.util.List;
import java.util.Objects;

public record AnnualTaxReport(int year, List<OwnerTaxSummary> owners,
                              BigDecimal grandTotal) {
    public AnnualTaxReport {
        if (year <= 0) {
            throw new IllegalArgumentException("Year must be positive");
        }

        owners = List.copyOf(owners);
        Objects.requireNonNull(grandTotal);
    }
}
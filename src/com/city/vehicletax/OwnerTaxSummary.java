package com.city.vehicletax;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public record OwnerTaxSummary(String licenseNumber, String ownerName,
                              List<VehicleTaxEntry> vehicles, BigDecimal totalTax) {
    public OwnerTaxSummary {
        Objects.requireNonNull(licenseNumber);
        Objects.requireNonNull(ownerName);
        vehicles = List.copyOf(vehicles);
        Objects.requireNonNull(totalTax);
    }
}
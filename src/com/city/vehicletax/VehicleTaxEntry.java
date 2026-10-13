package com.city.vehicletax;

import java.math.BigDecimal;
import java.util.Objects;


public record VehicleTaxEntry(String plateNumber, String vehicleType, BigDecimal annualTax) {

    public VehicleTaxEntry {
        Objects.requireNonNull(plateNumber);
        Objects.requireNonNull(vehicleType);
        Objects.requireNonNull(annualTax);
    }
}

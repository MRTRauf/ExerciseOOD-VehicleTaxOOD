package com.city.vehicletax;

import java.math.BigDecimal;

public final class HybridCar extends Car {

    public HybridCar(String plateNumber, String maker, String model, int co2Grams) {
        super(plateNumber, maker, model, co2Grams);
    }

    @Override
    protected BigDecimal euroPerGram() {
        return new BigDecimal("1.20");
    }

    @Override
    public String getTypeName() {
        return "Hybrid car";
    }

}


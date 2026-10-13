package com.city.vehicletax;


import java.math.BigDecimal;

public final class DieselCar extends Car {

    public DieselCar(String plateNumber, String maker, String model, int co2Grams) {
        super(plateNumber, maker, model, co2Grams);
    }

    @Override
    protected BigDecimal euroPerGram() {
        return new BigDecimal("1.80");
    }

    @Override
    public String getTypeName() {
        return "Diesel car";
    }
}
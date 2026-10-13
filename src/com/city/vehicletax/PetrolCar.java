package com.city.vehicletax;

import java.math.BigDecimal;

public final class PetrolCar extends Car {

    public PetrolCar(String plateNumber, String maker, String model, int co2Grams) {
        super(plateNumber, maker, model, co2Grams);
    }

    @Override
    protected BigDecimal euroPerGram() {
        return new BigDecimal("1.40");
    }

    @Override
    public String getTypeName() {
        return "Petrol car";
    }
}
package com.city.vehicletax;


import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Car extends Vehicle {
    private int co2Grams;

    protected Car(String plateNumber, String maker, String model, int co2Grams) {
        super(plateNumber, maker, model);
        updateCo2Grams(co2Grams);
    }

    public final int getCo2Grams() {
        return co2Grams;
    }

    public final void updateCo2Grams(int co2Grams) {
        this.co2Grams = Validation.nonNegative(co2Grams, "CO2 grams");
    }

    protected abstract BigDecimal euroPerGram();

    @Override
    public final BigDecimal calculateAnnualTax() {
        return euroPerGram().multiply(BigDecimal.valueOf(co2Grams))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
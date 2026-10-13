package com.city.vehicletax;


import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Motorcycle extends Vehicle {
    private int engineDisplacementCc;

    public Motorcycle(String plateNumber, String maker, String model, int engineDisplacementCc) {
        super(plateNumber, maker, model);
        updateEngineDisplacementCc(engineDisplacementCc);

    }

    public int getEngineDisplacementCc() {
        return engineDisplacementCc;
    }

    public void updateEngineDisplacementCc(int engineDisplacementCc) {
        this.engineDisplacementCc = Validation.positive(engineDisplacementCc, "Engine displacement (cc)");
    }

    @Override
    public BigDecimal calculateAnnualTax() {
        return new BigDecimal("0.10").multiply(BigDecimal.valueOf(engineDisplacementCc))
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getTypeName() {
        return "Motorcycle";
    }
}
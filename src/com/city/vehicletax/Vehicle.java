package com.city.vehicletax;

import java.math.BigDecimal;

public abstract class Vehicle {
    private final String plateNumber;
    private String maker;
    private String model;
    private Person owner;

    protected Vehicle(String plateNumber, String maker, String model) {
        this.plateNumber = Validation.nonBlank(plateNumber, "Plate number");
        updateDetails(maker, model);
    }

    public final String getPlateNumber() {
        return plateNumber;
    }

    public final String getMaker() {
        return maker;
    }

    public final String getModel() {
        return model;
    }

    public final Person getOwner() {
        return owner;
    }

    public final void updateDetails(String maker, String model) {
        String validMaker = Validation.nonBlank(maker, "Maker");
        String validModel = Validation.nonBlank(model, "Model");

        this.maker = validMaker;
        this.model = validModel;
    }

    final void changeOwner(Person newOwner) {
        this.owner = newOwner;
    }

    public abstract BigDecimal calculateAnnualTax();

    public abstract String getTypeName();
}


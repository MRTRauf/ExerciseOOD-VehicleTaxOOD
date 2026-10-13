package com.city.vehicletax;

import java.math.BigDecimal;

public final class DemoApp {
    private DemoApp() {
    }

    public static void main(String[] args) {
        VehicleTaxSystem system = new VehicleTaxSystem();

        system.addPerson(new Person("LIC-001", "Alya", "Putri", "Main Street 1"));
        system.addPerson(new Person("LIC-002", "Bima", "Santoso", "Oak Road 2"));
        system.addPerson(new Person("LIC-003", "Citra", "Dewi", "Pine Lane 3"));

        system.addVehicle(new PetrolCar("CAR-001", "Toyota", "Yaris", 120));
        system.addVehicle(new DieselCar("CAR-002", "Ford", "Focus", 100));
        system.addVehicle(new HybridCar("CAR-003", "Honda", "Jazz", 90));
        system.addVehicle(new Motorcycle("MOTO-001", "Yamaha", "NMAX", 150));

        BigDecimal saleTax = system.sellVehicle("CAR-001", "LIC-001");
        system.sellVehicle("CAR-002", "LIC-001");
        system.sellVehicle("MOTO-001", "LIC-002");
        system.transferVehicle("CAR-001", "LIC-002");

        System.out.println("Tax calculated on CAR-001 sale: EUR " + saleTax);
        System.out.println("CAR-001 transferred from Alya to Bima.");
        System.out.println("CAR-003 remains unowned. Citra has no vehicles.");
        System.out.println();

        printReport(system.generateAnnualTaxReport(2026));
    }

    private static void printReport(AnnualTaxReport report) {
        System.out.println("ANNUAL VEHICLE TAX REPORT " + report.year());
        System.out.println("------------------------------------------");

        for (OwnerTaxSummary owner : report.owners()) {
            System.out.println(owner.ownerName() + " [" + owner.licenseNumber() + "]");

            for (VehicleTaxEntry entry : owner.vehicles()) {
                System.out.printf("  %-10s %-12s EUR %s%n",
                        entry.plateNumber(),
                        entry.vehicleType(),
                        entry.annualTax());
            }

            System.out.println("  OWNER TOTAL: EUR " + owner.totalTax());
        }

        System.out.println("------------------------------------------");
        System.out.println("GRAND TOTAL: EUR " + report.grandTotal());
    }
}
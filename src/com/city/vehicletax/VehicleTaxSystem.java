package com.city.vehicletax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class VehicleTaxSystem {
    private final Map<String, Person> personsByLicense = new LinkedHashMap<>();
    private final Map<String, Vehicle> vehiclesByPlate = new LinkedHashMap<>();

    public void addPerson(Person person) {
        if (person == null) throw new IllegalArgumentException("Person must not be null");
        if (personsByLicense.containsKey(person.getLicenseNumber())) {
            throw new IllegalArgumentException("License number already exists: " + person.getLicenseNumber());
        }
        personsByLicense.put(person.getLicenseNumber(), person);
    }

    public void editPerson(String licenseNumber, String firstName, String surname, String address) {
        requirePerson(licenseNumber).updateDetails(firstName, surname, address);
    }

    public void deletePerson(String licenseNumber) {
        Person person = requirePerson(licenseNumber);
        for (Vehicle vehicle : vehiclesByPlate.values()) {
            if (vehicle.getOwner() == person) vehicle.changeOwner(null);
        }
        personsByLicense.remove(person.getLicenseNumber());
    }

    public Optional<Person> findPerson(String licenseNumber) {
        return Optional.ofNullable(personsByLicense.get(licenseNumber));
    }

    public List<Person> searchPersons(String query) {
        String q = searchQuery(query);
        List<Person> matches = new ArrayList<>();
        for (Person person : personsByLicense.values()) {
            boolean matchesName = contains(person.getFirstName(), q)
                    || contains(person.getSurname(), q);
            boolean matchesDetails = contains(person.getLicenseNumber(), q)
                    || contains(person.getAddress(), q);
            if (matchesName || matchesDetails) {
                matches.add(person);
            }
        }

        return List.copyOf(matches);
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicle == null) throw new IllegalArgumentException("Vehicle must not be null");
        if (vehicle.getOwner() != null) {
            throw new IllegalArgumentException("Register vehicles as unowned; use sellVehicle to assign a buyer");
        }
        if (vehiclesByPlate.containsKey(vehicle.getPlateNumber())) {
            throw new IllegalArgumentException("Plate number already exists: " + vehicle.getPlateNumber());
        }
        vehiclesByPlate.put(vehicle.getPlateNumber(), vehicle);
    }

    public void editVehicle(String plateNumber, String maker, String model) {
        requireVehicle(plateNumber).updateDetails(maker, model);
    }

    public void editCarCo2(String plateNumber, int co2Grams) {
        Vehicle vehicle = requireVehicle(plateNumber);
        if (!(vehicle instanceof Car car)) {
            throw new IllegalArgumentException("Vehicle is not a car: " + plateNumber);
        }
        car.updateCo2Grams(co2Grams);
    }

    public void editMotorcycleDisplacement(String plateNumber, int engineDisplacementCc) {
        Vehicle vehicle = requireVehicle(plateNumber);
        if (!(vehicle instanceof Motorcycle motorcycle)) {
            throw new IllegalArgumentException("Vehicle is not a motorcycle: " + plateNumber);
        }
        motorcycle.updateEngineDisplacementCc(engineDisplacementCc);
    }

    public void replaceVehicle(String plateNumber, Vehicle replacement) {
        Vehicle previous = requireVehicle(plateNumber);
        if (replacement == null || !plateNumber.equals(replacement.getPlateNumber())) {
            throw new IllegalArgumentException("Replacement must have the same plate number");
        }
        if (replacement.getOwner() != null) {
            throw new IllegalArgumentException("Replacement vehicle must start unowned");
        }
        replacement.changeOwner(previous.getOwner());
        vehiclesByPlate.put(plateNumber, replacement);
    }

    public void deleteVehicle(String plateNumber) {
        requireVehicle(plateNumber);
        vehiclesByPlate.remove(plateNumber);
    }

    public Optional<Vehicle> findVehicle(String plateNumber) {
        return Optional.ofNullable(vehiclesByPlate.get(plateNumber));
    }

    public List<Vehicle> searchVehicles(String query) {
        String q = searchQuery(query);
        List<Vehicle> matches = new ArrayList<>();
        for (Vehicle vehicle : vehiclesByPlate.values()) {
            boolean matchesIdentity = contains(vehicle.getPlateNumber(), q)
                    || contains(vehicle.getMaker(), q);
            boolean matchesDescription = contains(vehicle.getModel(), q)
                    || contains(vehicle.getTypeName(), q);
            if (matchesIdentity || matchesDescription) {
                matches.add(vehicle);
            }
        }
        return List.copyOf(matches);
    }

    public BigDecimal sellVehicle(String plateNumber, String buyerLicenseNumber) {
        Vehicle vehicle = requireVehicle(plateNumber);
        Person buyer = requirePerson(buyerLicenseNumber);
        if (vehicle.getOwner() != null) {
            throw new IllegalStateException("Vehicle is owned; use transferVehicle instead");
        }
        vehicle.changeOwner(buyer);
        return vehicle.calculateAnnualTax();
    }

    public void transferVehicle(String plateNumber, String newOwnerLicenseNumber) {
        Vehicle vehicle = requireVehicle(plateNumber);
        Person newOwner = requirePerson(newOwnerLicenseNumber);
        Person currentOwner = vehicle.getOwner();
        if (currentOwner == null) {
            throw new IllegalStateException("Vehicle has no owner; use sellVehicle instead");
        }
        if (currentOwner == newOwner) {
            throw new IllegalArgumentException("New owner must differ from current owner");
        }
        vehicle.changeOwner(newOwner);
    }

    public BigDecimal calculateAnnualTax(String plateNumber) {
        return requireVehicle(plateNumber).calculateAnnualTax();
    }

    public AnnualTaxReport generateAnnualTaxReport(int year) {
        return generateAnnualTaxReport(year, false);
    }

    public AnnualTaxReport generateAnnualTaxReport(int year, boolean includePeopleWithNoVehicles) {
        if (year <= 0) throw new IllegalArgumentException("Year must be positive");

        Map<String, List<VehicleTaxEntry>> grouped = new LinkedHashMap<>();
        for (Vehicle vehicle : vehiclesByPlate.values()) {
            Person owner = vehicle.getOwner();
            if (owner != null) {
                String ownerLicense = owner.getLicenseNumber();
                List<VehicleTaxEntry> ownerEntries = grouped.get(ownerLicense);
                if (ownerEntries == null) {
                    ownerEntries = new ArrayList<>();
                    grouped.put(ownerLicense, ownerEntries);
                }
                ownerEntries.add(new VehicleTaxEntry(vehicle.getPlateNumber(),
                        vehicle.getTypeName(), vehicle.calculateAnnualTax()));
            }
        }

        List<OwnerTaxSummary> summaries = new ArrayList<>();
        BigDecimal grandTotal = new BigDecimal("0.00");
        for (Person person : personsByLicense.values()) {
            List<VehicleTaxEntry> entries = grouped.getOrDefault(person.getLicenseNumber(), List.of());
            if (entries.isEmpty() && !includePeopleWithNoVehicles) continue;
            BigDecimal total = new BigDecimal("0.00");
            for (VehicleTaxEntry entry : entries) total = total.add(entry.annualTax());
            summaries.add(new OwnerTaxSummary(person.getLicenseNumber(), person.getFullName(),
                    entries, total));
            grandTotal = grandTotal.add(total);
        }
        return new AnnualTaxReport(year, summaries, grandTotal);
    }

    private Person requirePerson(String licenseNumber) {
        Person person = personsByLicense.get(licenseNumber);
        if (person == null) throw new IllegalArgumentException("Unknown license number: " + licenseNumber);
        return person;
    }

    private Vehicle requireVehicle(String plateNumber) {
        Vehicle vehicle = vehiclesByPlate.get(plateNumber);
        if (vehicle == null) throw new IllegalArgumentException("Unknown plate number: " + plateNumber);
        return vehicle;
    }

    private static String searchQuery(String query) {
        return query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
    }

    private static boolean contains(String value, String query) {
        return value.toLowerCase(Locale.ROOT).contains(query);
    }
}
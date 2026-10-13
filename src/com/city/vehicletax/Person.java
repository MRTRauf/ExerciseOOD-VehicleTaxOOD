
package com.city.vehicletax;

public final class Person {
    private final String licenseNumber;
    private String firstName;
    private String surname;
    private String address;

    public Person(String licenseNumber, String firstName, String surname, String address) {
        this.licenseNumber = Validation.nonBlank(licenseNumber, "License number");
        updateDetails(firstName, surname, address);
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public String getFullName() {
        return firstName + " " + surname;
    }

    public void updateDetails(String firstName, String surname, String address) {
        String validFirstName = Validation.nonBlank(firstName, "First name");
        String validSurname = Validation.nonBlank(surname, "Surname");
        String validAddress = Validation.nonBlank(address, "Address");

        this.firstName = validFirstName;
        this.surname = validSurname;
        this.address = validAddress;
    }
}


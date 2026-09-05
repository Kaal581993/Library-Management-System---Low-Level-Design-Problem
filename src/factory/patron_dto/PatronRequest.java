package factory.patron_dto;

import entity.PatronStatus;

public class PatronRequest {
    private String patronId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String userName;
    private String email;
    private entity.PatronType patronType;

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public entity.PatronType getPatronType() {
        return patronType;
    }

    public void setPatronType(entity.PatronType patronType) {
        this.patronType = patronType;
    }
}

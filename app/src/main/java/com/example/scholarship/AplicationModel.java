package com.example.newproj;

public class AplicationModel {
    private int id;
    private String fullname;
    private String surname;
    private String email;
    private String personalId;
    private String phone;
    private String field;
    private String level;
    private String pdfPath;
    private String scholarshipType;

    // Empty constructor (required by some libraries)
    public AplicationModel() {
    }

    // Full constructor
    public AplicationModel(int id,String fullname, String surname, String email, String personalId,
                           String phone, String field, String level, String pdfPath, String scholarshipType) {
        this.id = id;
        this.fullname = fullname;
        this.surname = surname;
        this.email = email;
        this.personalId = personalId;
        this.phone = phone;
        this.field = field;
        this.level = level;
        this.pdfPath = pdfPath;
        this.scholarshipType = scholarshipType;
    }

    // Getters
    public int getId() { return id; }
    public String getFullname() {
        return fullname;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPersonalId() {
        return personalId;
    }

    public String getPhone() {
        return phone;
    }

    public String getField() {
        return field;
    }

    public String getLevel() {
        return level;
    }

    public String getPdfPath() {
        return pdfPath;
    }
    public String getScholarshipType() {
        return scholarshipType;
    }


    // Setters
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }
    public void setScholarshipType(String scholarshipType) {
        this.scholarshipType = scholarshipType;
    }
}


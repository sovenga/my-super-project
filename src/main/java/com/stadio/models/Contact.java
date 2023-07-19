package com.stadio.models;

public class Contact {
    private String student_pk;
    private String contact_type_fk;
    private String contact_country_fk;
    private String contact_area_code;
    private String contact_number;
    private String capture_date;


    public Contact() {

    }

    public Contact(String student_pk, String contact_type_fk, String contact_country_fk, String contact_area_code, String contact_number, String capture_date) {
        this.student_pk = student_pk;
        this.contact_type_fk = contact_type_fk;
        this.contact_country_fk = contact_country_fk;
        this.contact_area_code = contact_area_code;
        this.contact_number = contact_number;
        this.capture_date = capture_date;
    }

    public String getStudent_pk() {
        return student_pk;
    }

    public void setStudent_pk(String student_pk) {
        this.student_pk = student_pk;
    }

    public String getContact_type_fk() {
        return contact_type_fk;
    }

    public void setContact_type_fk(String contact_type_fk) {
        this.contact_type_fk = contact_type_fk;
    }

    public String getContact_country_fk() {
        return contact_country_fk;
    }

    public void setContact_country_fk(String contact_country_fk) {
        this.contact_country_fk = contact_country_fk;
    }

    public String getContact_area_code() {
        return contact_area_code;
    }

    public void setContact_area_code(String contact_area_code) {
        this.contact_area_code = contact_area_code;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getCapture_date() {
        return capture_date;
    }

    public void setCapture_date(String capture_date) {
        this.capture_date = capture_date;
    }
}

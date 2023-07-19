package com.stadio.models;

public class Address {
    private String student_fk;
    private String type_of_address_fk;
    private String postcountry_fk;
    private String post_province_fk;
    private String address_1;
    private String address_2;
    private String address_3;
    private String address_4;
    private String postcode;
    private String capture_date;

    public Address() {
    }

    public Address(String student_fk, String type_of_address_fk, String postcountry_fk, String post_province_fk, String address_1, String address_2, String address_3, String address_4, String postcode, String capture_date) {
        this.student_fk = student_fk;
        this.type_of_address_fk = type_of_address_fk;
        this.postcountry_fk = postcountry_fk;
        this.post_province_fk = post_province_fk;
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.address_3 = address_3;
        this.address_4 = address_4;
        this.postcode = postcode;
        this.capture_date = capture_date;
    }

    public String getStudent_fk() {
        return student_fk;
    }

    public void setStudent_fk(String student_fk) {
        this.student_fk = student_fk;
    }

    public String getType_of_address_fk() {
        return type_of_address_fk;
    }

    public void setType_of_address_fk(String type_of_address_fk) {
        this.type_of_address_fk = type_of_address_fk;
    }

    public String getPostcountry_fk() {
        return postcountry_fk;
    }

    public void setPostcountry_fk(String postcountry_fk) {
        this.postcountry_fk = postcountry_fk;
    }

    public String getPost_province_fk() {
        return post_province_fk;
    }

    public void setPost_province_fk(String post_province_fk) {
        this.post_province_fk = post_province_fk;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getAddress_3() {
        return address_3;
    }

    public void setAddress_3(String address_3) {
        this.address_3 = address_3;
    }

    public String getAddress_4() {
        return address_4;
    }

    public void setAddress_4(String address_4) {
        this.address_4 = address_4;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCapture_date() {
        return capture_date;
    }

    public void setCapture_date(String capture_date) {
        this.capture_date = capture_date;
    }
}

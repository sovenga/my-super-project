package com.stadio.models;

public class Stadio_SRA {
    private int id;
    private String fname;
    private String lname;
    private String email;

    public Stadio_SRA(String fname, String lname, String email) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }
    public Stadio_SRA(int id, String fname, String lname, String email) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

package com.stadio.models;

public class Semester {
    private String sem_pk;
    private String sem_start_date;
    private String sem_end_date;

    public Semester() {
    }

    public Semester(String sem_pk, String sem_start_date, String sem_end_date) {
        this.sem_pk = sem_pk;
        this.sem_start_date = sem_start_date;
        this.sem_end_date = sem_end_date;
    }

    public String getSem_pk() {
        return sem_pk;
    }

    public void setSem_pk(String sem_pk) {
        this.sem_pk = sem_pk;
    }

    public String getSem_start_date() {
        return sem_start_date;
    }

    public void setSem_start_date(String sem_start_date) {
        this.sem_start_date = sem_start_date;
    }

    public String getSem_end_date() {
        return sem_end_date;
    }

    public void setSem_end_date(String sem_end_date) {
        this.sem_end_date = sem_end_date;
    }
}

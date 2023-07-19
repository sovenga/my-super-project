package com.stadio.models;

public class Student_Account {
    private String student_fk;
    private String sem_fk;
    private int transaction_fk;
    private String qual_fk;
    private double transaction_amount;
    private int num_items;
    private double transaction_total;
    private String transaction_date;
    private String transaction_account_note;
    private int captured_by;
    private String capture_date;
    private String ts;

    public Student_Account() {
    }

    public Student_Account(String student_fk, String sem_fk, int transaction_fk, String qual_fk, double transaction_amount, int num_items, double transaction_total, String transaction_date, String transaction_account_note, int captured_by, String capture_date, String ts) {
        this.student_fk = student_fk;
        this.sem_fk = sem_fk;
        this.transaction_fk = transaction_fk;
        this.qual_fk = qual_fk;
        this.transaction_amount = transaction_amount;
        this.num_items = num_items;
        this.transaction_total = transaction_total;
        this.transaction_date = transaction_date;
        this.transaction_account_note = transaction_account_note;
        this.captured_by = captured_by;
        this.capture_date = capture_date;
        this.ts = ts;
    }

    public String getStudent_fk() {
        return student_fk;
    }

    public void setStudent_fk(String student_fk) {
        this.student_fk = student_fk;
    }

    public String getSem_fk() {
        return sem_fk;
    }

    public void setSem_fk(String sem_fk) {
        this.sem_fk = sem_fk;
    }

    public int getTransaction_fk() {
        return transaction_fk;
    }

    public void setTransaction_fk(int transaction_fk) {
        this.transaction_fk = transaction_fk;
    }

    public String getQual_fk() {
        return qual_fk;
    }

    public void setQual_fk(String qual_fk) {
        this.qual_fk = qual_fk;
    }

    public double getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(double transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public int getNum_items() {
        return num_items;
    }

    public void setNum_items(int num_items) {
        this.num_items = num_items;
    }

    public double getTransaction_total() {
        return transaction_total;
    }

    public void setTransaction_total(double transaction_total) {
        this.transaction_total = transaction_total;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getTransaction_account_note() {
        return transaction_account_note;
    }

    public void setTransaction_account_note(String transaction_account_note) {
        this.transaction_account_note = transaction_account_note;
    }

    public int getCaptured_by() {
        return captured_by;
    }

    public void setCaptured_by(int captured_by) {
        this.captured_by = captured_by;
    }

    public String getCapture_date() {
        return capture_date;
    }

    public void setCapture_date(String capture_date) {
        this.capture_date = capture_date;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}

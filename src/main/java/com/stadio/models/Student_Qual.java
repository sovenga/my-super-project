package com.stadio.models;

public class Student_Qual {
    private String student_fk;
    private String qual_fk;
    private String sem_last_enrolled_fk;
    private String sem_obtained_fk;

    public Student_Qual() {
    }
    public Student_Qual(String sem_last_enrolled_fk) {
        this.sem_last_enrolled_fk = sem_last_enrolled_fk;
    }
    public Student_Qual(String student_fk, String qual_fk, String sem_last_enrolled_fk, String sem_obtained_fk) {
        this.student_fk = student_fk;
        this.qual_fk = qual_fk;
        this.sem_last_enrolled_fk = sem_last_enrolled_fk;
        this.sem_obtained_fk = sem_obtained_fk;
    }

    public String getStudent_fk() {
        return student_fk;
    }

    public void setStudent_fk(String student_fk) {
        this.student_fk = student_fk;
    }

    public String getQual_fk() {
        return qual_fk;
    }

    public void setQual_fk(String qual_fk) {
        this.qual_fk = qual_fk;
    }

    public String getSem_last_enrolled_fk() {
        return sem_last_enrolled_fk;
    }

    public void setSem_last_enrolled_fk(String sem_last_enrolled_fk) {
        this.sem_last_enrolled_fk = sem_last_enrolled_fk;
    }

    public String getSem_obtained_fk() {
        return sem_obtained_fk;
    }

    public void setSem_obtained_fk(String sem_obtained_fk) {
        this.sem_obtained_fk = sem_obtained_fk;
    }
}

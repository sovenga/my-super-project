package com.stadio.models;

public class Student {
    private String student_pk;
    private String fileref_fk;
    private String sg_fk;
    private String nationality_fk;
    private String id_number;
    private String crs_fk;
    private String title_fk;
    private String last_name;
    private String first_name;
    private String middle_name;
    private String previous_ln;
    private String dob;
    private String gender_fk;
    private String hl_fk;
    private String ds_fk;
    private String seeing_rating_fk;
    private String hearing_rating_fk;
    private String communication_rating_fk;
    private String walking_rating_fk;
    private String remembering_rating_fk;
    private String selfcare_rating_fk;
    private String ses_fk;
    private String equity_fk;
    private String equity_oth_fk;
    private String email;
    private String examvenue_fk;
    private String digital_pref;
    private String id_on_record;
    private String sc_on_record;
    private String hq_on_record;
    private String inactive_debt;
    private String withhold_qual;
    private String student_cancelled;
    private String mysbs_active;
    private String captured_by;
    private String capture_date;

    public Student() {
    }

    public Student(String student_pk, String id_number, String last_name, String first_name, String gender_fk) {
        this.student_pk = student_pk;
        this.id_number = id_number;
        this.last_name = last_name;
        this.first_name = first_name;
        this.gender_fk = gender_fk;
    }

    public Student(String student_pk, String fileref_fk, String sg_fk, String nationality_fk, String id_number, String crs_fk, String title_fk, String last_name, String first_name, String middle_name, String previous_ln, String dob, String gender_fk, String hl_fk, String ds_fk, String seeing_rating_fk, String hearing_rating_fk, String communication_rating_fk, String walking_rating_fk, String remembering_rating_fk, String selfcare_rating_fk, String ses_fk, String equity_fk, String equity_oth_fk, String email, String examvenue_fk, String digital_pref, String id_on_record, String sc_on_record, String hq_on_record, String inactive_debt, String withhold_qual, String student_cancelled, String mysbs_active, String captured_by, String capture_date) {
        this.student_pk = student_pk;
        this.fileref_fk = fileref_fk;
        this.sg_fk = sg_fk;
        this.nationality_fk = nationality_fk;
        this.id_number = id_number;
        this.crs_fk = crs_fk;
        this.title_fk = title_fk;
        this.last_name = last_name;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.previous_ln = previous_ln;
        this.dob = dob;
        this.gender_fk = gender_fk;
        this.hl_fk = hl_fk;
        this.ds_fk = ds_fk;
        this.seeing_rating_fk = seeing_rating_fk;
        this.hearing_rating_fk = hearing_rating_fk;
        this.communication_rating_fk = communication_rating_fk;
        this.walking_rating_fk = walking_rating_fk;
        this.remembering_rating_fk = remembering_rating_fk;
        this.selfcare_rating_fk = selfcare_rating_fk;
        this.ses_fk = ses_fk;
        this.equity_fk = equity_fk;
        this.equity_oth_fk = equity_oth_fk;
        this.email = email;
        this.examvenue_fk = examvenue_fk;
        this.digital_pref = digital_pref;
        this.id_on_record = id_on_record;
        this.sc_on_record = sc_on_record;
        this.hq_on_record = hq_on_record;
        this.inactive_debt = inactive_debt;
        this.withhold_qual = withhold_qual;
        this.student_cancelled = student_cancelled;
        this.mysbs_active = mysbs_active;
        this.captured_by = captured_by;
        this.capture_date = capture_date;
    }

    public String getStudent_pk() {
        return student_pk;
    }

    public void setStudent_pk(String student_pk) {
        this.student_pk = student_pk;
    }

    public String getFileref_fk() {
        return fileref_fk;
    }

    public void setFileref_fk(String fileref_fk) {
        this.fileref_fk = fileref_fk;
    }

    public String getSg_fk() {
        return sg_fk;
    }

    public void setSg_fk(String sg_fk) {
        this.sg_fk = sg_fk;
    }

    public String getNationality_fk() {
        return nationality_fk;
    }

    public void setNationality_fk(String nationality_fk) {
        this.nationality_fk = nationality_fk;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getCrs_fk() {
        return crs_fk;
    }

    public void setCrs_fk(String crs_fk) {
        this.crs_fk = crs_fk;
    }

    public String getTitle_fk() {
        return title_fk;
    }

    public void setTitle_fk(String title_fk) {
        this.title_fk = title_fk;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getPrevious_ln() {
        return previous_ln;
    }

    public void setPrevious_ln(String previous_ln) {
        this.previous_ln = previous_ln;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender_fk() {
        return gender_fk;
    }

    public void setGender_fk(String gender_fk) {
        this.gender_fk = gender_fk;
    }

    public String getHl_fk() {
        return hl_fk;
    }

    public void setHl_fk(String hl_fk) {
        this.hl_fk = hl_fk;
    }

    public String getDs_fk() {
        return ds_fk;
    }

    public void setDs_fk(String ds_fk) {
        this.ds_fk = ds_fk;
    }

    public String getSeeing_rating_fk() {
        return seeing_rating_fk;
    }

    public void setSeeing_rating_fk(String seeing_rating_fk) {
        this.seeing_rating_fk = seeing_rating_fk;
    }

    public String getHearing_rating_fk() {
        return hearing_rating_fk;
    }

    public void setHearing_rating_fk(String hearing_rating_fk) {
        this.hearing_rating_fk = hearing_rating_fk;
    }

    public String getCommunication_rating_fk() {
        return communication_rating_fk;
    }

    public void setCommunication_rating_fk(String communication_rating_fk) {
        this.communication_rating_fk = communication_rating_fk;
    }

    public String getWalking_rating_fk() {
        return walking_rating_fk;
    }

    public void setWalking_rating_fk(String walking_rating_fk) {
        this.walking_rating_fk = walking_rating_fk;
    }

    public String getRemembering_rating_fk() {
        return remembering_rating_fk;
    }

    public void setRemembering_rating_fk(String remembering_rating_fk) {
        this.remembering_rating_fk = remembering_rating_fk;
    }

    public String getSelfcare_rating_fk() {
        return selfcare_rating_fk;
    }

    public void setSelfcare_rating_fk(String selfcare_rating_fk) {
        this.selfcare_rating_fk = selfcare_rating_fk;
    }

    public String getSes_fk() {
        return ses_fk;
    }

    public void setSes_fk(String ses_fk) {
        this.ses_fk = ses_fk;
    }

    public String getEquity_fk() {
        return equity_fk;
    }

    public void setEquity_fk(String equity_fk) {
        this.equity_fk = equity_fk;
    }

    public String getEquity_oth_fk() {
        return equity_oth_fk;
    }

    public void setEquity_oth_fk(String equity_oth_fk) {
        this.equity_oth_fk = equity_oth_fk;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExamvenue_fk() {
        return examvenue_fk;
    }

    public void setExamvenue_fk(String examvenue_fk) {
        this.examvenue_fk = examvenue_fk;
    }

    public String getDigital_pref() {
        return digital_pref;
    }

    public void setDigital_pref(String digital_pref) {
        this.digital_pref = digital_pref;
    }

    public String getId_on_record() {
        return id_on_record;
    }

    public void setId_on_record(String id_on_record) {
        this.id_on_record = id_on_record;
    }

    public String getSc_on_record() {
        return sc_on_record;
    }

    public void setSc_on_record(String sc_on_record) {
        this.sc_on_record = sc_on_record;
    }

    public String getHq_on_record() {
        return hq_on_record;
    }

    public void setHq_on_record(String hq_on_record) {
        this.hq_on_record = hq_on_record;
    }

    public String getInactive_debt() {
        return inactive_debt;
    }

    public void setInactive_debt(String inactive_debt) {
        this.inactive_debt = inactive_debt;
    }

    public String getWithhold_qual() {
        return withhold_qual;
    }

    public void setWithhold_qual(String withhold_qual) {
        this.withhold_qual = withhold_qual;
    }

    public String getStudent_cancelled() {
        return student_cancelled;
    }

    public void setStudent_cancelled(String student_cancelled) {
        this.student_cancelled = student_cancelled;
    }

    public String getMysbs_active() {
        return mysbs_active;
    }

    public void setMysbs_active(String mysbs_active) {
        this.mysbs_active = mysbs_active;
    }

    public String getCaptured_by() {
        return captured_by;
    }

    public void setCaptured_by(String captured_by) {
        this.captured_by = captured_by;
    }

    public String getCapture_date() {
        return capture_date;
    }

    public void setCapture_date(String capture_date) {
        this.capture_date = capture_date;
    }
}

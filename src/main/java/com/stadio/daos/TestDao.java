package com.stadio.daos;

import java.sql.*;

public class TestDao {
    private String myDriver = "com.mysql.cj.jdbc.Driver";
    private String dbUrl = "jdbc:mysql://remotemysql.com:3306/PAhP5xO6Pc?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String LiveDbUrl = "jdbc:mysql://129.232.162.234:3306/sbs?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private Statement statement;
    private Connection connection;
    private PreparedStatement student_pstmt;
    private PreparedStatement student_qual_pstmt;
    PreparedStatement student_assress_pstmt;
    PreparedStatement student_contact_pstmt;

    Statement postcode_st;
    Statement student_st1;
    Statement student_st2;
    Statement student_qual_st;
    Statement student_qual_lk_st;
    Connection conn;

    public TestDao() throws SQLException, ClassNotFoundException {
        Class.forName(myDriver);
        //String url = dbUrl;
       /*String name = "root";
        String password = "";*/
        String url = dbUrl;
        String name = "PAhP5xO6Pc";
        String password = "voeqXu0cEt";
        connection = DriverManager.getConnection(url, name, password);
        statement = connection.createStatement();
    }
    public boolean checkDB() throws SQLException {
        String sql = "select * from user";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()){
            return true;
        }else {
            return false;
        }
    }
    public int insertIntoStudentLiveDB(String student_pk,String fileref_fk,String sg_fk,String nationality_fk,String id_number,String crs_fk,String title_fk,String last_name,String first_name,String middle_name,String previous_ln,String dob,String gender_fk,String hl_fk,String ds_fk,String seeing_rating_fk,String hearing_rating_fk,String communication_rating_fk,String walking_rating_fk,String remembering_rating_fk,String selfcare_rating_fk,String ses_fk,String equity_fk,String equity_oth_fk,String email,String examvenue_fk,String digital_pref,String id_on_record,String sc_on_record,String hq_on_record,String inactive_debt,String withhold_qual,String student_cancelled,String mysbs_active,String captured_by,String capture_date) throws SQLException {
        String sql = "INSERT INTO student(student_pk,fileref_fk,sg_fk,nationality_fk,id_number,crs_fk,title_fk,last_name,first_name,middle_name,previous_ln,dob,gender_fk,hl_fk,ds_fk,seeing_rating_fk,hearing_rating_fk,communication_rating_fk,walking_rating_fk,remembering_rating_fk,selfcare_rating_fk,ses_fk,equity_fk,email,examvenue_fk,digital_pref,id_on_record,sc_on_record,hq_on_record,inactive_debt,withhold_qual,student_cancelled,mysbs_active,captured_by,capture_date) " +
                "values ("+student_pk+",'"+fileref_fk+"','"+sg_fk+"','"+nationality_fk+"','"+id_number+"','"+crs_fk+"','"+title_fk+"','"+last_name+"','"+first_name+"','"+middle_name+"','"+previous_ln+"','"+dob+"','"+gender_fk+"','"+hl_fk+"','"+ds_fk+"',"+seeing_rating_fk+","+hearing_rating_fk+","+communication_rating_fk+"," +
                ""+walking_rating_fk+","+remembering_rating_fk+","+selfcare_rating_fk+",'"+ses_fk+"','"+equity_fk+"','"+email+"','"+examvenue_fk+"','"+digital_pref+"','"+id_on_record+"',"+sc_on_record+","+hq_on_record+","+inactive_debt+","+withhold_qual+","+student_cancelled+","+mysbs_active+","+captured_by+",'"+capture_date+"')";
        return statement.executeUpdate(sql);
    }
    public int insertContactData(long student_pk,String contact_type_fk,String contact_country_fk,String contact_area_code,String contact_number,String capture_date) throws SQLException {
        String sql = "insert into student_contact(student_contact_pk,student_fk,contact_type_fk,contact_country_fk,contact_area_code,contact_number,capture_date)  " +
                "values("+0+","+student_pk+",'"+contact_type_fk+"','"+contact_country_fk+"','"+contact_area_code+"','"+contact_number+"','"+capture_date+"')";
        return statement.executeUpdate(sql);
    }
    public int insertStudentAddress(long student_pk,String type_of_address_fk,String postcountry,String post_province_fk,String address_1,String address_2,String address_3,String address_4,String postcode,String capture_date) throws SQLException{
        String sql = "insert into student_address(student_address_pk,student_fk,type_of_address_fk,postcountry_fk,post_province_fk,address_1,address_2,address_3,address_4,postcode,capture_date) values("+0+","+student_pk+",'"+type_of_address_fk+"','"+postcountry+"','"+post_province_fk+"','"+address_1+"','"+address_2+"','"+address_3+"','"+address_4+"','"+postcode+"','"+capture_date+"')";
        return statement.executeUpdate(sql);
    }
    //ec_studentnumber, qual_fk, QUAL_ACH_STATUS, CURRENT_SEMESTER, DATE_FIRST_ENROLLED,CURRENT_SEMESTER,DATE_LAST_ENROLLED,CREDIT_AWARDED,QUAL_HONS_STATUS_PK
    public int insertQualification(String student_fk,String qual_fk,String qual_ach_status_fk,String sem_first_enrolled_fk,String date_first_enrolled,String CURRENT_SEMESTER,String DATE_LAST_ENROLLED,int CREDIT_AWARDED,String QUAL_HONS_STATUS_PK) throws SQLException {
        String sql = "insert into student_qual(student_qual_pk,student_fk,qual_fk,qual_ach_status_fk,sem_first_enrolled_fk,date_first_enrolled) values("+0+","+student_fk+",'"+qual_fk+"','"+qual_ach_status_fk+"','"+sem_first_enrolled_fk+"','"+date_first_enrolled +"')";
        return statement.executeUpdate(sql);
    }

    public boolean liveStudentExists(String studentNumber) throws SQLException {
        boolean found = false;
        String sql = "select * from student where student_pk = '"+studentNumber+"'";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()){
            found = true;
        }
        return found;
    }
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(myDriver);
        String url = dbUrl;
        String name = "root";
        String password = "";

        Connection con = DriverManager.getConnection(url, name, password);
        return con;
    }
    public void closeLIVEConnections() throws SQLException {
        statement.close();
        connection.close();
    }
    public void insertStudent(){
        //student_pstmt = conn.prepareStatement("INSERT INTO student(student_pk,fileref_fk,sg_fk,nationality_fk,id_number,crs_fk,title_fk,last_name,first_name,middle_name,previous_ln,dob,gender_fk,hl_fk,hl_oth_fk,ds_fk,seeing_rating_fk,hearing_rating_fk,communication_rating_fk,walking_rating_fk,remembering_rating_fk,selfcare_rating_fk,ses_fk,equity_fk,equity_oth_fk,email,sbs_email,examvenue_fk,digital_pref,id_on_record,sc_on_record,hq_on_record,inactive_debt,withhold_qual,student_cancelled,mysbs_active,captured_by,capture_date) values (?, ?, ?,?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

    }
}

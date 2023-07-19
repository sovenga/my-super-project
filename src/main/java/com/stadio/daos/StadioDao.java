package com.stadio.daos;

import com.stadio.models.Semester;
import com.stadio.models.Student;
import com.stadio.models.Student_Account;
import com.stadio.models.Student_Qual;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StadioDao {
    private String myDriver = "com.mysql.cj.jdbc.Driver";
    //private String dbUrl = "jdbc:mysql://localhost:3306/livedb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    //private String LiveDbUrl = "jdbc:mysql://129.232.162.234:3306/sbs?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String LiveDbUrl = "jdbc:mysql://41.72.155.42:3306/sbs?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    //private String LiveDbUrl = "jdbc:mysql://41.72.151.160:3306/sbs?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private Statement statement;
    private Connection connection;

    public StadioDao() throws SQLException, ClassNotFoundException {
        Class.forName(myDriver);

       /*String name = "root";
        String password = "";*/
        String url = LiveDbUrl;
        //String url = dbUrl;
        String name = "mdynamicssbs";
        String password = "vdEI&zLxUCHw";
        connection = DriverManager.getConnection(url, name, password);
        statement = connection.createStatement();
    }

    public int saveNote(String student_fk, String capture_date, String semester) throws SQLException {
        String locPriority = "M";
        int capturedByID = 99;
        String strStudent_note = "CRM Studnt Number "+student_fk+" ("+ semester+")";
        String sql = "insert into student_note(student_note_pk,student_fk, student_note, note_priority_fk,captured_by, capture_date) " +
                "values(" + 0 + ",'" + student_fk + "','" + strStudent_note + "','" + locPriority + "','" + capturedByID + "','" + capture_date + "')";
        return statement.executeUpdate(sql);
    }
    public int saveNoteForExistingStudents(String student_fk, String capture_date) throws SQLException {
        String locPriority = "M";
        int capturedByID = 99;
        String strStudent_note = "CRM ISSUED STUDENT NUMBER " + student_fk;
        String sql = "insert into student_note(student_note_pk,student_fk, student_note, note_priority_fk,captured_by, capture_date) " +
                "values(" + 0 + ",'" + student_fk + "','" + strStudent_note + "','" + locPriority + "','" + capturedByID + "','" + capture_date + "')";
        return statement.executeUpdate(sql);
    }
    public List<Student> getStudents() throws SQLException {
        ArrayList<Student> students = new ArrayList<Student>();
        String sql = "SELECT distinct student.student_pk,student.id_number,student.last_name,student.first_name,student.gender_fk FROM student INNER JOIN student_qual ON student_qual.student_fk = student.student_pk INNER JOIN student_reg ON student_reg.student_qual_fk = student_qual.student_qual_pk INNER JOIN reg ON reg.reg_pk = student_reg.reg_fk WHERE  student_qual.sem_first_enrolled_fk BETWEEN CAST('2016-01-01' AS DATE) AND CAST('2021-08-31' AS DATE)";
        ResultSet rs = statement.executeQuery(sql);
        while(rs.next()){
            students.add( new Student(rs.getString("student_pk"),rs.getString("id_number"),rs.getString("last_name"),rs.getString("first_name"),rs.getString("gender_fk")));
        }
        return students;
    }
    public List<Semester> getSemester(String startSemester, String semesterCycle) throws SQLException {
        ArrayList<Semester> semesters = new ArrayList<Semester>();
        String semSql = "select sem_pk,sem_start_date,sem_end_date from lk_sem where sem_pk = '" + startSemester + semesterCycle + "'";
        ResultSet semSQLSR = statement.executeQuery(semSql);
        if(semSQLSR.next()){
            semesters.add( new Semester(semSQLSR.getString(1),semSQLSR.getString(2),semSQLSR.getString(3)));
        }
        return semesters;
    }
    public int saveNote_v2(String student_fk, String capture_date, String semester,String oldBSBStudNo) throws SQLException {
        String locPriority = "M";
        int capturedByID = 99;
        String strStudent_note = "CRM " + semester+" Old student : "+oldBSBStudNo;
        String sql = "insert into student_note(student_note_pk,student_fk, student_note, note_priority_fk,captured_by, capture_date) " +
                "values(" + 0 + ",'" + student_fk + "','" + strStudent_note + "','" + locPriority + "','" + capturedByID + "','" + capture_date + "')";
        return statement.executeUpdate(sql);
    }

    public int insertIntoStudentLiveDB(String student_pk, String fileref_fk, String sg_fk, String nationality_fk, String id_number, String crs_fk, String title_fk, String last_name, String first_name, String middle_name, String previous_ln, String dob, String gender_fk, String hl_fk, String ds_fk, String seeing_rating_fk, String hearing_rating_fk, String communication_rating_fk, String walking_rating_fk, String remembering_rating_fk, String selfcare_rating_fk, String ses_fk, String equity_fk, String equity_oth_fk, String email, String examvenue_fk, String digital_pref, String id_on_record, String sc_on_record, String hq_on_record, String inactive_debt, String withhold_qual, String student_cancelled, String mysbs_active, String captured_by, String capture_date,String crm_student_number,int excl_crm) throws SQLException {
        String sql = "INSERT INTO student(student_pk,fileref_fk,sg_fk,nationality_fk,id_number,crs_fk,title_fk,last_name,first_name,middle_name,previous_ln,dob,gender_fk,hl_fk,ds_fk,seeing_rating_fk,hearing_rating_fk,communication_rating_fk,walking_rating_fk,remembering_rating_fk,selfcare_rating_fk,ses_fk,equity_fk,email,examvenue_fk,digital_pref,id_on_record,sc_on_record,hq_on_record,inactive_debt,withhold_qual,student_cancelled,mysbs_active,captured_by,capture_date,crm_student_number,excl_crm) " +
                "values (" + student_pk + ",'" + fileref_fk + "','" + sg_fk + "','" + nationality_fk + "','" + id_number + "','" + crs_fk + "','" + title_fk + "','" + last_name + "','" + first_name + "','" + middle_name + "','" + previous_ln + "','" + dob + "','" + gender_fk + "','" + hl_fk + "','" + ds_fk + "'," + seeing_rating_fk + "," + hearing_rating_fk + "," + communication_rating_fk + "," +
                "" + walking_rating_fk + "," + remembering_rating_fk + "," + selfcare_rating_fk + ",'" + ses_fk + "','" + equity_fk + "','" + email + "','" + examvenue_fk + "','" + digital_pref + "','" + id_on_record + "'," + sc_on_record + "," + hq_on_record + "," + inactive_debt + "," + withhold_qual + "," + student_cancelled + "," + mysbs_active + "," + "99" + ",'" + capture_date + "','"+crm_student_number+"','"+excl_crm+"')";
        return statement.executeUpdate(sql);
    }
    public int insertIntoStudentLiveDB(String student_pk, String fileref_fk, String sg_fk, String nationality_fk, String id_number, String crs_fk, String title_fk, String last_name, String first_name, String middle_name, String previous_ln, String dob, String gender_fk, String hl_fk, String ds_fk, String seeing_rating_fk, String hearing_rating_fk, String communication_rating_fk, String walking_rating_fk, String remembering_rating_fk, String selfcare_rating_fk, String ses_fk, String equity_fk, String equity_oth_fk, String email, String examvenue_fk, String digital_pref, String id_on_record, String sc_on_record, String hq_on_record, String inactive_debt, String withhold_qual, String student_cancelled, String mysbs_active, String captured_by, String capture_date,String crm_student_number,int excl_crm,int coprincipal_lk,String ubw_costc,String type_stu) throws SQLException {
        String sql = "INSERT INTO student(student_pk,fileref_fk,sg_fk,nationality_fk,id_number,crs_fk,title_fk,last_name,first_name,middle_name,previous_ln,dob,gender_fk,hl_fk,ds_fk,seeing_rating_fk,hearing_rating_fk,communication_rating_fk,walking_rating_fk,remembering_rating_fk,selfcare_rating_fk,ses_fk,equity_fk,email,examvenue_fk,digital_pref,id_on_record,sc_on_record,hq_on_record,inactive_debt,withhold_qual,student_cancelled,mysbs_active,captured_by,capture_date,crm_student_number,excl_crm,coprincipal_lk,ubw_costc,type_stu) " +
                "values (" + student_pk + ",'" + fileref_fk + "','" + sg_fk + "','" + nationality_fk + "','" + id_number + "','" + crs_fk + "','" + title_fk + "','" + last_name + "','" + first_name + "','" + middle_name + "','" + previous_ln + "','" + dob + "','" + gender_fk + "','" + hl_fk + "','" + ds_fk + "'," + seeing_rating_fk + "," + hearing_rating_fk + "," + communication_rating_fk + "," +
                "" + walking_rating_fk + "," + remembering_rating_fk + "," + selfcare_rating_fk + ",'" + ses_fk + "','" + equity_fk + "','" + email + "','" + examvenue_fk + "','" + digital_pref + "','" + id_on_record + "'," + sc_on_record + "," + hq_on_record + "," + inactive_debt + "," + withhold_qual + "," + student_cancelled + "," + mysbs_active + "," + "99" + ",'" + capture_date + "','"+crm_student_number+"','"+excl_crm+"','"+coprincipal_lk+"','"+ubw_costc+"','"+type_stu+"')";
        return statement.executeUpdate(sql);
    }
    public String getliveIDnumber(String ec_stud_no) throws SQLException {
        String sql = "select id_number from student where student_pk = '"+ec_stud_no+"' and id_number is not null";
        ResultSet rs = statement.executeQuery(sql);
        String res = "";
        if(rs.next()){
            res = rs.getString(1);
        }
        return res;
    }

    public boolean isIDNumberFoundInLiveDB(String localIDNum) throws SQLException {
        String sql = "select* from student where id_number = '"+localIDNum+"'";
        ResultSet rs = statement.executeQuery(sql);
        boolean found = false;
        if(rs.next()){
            found = true;
        }
        return found;
    }
    public String getLIVEStudNumber(String localIDnum) throws SQLException {
        String sql = "select student_pk from student where id_number = '"+localIDnum+"' and id_number is not null";
        ResultSet rs = statement.executeQuery(sql);
        String res = "";
        if(rs.next()){
            res = rs.getString(1);
        }
        return res;
    }

    public String getIDnumber(String ec_stud_no) throws SQLException {
        String sql = "select id_number from student where student_pk = '"+ec_stud_no+"' and id_number is not null";
        ResultSet rs = statement.executeQuery(sql);
        String res = "";
        if(rs.next()){
            res = rs.getString(1);
        }
        return res;
    }
    public int updateCrmIssuedStudentNumber(String crmStudentNumber,String idNo) throws SQLException {
        String sql = "update student set crm_student_number ='"+crmStudentNumber+"', excl_crm =1 where id_number = '"+idNo+"'";
        return statement.executeUpdate(sql);
    }
    public int insertContactData(String student_pk, String contact_type_fk, String contact_country_fk, String contact_area_code, String contact_number, String capture_date) throws SQLException {
        String sql = "insert into student_contact(student_contact_pk,student_fk,contact_type_fk,contact_country_fk,contact_area_code,contact_number,capture_date)  " +
                "values(" + 0 + "," + student_pk + ",'" + contact_type_fk + "','" + contact_country_fk + "','" + contact_area_code + "','" + contact_number + "','" + capture_date + "')";
        return statement.executeUpdate(sql);
    }

    public int insertStudentAddress(String student_pk, String type_of_address_fk, String postcountry, String post_province_fk, String address_1, String address_2, String address_3, String address_4, String postcode, String capture_date) throws SQLException {
        String sql = "insert into student_address(student_address_pk,student_fk,type_of_address_fk,postcountry_fk,post_province_fk,address_1,address_2,address_3,address_4,postcode,capture_date) " +
                "values(" + 0 + "," + student_pk + ",'" + type_of_address_fk + "','" + postcountry + "','" + post_province_fk + "','" + address_1 + "','" + address_2 + "','" + address_3 + "','" + address_4 + "','" + postcode + "','" + capture_date + "')";
        return statement.executeUpdate(sql);
    }

    public int updateOldStudentNumber(String CRM_STUDENTNO, String id_num) throws SQLException {
        String sql = "update student set student_pk = " + CRM_STUDENTNO+" ,crm_student_number ='"+CRM_STUDENTNO+"', excl_crm =1 where id_number = '"+id_num + "'";
        return statement.executeUpdate(sql);
    }
    public String getOldStudentNumber(String id_number) throws SQLException {
        String ol_stud_num = "";
        String sql = "select old_student_number from student where id_number = '"+id_number+"' and old_student_number IS NOT NULL";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()){
            ol_stud_num = rs.getString(1);
        }
        return ol_stud_num;
    }
    public boolean getOldStudentNumberBoolean(String id_number) throws SQLException {
        String ol_stud_num = "";
        String sql = "select old_student_number from student where id_number = '"+id_number+"' and old_student_number IS NOT NULL";
        ResultSet rs = statement.executeQuery(sql);
        boolean found = false;
        if(rs.next()){
            if(rs.getString(1) != null){
                found = true;
            }
        }
        return found;
    }

    //ec_studentnumber, qual_fk, QUAL_ACH_STATUS, CURRENT_SEMESTER, DATE_FIRST_ENROLLED,CURRENT_SEMESTER,DATE_LAST_ENROLLED,CREDIT_AWARDED,QUAL_HONS_STATUS_PK
    public int insertQualification(String student_fk, String qual_fk, String stream_fk, String qual_ach_status_fk, String sem_first_enrolled_fk, String date_first_enrolled, String CURRENT_SEMESTER, String DATE_LAST_ENROLLED, int CREDIT_AWARDED, String QUAL_HONS_STATUS_PK) throws SQLException {
        String sql = "insert into student_qual(student_qual_pk,student_fk,qual_fk,stream_fk,qual_ach_status_fk,sem_first_enrolled_fk,date_first_enrolled,sem_last_enrolled_fk,DATE_LAST_ENROLLED,CREDIT_AWARDED,QUAL_HONS_STATUS_FK) " +
                "values(" + 0 + "," + student_fk + ",'" + qual_fk + "','" + stream_fk + "','" + qual_ach_status_fk + "','" + sem_first_enrolled_fk + "','" + date_first_enrolled + "','" + CURRENT_SEMESTER + "','" + DATE_LAST_ENROLLED + "'," + CREDIT_AWARDED + ",'" + QUAL_HONS_STATUS_PK + "')";
        return statement.executeUpdate(sql);
    }
    public boolean checkQualExistFirst(String student_fk, String qual_fk) throws SQLException {
        String sql = "select * from student_qual where student_fk ='" + student_fk + "' and qual_fk = '" + qual_fk + "'";
        ResultSet rs = statement.executeQuery(sql);
        boolean found = false;
        if (rs.next()) {
            found = true;
        }
        return found;
    }
    public boolean checkNote(String studentFK) throws SQLException {
        String sql = "select * from student_note where student_fk="+studentFK;
        ResultSet rs = statement.executeQuery(sql);
        boolean found = false;
        while(rs.next()){
            found = true;
        }
        return found;
    }
    public boolean studentMaxSemester(String studentfk) throws SQLException {
        String sql = "SELECT MAX(sem_last_enrolled_fk) FROM student_qual WHERE student_fk ="+studentfk;
        boolean found = false;
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()){
            found = true;
        }
        return found;
    }
    public String getMajorStreamLive(String stream_name) throws SQLException {
        String streamFk = "---";
        String sql = "select stream_pk from lk_stream where crm_stream_id = '"+stream_name+"'";
        ResultSet streamSql = statement.executeQuery(sql);
        if(streamSql.next()){
            streamFk = streamSql.getString(1);
        }
        return streamFk;
    }
    public String getStringStudentMaximumSemester(String studentfk) throws SQLException {
        String sql = "SELECT MAX(sem_last_enrolled_fk) FROM student_qual WHERE student_fk ="+studentfk;
        ResultSet rs = statement.executeQuery(sql);
        String res = "";
        while(rs.next()){
            res = rs.getString(1);
        }
        return res;
    }
    public List<Student_Qual> getStudentMaximumSemester(String studentfk) throws SQLException {
        String sql = "SELECT MAX(sem_last_enrolled_fk) FROM student_qual WHERE student_fk ="+studentfk;
        ResultSet rs = statement.executeQuery(sql);
        List<Student_Qual> result = new ArrayList<Student_Qual>();
        while(rs.next()){
            result.add(new Student_Qual(rs.getString(1)));
        }
        return result;
    }

    public String getStudentObtainedSemester(String student_fk,String latestQual) throws SQLException {
        String sql = "SELECT sem_obtained_fk FROM student_qual WHERE qual_fk ='"+latestQual+"' and student_fk ='"+student_fk+"'";
        ResultSet rs = statement.executeQuery(sql);
        String result = "";
        if(rs.next()){
            result = rs.getString(1);
        }
        return result;
    }
    public String getStudentMaximumSemesterQual_fk(String maxSemEnrolledFK) throws SQLException {
        String sql = "SELECT qual_fk FROM student_qual WHERE sem_last_enrolled_fk ='"+maxSemEnrolledFK+"'";
        ResultSet rs = statement.executeQuery(sql);
        String result = "";
        if(rs.next()){
            result = rs.getString(1);
        }
        return result;
    }
    public boolean checkQualExistFirst(String student_fk, String qual_fk, String semester_last_enrol) throws SQLException {
        String sql = "select * from student_qual where student_fk ='" + student_fk + "' and qual_fk = '" + qual_fk + "' and sem_last_enrolled_fk ='" + semester_last_enrol + "'";
        ResultSet rs = statement.executeQuery(sql);
        boolean found = false;
        if (rs.next()) {
            found = true;
        }
        return found;
    }
    public String getSBSStudentNumber(String idNumber) throws SQLException {
        String sql = "select student_pk from student where id_number ='"+idNumber+"'";
        ResultSet rs = statement.executeQuery(sql);
        String result = "";
        if(rs.next()){
            result = rs.getString(1);
        }
        return result;
    }
    public int insertStudentAccount(String student_fk,String sem_fk,int transaction_fk,String qual_fk,double transaction_amount,int num_items,double transaction_total,String transaction_date,String transaction_account_note,int captured_by,String capture_date) throws SQLException {
        String sql = "insert into student_account(student_account_pk,student_fk,sem_fk,transaction_fk,qual_fk,transaction_amount,num_items,transaction_total,transaction_date,transaction_account_note,captured_by,capture_date)" +
                "values("+0+","+student_fk+",'"+sem_fk+"',"+transaction_fk+",'"+qual_fk+"',"+transaction_amount+","+num_items+","+transaction_total+",'"+transaction_date+"','"+transaction_account_note+"',"+captured_by+",'"+capture_date+"')";
        return statement.executeUpdate(sql);
    }
    public List<Student_Account> getAccountData(String qual_fk) throws SQLException {
        List<Student_Account> student_account = new ArrayList<Student_Account>();
        String sql = "SELECT student_fk,sem_fk,transaction_fk,qual_fk,transaction_amount,num_items,transaction_total,transaction_date,transaction_account_note,captured_by,capture_date FROM sbs.student_account WHERE qual_fk = '"+qual_fk+"' AND transaction_fk = '58'";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            student_account.add( new Student_Account(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getDouble(5),rs.getInt(6),rs.getDouble(7),rs.getString(8),rs.getString(9),rs.getInt(10),rs.getString(11),rs.getString(12)));
        }
        return student_account;
    }
    public double getARegFeeAmount(String qual_fk,String sem_fk) throws SQLException {
        String sql = "select reg_fee from qual_sem where qual_fk = '"+qual_fk+"' and sem_fk = '"+sem_fk+"'";
        ResultSet rs = statement.executeQuery(sql);
        double reg_amount = 0;
        if (rs.next()){
            reg_amount = rs.getDouble(1);
        }
        return reg_amount;
    }

    public boolean getExistingStudentAddress(String student_fk, String type_of_address_fk, String postcountry, String post_province_fk, String address_1, String address_2, String address_3, String address_4, String postcode) throws SQLException {
        String sql = "select * from student_address where student_fk ='" + student_fk + "' and type_of_address_fk ='" + type_of_address_fk + "' and postcountry_fk ='" + postcountry + "' and post_province_fk = '" + post_province_fk + "' and address_1 ='" + address_1 + "' and address_2='" + address_2 + "' and address_3 ='" + address_3 + "' and address_4 ='" + address_4 + "' and postcode ='" + postcode + "'";
        ResultSet rs = statement.executeQuery(sql);
        boolean found = false;
        if (rs.next()) {
            found = true;
        }
        return found;
    }
    public boolean getExistingStudentAddressPostal(String student_fk, String type_of_address_fk) throws SQLException {
        String sql = "select * from student_address where student_fk ='" + student_fk + "' and type_of_address_fk = 'P'";
        ResultSet rs = statement.executeQuery(sql);
        boolean found = false;
        if (rs.next()) {
            found = true;
        }
        return found;
    }

    public boolean getExistingStudentContact(String student_fk, String contact_type_fk, String contact_country_fk, String contact_area_code, String contact_number) throws SQLException {
        String sql = "select * from student_contact where student_fk ='" + student_fk + "' and contact_type_fk ='" + contact_type_fk + "' and contact_country_fk ='" + contact_country_fk + "' and contact_area_code ='" + contact_area_code + "' and contact_number ='" + contact_number + "'";
        ResultSet rs = statement.executeQuery(sql);
        boolean found = false;
        if (rs.next()) {
            found = true;
        }
        return found;
    }

    public boolean liveliveStudentExistsWithID(String idNumber) throws SQLException {
        boolean found = false;
        String sql = "select * from student where id_number ='" + idNumber + "'";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            found = true;
        }
        return found;
    }

    public boolean liveLiveStudentExists(String studentNumber) throws SQLException {
        boolean found = false;
        String sql = "select * from student where student_pk = '" + studentNumber + "'";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            found = true;
        }
        return found;
    }

    public boolean liveLiveStudentExistsWithID(String idNumber) throws SQLException {
        boolean found = false;
        String sql = "select * from student where id_number ='" + idNumber + "'";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            found = true;
        }
        return found;
    }

    public void closeLIVEConnections() throws SQLException {
        statement.close();
        connection.close();
    }

    public void insertStudent() {
        //student_pstmt = conn.prepareStatement("INSERT INTO student(student_pk,fileref_fk,sg_fk,nationality_fk,id_number,crs_fk,title_fk,last_name,first_name,middle_name,previous_ln,dob,gender_fk,hl_fk,hl_oth_fk,ds_fk,seeing_rating_fk,hearing_rating_fk,communication_rating_fk,walking_rating_fk,remembering_rating_fk,selfcare_rating_fk,ses_fk,equity_fk,equity_oth_fk,email,sbs_email,examvenue_fk,digital_pref,id_on_record,sc_on_record,hq_on_record,inactive_debt,withhold_qual,student_cancelled,mysbs_active,captured_by,capture_date) values (?, ?, ?,?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

    }
    public String getContactEmail(int contactNumber)
            throws SQLException
    {
        Connection connection = null;
        PreparedStatement doSelect = null;
        try
        {
            String contactEmail = null;
            String selectStatement = "SELECT contact_email FROM store WHERE contact_id = ?";
            //connection = ConnectionPool.getConnection();
            doSelect = connection.prepareStatement(selectStatement);
            doSelect.setInt(1,contactNumber);
            ResultSet rs = doSelect.executeQuery();
            if ( rs.next() )
            {
                contactEmail = rs.getString(1);
                return contactEmail;
            }
            else
            {
                return null;
            }
        }
        catch (SQLException se)
        {
            // log the exception
            //log.error(se);
            // re-throw the exception
            throw se;
        }
        finally
        {
            try
            {
                doSelect.close();
               // ConnectionPool.freeConnection(connection);
            }
            catch (Exception e)
            {
            }
        }
    }
}

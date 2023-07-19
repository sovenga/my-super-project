package com.stadio.daos;



import com.stadio.mail.MailSender;
import com.stadio.models.*;
import com.stadio.services.JsonKeyChecker;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StadioStageDao {
    private String myDriver = "com.mysql.cj.jdbc.Driver";
    private String dbUrl = "jdbc:mysql://localhost:3306/sbs?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    //private String LiveDbUrl = "jdbc:mysql://129.232.162.234:3306/sbs?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private Statement statement;
    private Connection connection;
    Connection conn;

    public StadioStageDao() throws SQLException, ClassNotFoundException {
        Class.forName(myDriver);
        String url = dbUrl;
        String name = "mdynamicssbs";
        String password = "vdEI&zLxUCHw";
        connection = DriverManager.getConnection(url, name, password);
        statement = connection.createStatement();
    }
    public int saveStudentStage(String student_pk,String fileref_fk,String sg_fk,String nationality_fk,String id_number,String crs_fk,String title_fk,String last_name,String first_name,String middle_name,String previous_ln,String dob,String gender_fk,String hl_fk,String hl_oth_fk,String ds_fk,int seeing_rating_fk,int hearing_rating_fk,int communication_rating_fk,int walking_rating_fk,int remembering_rating_fk,int selfcare_rating_fk,String ses_fk,String equity_fk,String equity_oth_fk,String email,int examvenue_fk,int digital_pref,int id_on_record,int sc_on_record,int hq_on_record,String capture_date) throws SQLException {
        String sql = "INSERT INTO stage_student(student_pk,fileref_fk,sg_fk,nationality_fk,id_number,crs_fk,title_fk,last_name,first_name,middle_name,previous_ln,dob,gender_fk,hl_fk,hl_oth_fk,ds_fk,seeing_rating_fk,hearing_rating_fk,communication_rating_fk,walking_rating_fk,remembering_rating_fk,selfcare_rating_fk,ses_fk,equity_fk,equity_oth_fk,email,examvenue_fk,digital_pref,id_on_record,sc_on_record,hq_on_record,capture_date) " +
                "values ("+student_pk+",'"+fileref_fk+"','"+sg_fk+"','"+nationality_fk+"','"+id_number+"','"+crs_fk+"','"+title_fk+"','"+last_name+"','"+first_name+"','"+middle_name+"','"+previous_ln+"','"+dob+"','"+gender_fk+"','"+hl_fk+"','"+hl_oth_fk+"','"+ds_fk+"',"+seeing_rating_fk+","+hearing_rating_fk+","+communication_rating_fk+"," +
                ""+walking_rating_fk+","+remembering_rating_fk+","+selfcare_rating_fk+",'"+ses_fk+"','"+equity_fk+"','"+equity_oth_fk+"','"+email+"','"+examvenue_fk+"',"+digital_pref+",'"+id_on_record+"',"+sc_on_record+","+hq_on_record+",'"+capture_date+"')";
        return statement.executeUpdate(sql);
    }
    public List<MyServiceBusMessage> getServiceBusMessages() throws SQLException {
        List<MyServiceBusMessage> myServiceBusMessages = new ArrayList<>();
        String sql = "select file_name from consumed_message_log where status = 1";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            myServiceBusMessages.add(new MyServiceBusMessage(rs.getString(1)));
        }
        return myServiceBusMessages;
    }
    public List<Stadio_SRA> getSRAs() throws SQLException {
        List<Stadio_SRA> stadio_sras = new ArrayList<Stadio_SRA>();
        String sql = "select First_Name,Last_Name,Email_Address from sra_profiles where status = 1";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            stadio_sras.add(new Stadio_SRA(rs.getString(1),rs.getString(2),rs.getString(3)));
        }
        return stadio_sras;
    }
    public List<Student> getStudentExistInStageStudent(String ec_studentnumber) throws SQLException {
        String checkStudNoExistsSql = "select student_pk,fileref_fk,sg_fk,nationality_fk,id_number,crs_fk,title_fk,last_name,first_name,middle_name,previous_ln,dob,gender_fk,hl_fk,ds_fk,seeing_rating_fk,hearing_rating_fk,communication_rating_fk,walking_rating_fk,remembering_rating_fk,selfcare_rating_fk,ses_fk,equity_fk,equity_oth_fk,email,examvenue_fk,digital_pref,id_on_record,sc_on_record,hq_on_record,inactive_debt,withhold_qual,student_cancelled,mysbs_active,captured_by,capture_date " +
                "from stage_student where student_pk ='" + ec_studentnumber + "'";
        List<Student> students = new ArrayList<Student>();
        ResultSet resultSet = statement.executeQuery(checkStudNoExistsSql);
        if(resultSet.next()){
            students.add(new Student(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8), resultSet.getString(9),
                    resultSet.getString(10), resultSet.getString(11), resultSet.getString(12), resultSet.getString(13), resultSet.getString(14), resultSet.getString(15), resultSet.getString(16), resultSet.getString(17), resultSet.getString(18),
                    resultSet.getString(19), resultSet.getString(20), resultSet.getString(21), resultSet.getString(22), resultSet.getString(23), resultSet.getString(24), resultSet.getString(25), resultSet.getString(26), resultSet.getString(27),
                    resultSet.getString(28), resultSet.getString(29), resultSet.getString(30), resultSet.getString(31), resultSet.getString(32), resultSet.getString(33), resultSet.getString(34), resultSet.getString(35), resultSet.getString(36)));
        }
        return students;
    }
    public List<Address> getStudentAddress(String ec_studentnumber) throws SQLException {
        List<Address> address = new ArrayList<Address>();
        String sql = "select student_fk,type_of_address_fk,postcountry_fk,post_province_fk,address_1,address_2,address_3,address_4,postcode,capture_date from student_address where student_fk ='"+ec_studentnumber+"'";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            address.add( new Address(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),resultSet.getString(9),resultSet.getString(10)));
        }
        return address;
    }
    public List<Semester> getSemester(String startSemester,String semesterCycle) throws SQLException {
        ArrayList<Semester> semesters = new ArrayList<Semester>();
        String semSql = "select sem_pk,sem_start_date,sem_end_date from lk_sem where sem_pk = '" + startSemester + semesterCycle + "'";
        ResultSet semSQLSR = statement.executeQuery(semSql);
        if(semSQLSR.next()){
            semesters.add( new Semester(semSQLSR.getString(1),semSQLSR.getString(2),semSQLSR.getString(3)));
        }
        return semesters;
    }
    public String getQualPk(String qual_name) throws SQLException {
        String qualSql = "select qual_pk from lk_qual where crm_qual_id ='" + qual_name + "' and inactive = 0";
        ResultSet qual_rs = statement.executeQuery(qualSql);
        String qual_pk = "";
        if(qual_rs.next()){
            qual_pk = qual_rs.getString(1);
        }
        return qual_pk;
    }
    public String getQualName(String qual_pk) throws SQLException {
        String qualSql = "select qual_name from lk_qual where qual_pk ='" + qual_pk + "' and inactive = 0";
        ResultSet qual_rs = statement.executeQuery(qualSql);
        if(qual_rs.next()){
            qual_pk = qual_rs.getString(1);
        }
        return qual_pk;
    }
    public String getQualPkWithCode(String crm_qual_id) throws SQLException {
        String qualSql = "select qual_pk from lk_qual where crm_qual_id ='" + crm_qual_id + "' and inactive = 0";
        ResultSet qual_rs = statement.executeQuery(qualSql);
        String qual_pk = "";
        if(qual_rs.next()){
            qual_pk = qual_rs.getString(1);
        }
        return qual_pk;
    }
    public List<Contact> getStudentContact(String ec_studentnumber) throws SQLException {
        List<Contact> contacts = new ArrayList<Contact>();
        String sql = "select student_fk,contact_type_fk,contact_country_fk,contact_area_code,contact_number,capture_date from student_contact where student_fk ='"+ec_studentnumber+"'";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            contacts.add( new Contact(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),resultSet.getString(6)));
        }
        return contacts;
    }
    public void closeLocalConnections() throws SQLException {
        statement.close();
        connection.close();
    }
    public boolean stagingStudentExistsWithStudNo(String ec_studentnumber) throws SQLException {
        boolean found = false;
        String sql = "select * from student where student_pk = '" + ec_studentnumber + "'";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()){
            found = true;
        }
        return found;
    }
    public int insertContactDataOnLocal(long student_pk,String contact_type_fk,String contact_country_fk,String contact_area_code,String contact_number,String capture_date) throws SQLException {
        String sql = "insert into student_contact(student_contact_pk,student_fk,contact_type_fk,contact_country_fk,contact_area_code,contact_number,capture_date)  " +
                "values("+0+","+student_pk+",'"+contact_type_fk+"','"+contact_country_fk+"','"+contact_area_code+"','"+contact_number+"','"+capture_date+"')";
        return statement.executeUpdate(sql);
    }
    public int insertStudentAddressOnLocal(long student_pk,String type_of_address_fk,String postcountry,String post_province_fk,String address_1,String address_2,String address_3,String address_4,String postcode,String capture_date) throws SQLException{
        String sql = "insert into student_address(student_address_pk,student_fk,type_of_address_fk,postcountry_fk,post_province_fk,address_1,address_2,address_3,address_4,postcode,capture_date) " +
                "values("+0+","+student_pk+",'"+type_of_address_fk+"','"+postcountry+"','"+post_province_fk+"','"+address_1+"','"+address_2+"','"+address_3+"','"+address_4+"','"+postcode+"','"+capture_date+"')";
        return statement.executeUpdate(sql);
    }
    public boolean chekIfStudentExistsInStage_Student_table_use_STUDNO(String ec_studentnumber) throws SQLException {
        boolean found = false;
        String sql = "select * from stage_student where student_pk = '" + ec_studentnumber + "'";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()){
            found = true;
        }
        return found;
    }
    public boolean chekIfStudentExistsInStage_Student_table_use_STUDID(String id_number) throws SQLException {
        boolean found = false;
        String sql = "select * from stage_student where id_number = '" + id_number + "'";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()){
            found = true;
        }
        return found;
    }
    public boolean stagingStudentExistsWithIDNo(String id_number) throws SQLException {
        boolean found = false;
        String sql = "select * from student where id_number = '" + id_number + "'";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()){
            found = true;
        }
        return found;
    }
    public boolean stagingStudentExistsWithStudNo_stud_stage(String ec_studentnumber,String id_number) throws SQLException {
        boolean found = false;
        String sql = "select * from stage_student where student_pk = '" + ec_studentnumber + "' or id_number ='"+id_number+"'";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()){
            found = true;
        }
        return found;
    }
    public boolean getOldStudentNumberBoolean(String id_number) throws SQLException {
        String ol_stud_num = "";
        String sql = "select old_student_number from student where id_number = '"+id_number+"' and old_student_number IS NOT NULL";
        ResultSet rs = statement.executeQuery(sql);
        boolean found = false;
        if(rs.next()){
            found = true;
        }
        return found;
    }
    public boolean messageExists(String file_name) throws SQLException {
        boolean found = false;
        String sql = "select * from consumed_message_log where file_name ='"+file_name+"'";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()){
            found = true;
        }
        return found;
    }
    public int saveServiceBusMessage(String fileName) throws SQLException {
        String sql = "insert into consumed_message_log(file_name) values('"+fileName+"')";
        return statement.executeUpdate(sql);
    }
    public String getSRA_GRP(String qual_code) throws SQLException {
        String email = "";
        String sql = "select group_email from sra_quals where crm_qual_id ='"+qual_code+"' and status =1";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()){
            email = rs.getString(1);
        }
        return email;
    }
    public String getLocalIDnumber(String ec_stud_no) throws SQLException {
        String sql = "select id_number from stage_student where student_pk = '"+ec_stud_no+"' and id_number is not null";
        ResultSet rs = statement.executeQuery(sql);
        String res = "";
        if(rs.next()){
            res = rs.getString(1);
        }
        return res;
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
    public boolean liveStudentExists(String studentNumber) throws SQLException {
        boolean found = false;
        String sql = "select * from student where student_pk = '"+studentNumber+"'";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()){
            found = true;
        }
        return found;
    }
    public boolean liveStudentExistsFromStudent_Stage(String studentNumber) throws SQLException {
        boolean found = false;
        String sql = "select * from student where student_pk = '"+studentNumber+"'";
        ResultSet rs = statement.executeQuery(sql);
        if(rs.next()){
            found = true;
        }
        return found;
    }
    public String getTitle(String salutation) throws SQLException {
        String title_fk = "";
        String check_title = "select title_pk from lk_title where title_abbr ='" + salutation + "'";
        ResultSet chk_title_rs = statement.executeQuery(check_title);
        if (chk_title_rs.next()) {
            title_fk = chk_title_rs.getString(1);
            System.out.println("Title is: " + title_fk);
        }
        return title_fk;
    }
    public Long getPOstalCode(String address1_city) throws SQLException {
        Long postal_code = null;
        String strPostSql = "SELECT postcode_pk FROM lk_postcode where AREA = '" + address1_city + "' LIMIT 1";
        ResultSet postcode_rs = statement.executeQuery(strPostSql);
        if (postcode_rs.next()) {
            postal_code = postcode_rs.getLong(1);
            System.out.println("Postal Code is: " + postal_code);
        }
        return postal_code;
    }
    public String getItEmailAddress() throws SQLException {
        String sql = "select email_address from sra_profiles where first_name = 'IT GROUP'";
        ResultSet rs = statement.executeQuery(sql);
        String itEmail = "";
        if(rs.next()){
            itEmail = rs.getString(1);
        }
        return itEmail;
    }
    public String getSRAEmail(String SRA_NAME) throws SQLException {
        String sql = "select email_address from sra_profiles where first_name = '"+SRA_NAME+"'";
        String sraName = "";
        ResultSet sraSql = statement.executeQuery(sql);
        if(sraSql.next()){
            sraName = sraSql.getString(1);
            //System.out.println("SRA EMAIL IS "+sraName);
        }
        return sraName;
    }
    public String getGroupSRAEmail() throws SQLException {
        String sql = "select email_address from sra_profiles where isGroup = 1 and status = 1";
        String sraGroupEmail = "";
        ResultSet sraSql = statement.executeQuery(sql);
        if(sraSql.next()){
            sraGroupEmail = sraSql.getString(1);
        }
        return sraGroupEmail;
    }
    public String getMajorStream(String stream_name) throws SQLException {
        String streamFk = "---";
        String sql = "select stream_pk from lk_stream where stream_name = '"+stream_name+"'";
        ResultSet streamSql = statement.executeQuery(sql);
        if(streamSql.next()){
            streamFk = streamSql.getString(1);
        }
        return streamFk;
    }
    public boolean liveStudentExistsWithID(String idNumber) throws SQLException {
        boolean found = false;
        String sql = "select * from student where id_number ='"+idNumber+"'";
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

        Connection con = DriverManager.getConnection(url, name, password);//0116719500
        return con;
    }
    public String getCountryCode(String countryName) throws SQLException {
        String sql = "SELECT country_pk FROM sbs.lk_country where country_name = '"+countryName+"';";
        ResultSet rs = statement.executeQuery(sql);
        String res = "";
        if(rs.next()){
            res = rs.getString(1);
        }
        return res;
    }
    public void processContactData(String ec_studentnumber, String GLOBAL_ID_NUMBER, JSONObject studRecord, JsonKeyChecker jsonKeyChecker, FileWriter service_bus_audit_log, String formattedDateTime, MailSender mailSender,FileWriter service_bus_error_log,boolean DUP_STUD_ID_FOUND,StringBuffer SRA_CRM_MESSAGE,String strCaptrureDate) throws IOException, SQLException {
        FileWriter service_bus_txt_file = new FileWriter("C:\\sbs\\service-bus\\" + ec_studentnumber + " contact data.txt");
        //Define variables
        String fileref_fk = "CST";
        String sg_fk = "";
        String nationallity = "";
        String idNumber = "";
        String crs_fk = "";
        String title_fk = "";
        String salutation = (String) studRecord.get("salutation");
        String firstname = (String) studRecord.get("firstname");
        String lastname = (String) studRecord.get("lastname");


        String middlename = "";
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "middlename")) {
            if (!middlename.equalsIgnoreCase("")) {
                middlename = (String) studRecord.get("middlename");
            }
        }
        String ec_maidenname = "";
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_maidenname")) {
            if (!ec_maidenname.equalsIgnoreCase("")) {
                ec_maidenname = (String) studRecord.get("ec_maidenname");
            }
        }

        if(!firstname.equalsIgnoreCase("") && !lastname.equalsIgnoreCase("")){
            String firstFirstNameLetStr = firstname.substring(0, 1);
            String remFirstNameStr = firstname.substring(1);
            String firstLetirstNameToUpper = firstFirstNameLetStr.toUpperCase();
            String initCapFirstname = firstLetirstNameToUpper+remFirstNameStr;
            String firstLastNameNameLetStr = lastname.substring(0, 1);
            String remLastNameStr = lastname.substring(1);
            String firstLetLastNameToUpper = firstLastNameNameLetStr.toUpperCase();
            String initCaplastname = firstLetLastNameToUpper+remLastNameStr;
            System.out.println("First Name: "+initCapFirstname + " Last Name: "+initCaplastname);

            firstname  = initCapFirstname;
            lastname = initCaplastname;
        }else {
            System.out.println("FIRST NAME AND LAST NAME ARE EMPTY............................");
        }

        if(firstname.contains(" ")){
            String initFirstname = firstname;
            firstname = firstname.substring(0,firstname.indexOf(" "));
            middlename = initFirstname.substring(initFirstname.indexOf(" ")+1);
            System.out.println("First Name v1: "+firstname +" middlename: "+middlename);
        }else {
            System.out.println("NO SPACE FOUND");
        }
        String birthdate = (String) studRecord.get("birthdate");

        int gendercode_;
        String gendercode = "";
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "gendercode")) {
            gendercode_ = Integer.parseInt(studRecord.get("gendercode").toString());

            if (gendercode_ == 1) {
                gendercode = "M";
            } else if (gendercode_ == 2) {
                gendercode = "F";
            } else {
                gendercode = "MF";
            }
        }
        String ec_postaladdressisthesameasresidential = "";
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_postaladdressisthesameasresidential@OData.Community.Display.V1.FormattedValue")) {
            ec_postaladdressisthesameasresidential = (String) studRecord.get("ec_postaladdressisthesameasresidential@OData.Community.Display.V1.FormattedValue");
        }
        String address1_city = "";
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address1_city")) {
            address1_city = (String) studRecord.get("address1_city");
        }
        String addres1Type = "";
        boolean addressType1 = false;

        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address1_addresstypecode")) {
            int address1TypeCode = Integer.parseInt(studRecord.get("address1_addresstypecode").toString());
            addressType1 = true;
            if (address1TypeCode == 1) {
                addres1Type = "P";
            } else if (address1TypeCode == 3 || address1TypeCode == 2) {
                addres1Type = "H";
            } else {
                addres1Type = "W";
            }
        }
        String addres2Type = "";
        boolean addressType2 = false;
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address2_addresstypecode")) {
            if (addressType1 == false) {
                int address2TypeCode = Integer.parseInt(studRecord.get("address2_addresstypecode").toString());
                addressType2 = true;
                if (address2TypeCode == 1) {
                    addres2Type = "P";
                } else if (address2TypeCode == 3 || address2TypeCode == 2) {
                    addres2Type = "H";
                } else {
                    addres2Type = "W";
                }
            }
        }
        String passport = "";
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_passportnumber")) {
            passport = (String) studRecord.get("ec_passportnumber");
            if (!passport.equalsIgnoreCase("")) {
                idNumber = passport;
                GLOBAL_ID_NUMBER = passport;
                System.out.println("Passport Number is " + passport);
            }
            System.out.println("Passport exists");
        }
        String saidnumber = "";
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_saidnumber")) {
            saidnumber = (String) studRecord.get("ec_saidnumber");
            if (!saidnumber.equalsIgnoreCase("")) {
                idNumber = saidnumber;
                GLOBAL_ID_NUMBER = saidnumber;
                System.out.println("Id Number is " + idNumber);
            }
        }
        String ec_namibianidnumber = "";
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_namibianidnumber")) {
            ec_namibianidnumber = (String) studRecord.get("ec_namibianidnumber");
            if (!ec_namibianidnumber.equalsIgnoreCase("")) {
                idNumber = ec_namibianidnumber;
                GLOBAL_ID_NUMBER = ec_namibianidnumber;
                System.out.println("Nam ID Number is " + ec_namibianidnumber);
            }
        }
        String ec_seeing = "";
        int seeing = 6;
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_seeing@OData.Community.Display.V1.FormattedValue")) {
            ec_seeing = (String) studRecord.get("ec_seeing@OData.Community.Display.V1.FormattedValue");
            if (ec_seeing.equalsIgnoreCase("No")) {
                seeing = 1;
            } else {
                seeing = 2;
            }
        }

        String ec_intellectuallearning = "";
        int intellectuallearning = 6;
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_intellectuallearning@OData.Community.Display.V1.FormattedValue")) {
            ec_intellectuallearning = (String) studRecord.get("ec_intellectuallearning@OData.Community.Display.V1.FormattedValue");
            if (ec_intellectuallearning.equalsIgnoreCase("No")) {
                intellectuallearning = 1;
            } else {
                intellectuallearning = 2;
            }
        }

        String ec_physical = "";
        int physical = 6;
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_physical@OData.Community.Display.V1.FormattedValue")) {
            ec_physical = (String) studRecord.get("ec_physical@OData.Community.Display.V1.FormattedValue");
            if (ec_physical.equalsIgnoreCase("No")) {
                physical = 1;
            } else {
                physical = 2;
            }
        }

        String socioeconomicstatus = "NWUns";
        long ec_socioeconomicstatus;
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_socioeconomicstatus")) {
            ec_socioeconomicstatus = (long) studRecord.get("ec_socioeconomicstatus");
            if (ec_socioeconomicstatus == 948180001) {
                socioeconomicstatus = "EMPLD";
            } else if (ec_socioeconomicstatus == 948180004) {
                socioeconomicstatus = "NWHom";
            } else if (ec_socioeconomicstatus == 948180005) {
                socioeconomicstatus = "NWStu";
            } else if (ec_socioeconomicstatus == 948180006) {
                socioeconomicstatus = "NWPen";
            } else if (ec_socioeconomicstatus == 948180007) {
                socioeconomicstatus = "NWDis";
            } else {
                socioeconomicstatus = "NWUns";
            }
        }
        System.out.println("ec_socioeconomicstatus: " + socioeconomicstatus);
        String ec_communicationspeech = "";
        int communicationSpeech = 6;
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_communicationspeech@OData.Community.Display.V1.FormattedValue")) {
            ec_communicationspeech = (String) studRecord.get("ec_communicationspeech@OData.Community.Display.V1.FormattedValue");
            if (ec_communicationspeech.equalsIgnoreCase("No")) {
                communicationSpeech = 1;
            } else {
                communicationSpeech = 2;
            }
        }

        String ec_hearing = "";
        int hearing = 6;
        int count_all_disabilites = 0;
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_hearing@OData.Community.Display.V1.FormattedValue")) {
            ec_hearing = (String) studRecord.get("ec_hearing@OData.Community.Display.V1.FormattedValue");
            if (ec_hearing.equalsIgnoreCase("No")) {
                hearing = 1;
            } else {
                hearing = 2;
            }
        }

        long ec_homelanguage_;
        String ec_homelanguage = "";
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_homelanguage")) {
            ec_homelanguage_ = (long) studRecord.get("ec_homelanguage");
            if (ec_homelanguage_ == 948180001) {
                ec_homelanguage = "Xho";
            } else if (ec_homelanguage_ == 948180002) {
                ec_homelanguage = "Afr";
            } else if (ec_homelanguage_ == 948180003) {
                ec_homelanguage = "Eng";
            } else if (ec_homelanguage_ == 948180005) {
                ec_homelanguage = "Set";
            } else if (ec_homelanguage_ == 948180009) {
                ec_homelanguage = "Tsh";
            } else if (ec_homelanguage_ == 948180010) {
                ec_homelanguage = "Nde";
            } else if (ec_homelanguage_ == 948180012) {
                ec_homelanguage = "Oth";
            } else if (ec_homelanguage_ == 948180000) {
                ec_homelanguage = "Zul";
            } else if (ec_homelanguage_ == 948180004) {
                ec_homelanguage = "Sep";
            } else if (ec_homelanguage_ == 948180006) {
                ec_homelanguage = "Set";
            } else if (ec_homelanguage_ == 948180007) {
                ec_homelanguage = "Xho";
            } else if (ec_homelanguage_ == 948180008) {
                ec_homelanguage = "Swa";
            } else if (ec_homelanguage_ == 948180011) {
                ec_homelanguage = "SAS";
            }
        }

        System.out.println(ec_homelanguage);
        String ec_homelanguageother = (String) studRecord.get("ec_homelanguageother");
        String ec_disabilityspecialneeds = "";
        String disabilityspecialneeds = "";

        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_disabilityspecialneeds@OData.Community.Display.V1.FormattedValue")) {
            ec_disabilityspecialneeds = (String) studRecord.get("ec_disabilityspecialneeds@OData.Community.Display.V1.FormattedValue");
            if (ec_disabilityspecialneeds.equalsIgnoreCase("Yes")) {
                if (seeing == 2) {
                    disabilityspecialneeds = "Sgt";
                    count_all_disabilites = 1;
                }
                if (hearing == 2) {
                    disabilityspecialneeds = "Hea";
                    count_all_disabilites += 1;
                }
                if (intellectuallearning == 2) {
                    disabilityspecialneeds = "Int";
                    count_all_disabilites += 1;
                }
                if (communicationSpeech == 2) {
                    disabilityspecialneeds = "Com";
                    count_all_disabilites += 1;
                }
                if (physical == 2) {
                    disabilityspecialneeds = "Phy";
                    count_all_disabilites += 1;
                }
                System.out.println("Number of disabilites " + count_all_disabilites +" - "+disabilityspecialneeds);
            } else {
                disabilityspecialneeds = "Non";
            }
        }
        if (count_all_disabilites > 1) {
            disabilityspecialneeds = "Mul";
        }
        System.out.println("disabilityspecialneeds : " + disabilityspecialneeds);
        long ec_race;
        String race = "";
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_race")) {
            ec_race = (Long) studRecord.get("ec_race");
            if (ec_race == 948180001) {
                race = "BA";
            } else if (ec_race == 948180002) {
                race = "BC";
            } else if (ec_race == 948180000) {
                race = "Wh";
            } else if (ec_race == 948180005) {
                race = "X1";
            } else if (ec_race == 948180004) {//
                race = "BI";
            }else {
                race = "Ot";
            }
        }
        String emailaddress1 = "";
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "emailaddress1")) {
            emailaddress1 = (String) studRecord.get("emailaddress1");
        }
        int examvenue_fk = 0;
        int digital_pref = 0;
        int id_on_record = 1;
        int sc_on_record = 1;
        int hq_on_record = 1;
        int inactive_debt = 0;
        int withhold_qual = 0;
        int student_cancelled = 0;
        int mysbs_active = 0;
        int captured_by = 000000000;
        int walking_rating_fk = 1;
        int remembering_rating_fk = 1;
        int selfcare_rating_fk = 1;
        String equity_oth_fk = "";
        String sbs_email = "";
        int last_editor = 100;

        String postcountry = "";
        String telephone1 = (String) studRecord.get("telephone1");
        String telephone2 = (String) studRecord.get("telephone2");
        String ec_province = "";
        long ec_province_;
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_province")) {
            ec_province_ = (long) studRecord.get("ec_province");
            if (ec_province_ == 948180000) {
                ec_province = "EC";
            } else if (ec_province_ == 948180004) {
                ec_province = "Li";
            } else if (ec_province_ == 948180006) {
                ec_province = "NC";
            } else if (ec_province_ == 948180001) {
                ec_province = "FS";
            } else if (ec_province_ == 948180002) {
                ec_province = "Ga";
            } else if (ec_province_ == 948180003) {//948180003
                ec_province = "KN";
            } else if (ec_province_ == 948180007) {
                ec_province = "NW";
            } else if (ec_province_ == 948180008) {
                ec_province = "WC";
            } else if (ec_province_ == 948180005) {
                ec_province = "Mp";
            } else if (ec_province_ == 948180012 || ec_province_ == 948180015 || ec_province_ == 948180017 || ec_province_ == 948180018 || ec_province_ == 948180021 || ec_province_ == 948180022 || ec_province_ == 948180023 || ec_province_ == 948180011 || ec_province_ == 948180013 || ec_province_ == 948180019 || ec_province_ == 948180010 || ec_province_ == 948180024) {
                ec_province = "Ou";
            }else {
                ec_province = "Ou";
            }
        }else{
            ec_province = "Ou";
        }
        String place_of_birth = "";
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "_ec_placeofbirth_value@OData.Community.Display.V1.FormattedValue")) {
            place_of_birth = (String) studRecord.get("_ec_placeofbirth_value@OData.Community.Display.V1.FormattedValue");
            if(place_of_birth.equalsIgnoreCase("")){
                place_of_birth = "AI";
            }
        }
        long ec_nationality_;
        String ec_nationality = "";

        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_nationality")) {
            ec_nationality_ = (long) studRecord.get("ec_nationality");
            if (ec_nationality_ == 948180000) {
                ec_nationality = "ZA";
                postcountry = "ZA";
                sg_fk = "SBS001";
                examvenue_fk = 138;
                System.out.println("Nationality: " + ec_nationality);
                System.out.println("SG_FK: " + sg_fk);
            } else if (ec_nationality_ == 948180002) {
                ec_nationality = "NA";
                postcountry = "NA";
                sg_fk = "SBN001";
                examvenue_fk = 138;
                System.out.println("Nationality: " + ec_nationality);
                System.out.println("SG_FK: " + sg_fk);
            } else {
                postcountry = getCountryCode(place_of_birth);
                examvenue_fk = 138;
                ec_nationality = getCountryCode(place_of_birth);
                sg_fk = "SBS001";
            }
        }

        Long postal_code = getPOstalCode(address1_city);
        long ec_crs_citizen;
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_citizenresidentstatus")) {
            ec_crs_citizen = (long) studRecord.get("ec_citizenresidentstatus");
            if (ec_crs_citizen == 948180000) {
                crs_fk = "PR";
            } else if (ec_crs_citizen == 948180002) {
                crs_fk = "SA";
            } else {
                crs_fk = "Ot";
            }
        }
        String DEFAULT_SRA = "STADIO Student Advisor";
        String strSRA_Name = "";
        boolean CAN_SEND_TO_SRA = false;
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "_ownerid_value@OData.Community.Display.V1.FormattedValue")) {
            strSRA_Name = (String) studRecord.get("_ownerid_value@OData.Community.Display.V1.FormattedValue");
            String fName = strSRA_Name.substring(0, strSRA_Name.indexOf(' '));
            if (!strSRA_Name.equalsIgnoreCase(DEFAULT_SRA)) {
                System.out.println("SRA First name: " + fName);
            }
        }
        String default_email = ec_studentnumber+"@stadiodl.ac.za";
        title_fk = getTitle(salutation);

        if (!stagingStudentExistsWithStudNo(ec_studentnumber)) {
            if (!stagingStudentExistsWithIDNo(idNumber)) {
                if (!chekIfStudentExistsInStage_Student_table_use_STUDNO(ec_studentnumber)) {
                    if (!chekIfStudentExistsInStage_Student_table_use_STUDID(idNumber)) {
                        if (!idNumber.equalsIgnoreCase("")) {
                            String hl_oth_fk = "";
                            saveStudentStage(ec_studentnumber, fileref_fk, sg_fk, ec_nationality, idNumber, crs_fk, title_fk, lastname, firstname, middlename, ec_maidenname, birthdate, gendercode, ec_homelanguage, hl_oth_fk, disabilityspecialneeds,
                                    seeing, hearing, communicationSpeech, walking_rating_fk, remembering_rating_fk, selfcare_rating_fk, socioeconomicstatus, race, equity_oth_fk, emailaddress1, examvenue_fk, digital_pref, id_on_record, sc_on_record, hq_on_record, strCaptrureDate);
                            service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Contact Record inserted");

                            System.out.println("Student Records inserted.....");
                            service_bus_txt_file.write("{ " + "\nStudNumber:" + ec_studentnumber + " \nfileref_fk:" + fileref_fk + "\nsg_fk: " + sg_fk +
                                    "\nnationality_fk:" + ec_nationality + "\nid_number:" + idNumber + "\ncrs_fk:" + crs_fk + "\ntitle_fk:" + title_fk + "\nlast_name:" + lastname + "\nfirst_name:" + firstname + "\nmiddle_name:" + middlename +
                                    "\nprevious_ln:" + ec_maidenname + "\ndob:" + birthdate + "\ngender_fk:" + gendercode + "\nhl_fk:" + ec_homelanguage + "\nds_fk:" + disabilityspecialneeds +
                                    "\nseeing_rating_fk:" + seeing + "\nhearing_rating_fk:" + hearing + "\ncommunication_rating_fk:" + communicationSpeech + "\nses_fk:" + socioeconomicstatus + "\nequity_fk:" + race + "\nemail:" + emailaddress1 +
                                    "\nexamvenue_fk:" + examvenue_fk + "\ndigital_pref:" + digital_pref + "\nid_on_record:" + id_on_record + "\nsc_on_record:" + sc_on_record + "\nhq_on_record:" + hq_on_record + "inactive_debt:" + "0" + "\nwithhold_qual:" + "0" + "\nstudent_cancelled:" + "0" +
                                    "\nmysbs_active:" + mysbs_active + "\nec_province:" + ec_province + "\n}");
                            service_bus_txt_file.close();

                            String address1_line1 = "";
                            String address1_line2 = "";
                            String address1_line3 = "";
                            String address1_line4 = "";
                            String address1_postalcode = "";

                            String address2_line1 = "";
                            String address2_line2 = "";
                            String address2_line3 = "";
                            String address2_line4 = "";
                            String address2_postalcode = "";

                            if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address1_postalcode")) {
                                address1_postalcode = (String) studRecord.get("address1_postalcode");
                            }
                            if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address2_postalcode")) {
                                address2_postalcode = (String) studRecord.get("address2_postalcode");
                            }
                            String finalAddress1_line1 = "";
                            if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address1_line1")) {
                                address1_line1 = (String) studRecord.get("address1_line1");
                                String spaceRem1 = address1_line1.replace("'", " ");
                                if (spaceRem1.length() > 55) {
                                    finalAddress1_line1 = spaceRem1.substring(0, 54);
                                } else {
                                    finalAddress1_line1 = address1_line1;
                                }
                            }
                            String finalAddress1_line2 = "";
                            if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address1_line2")) {
                                address1_line2 = (String) studRecord.get("address1_line2");
                                String spaceRem2 = address1_line2.replace("'", " ");
                                if (spaceRem2.length() > 50) {
                                    finalAddress1_line2 = spaceRem2.substring(0, 49);
                                } else {
                                    finalAddress1_line2 = address1_line2;
                                }
                            }
                            String finalAddress1_line3 = "";
                            if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address1_line3")) {
                                address1_line3 = (String) studRecord.get("address1_line3");
                                String spaceRem3 = address1_line3.replace("'", " ");
                                if (spaceRem3.length() > 50) {
                                    finalAddress1_line3 = spaceRem3.substring(0, 49);
                                } else {
                                    finalAddress1_line3 = address1_line3;
                                }
                            }
                            String finalAddress1_line4 = "";
                            if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address1_city")) {
                                address1_line4 = (String) studRecord.get("address1_city");
                                String spaceRem4 = address1_line4.replace("'", " ");
                                if (spaceRem4.length() > 50) {
                                    finalAddress1_line4 = spaceRem4.substring(0, 49);
                                } else {
                                    finalAddress1_line4 = address1_line4;
                                }
                            }
                            String addressType = "";
                            if (addressType1 == true) {
                                addressType = addres1Type;
                            }
                            if (addressType1 == false) {
                                addressType = addres2Type;
                            }
                            String spaceRemoved = address1_postalcode.replace(" ", "");
                            int intPostLength = spaceRemoved.length();
                            String finalPost = spaceRemoved;
                            if (intPostLength > 9) {
                                finalPost = spaceRemoved.substring(0, 9);
                            }

                            String spaceRemoved1 = address2_postalcode.replace(" ", "");
                            int intPostLength1 = spaceRemoved1.length();
                            String finalPost1 = spaceRemoved1;
                            if (intPostLength1 > 9) {
                                finalPost1 = spaceRemoved1.substring(0, 9);
                            }
                            boolean postalAddressExists = false;
                            if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address2_city") || jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address2_line1") || jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address4_line1") || jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address2_line1") || jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address2_line1") ) {
                                postalAddressExists = true;
                            }
                            if(ec_postaladdressisthesameasresidential.equalsIgnoreCase("Yes")){
                                insertStudentAddressOnLocal(Long.parseLong(ec_studentnumber), "H", postcountry, ec_province, finalAddress1_line1.toUpperCase(), finalAddress1_line2.toUpperCase(), finalAddress1_line3.toUpperCase(), finalAddress1_line4.toUpperCase(), finalPost, strCaptrureDate);
                                insertStudentAddressOnLocal(Long.parseLong(ec_studentnumber), "P", postcountry, ec_province, finalAddress1_line1.toUpperCase(), finalAddress1_line2.toUpperCase(), finalAddress1_line3.toUpperCase(), finalAddress1_line4.toUpperCase(), finalPost, strCaptrureDate);
                                service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - POSTAL Address is the same as Residential - Data inseted on Local DB");
                                System.out.println("Student Same Addresses Records inserted.....");
                            }else {
                                insertStudentAddressOnLocal(Long.parseLong(ec_studentnumber), "H", postcountry, ec_province, finalAddress1_line1.toUpperCase(), finalAddress1_line2.toUpperCase(), finalAddress1_line3.toUpperCase(), finalAddress1_line4.toUpperCase(), finalPost, strCaptrureDate);
                                System.out.println("Student Addresses Records inserted.....");
                                service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Residential - Data inseted on Local DB");
                                if(postalAddressExists){
                                    if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address2_postalcode")) {
                                        address2_postalcode = (String) studRecord.get("address2_postalcode");
                                    }
                                    String finalAddress2_line1 = "";
                                    if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address2_line1")) {
                                        address2_line1 = (String) studRecord.get("address2_line1");
                                        String spaceRem1 = address2_line1.replace("'", " ");
                                        if (spaceRem1.length() > 55) {
                                            finalAddress2_line1 = spaceRem1.substring(0, 54);
                                        } else {
                                            finalAddress2_line1 = address2_line1;
                                        }
                                    }
                                    String finalAddress2_line2 = "";
                                    if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address2_line2")) {
                                        address2_line2 = (String) studRecord.get("address2_line2");
                                        String spaceRem2 = address2_line2.replace("'", " ");
                                        if (spaceRem2.length() > 50) {
                                            finalAddress2_line2 = spaceRem2.substring(0, 49);
                                        } else {
                                            finalAddress2_line2 = address2_line2;
                                        }
                                    }
                                    String finalAddress2_line3 = "";
                                    if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address2_line3")) {
                                        address2_line3 = (String) studRecord.get("address2_line3");
                                        String spaceRem3 = address2_line3.replace("'", " ");
                                        if (spaceRem3.length() > 50) {
                                            finalAddress2_line3 = spaceRem3.substring(0, 49);
                                        } else {
                                            finalAddress2_line3 = address2_line3;
                                        }
                                    }
                                    String finalAddress2_line4 = "";
                                    if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address2_city")) {
                                        address2_line4 = (String) studRecord.get("address2_city");
                                        String spaceRem4 = address2_line4.replace("'", " ");
                                        if (spaceRem4.length() > 50) {
                                            finalAddress2_line4 = spaceRem4.substring(0, 49);
                                        } else {
                                            finalAddress2_line4 = address2_line4;
                                        }
                                    }
                                    insertStudentAddressOnLocal(Long.parseLong(ec_studentnumber), "P", postcountry, ec_province, finalAddress2_line1.toUpperCase(), finalAddress2_line2.toUpperCase(),
                                            finalAddress2_line3.toUpperCase(), finalAddress2_line4.toUpperCase(), finalPost1, strCaptrureDate);
                                    service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Postal Address - Data inseted on Local DB");
                                }
                            }

                            String mobilephone = "";
                            String contact_type = "";
                            String contactAreaCodePart = "";
                            String remContactNumber = "";
                            if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "mobilephone")) {
                                contact_type = "Cel";
                                mobilephone = (String) studRecord.get("mobilephone");
                                remContactNumber = mobilephone.substring(3);
                                contactAreaCodePart = mobilephone.substring(0, 3);
                                insertContactDataOnLocal(Long.parseLong(ec_studentnumber), contact_type, ec_nationality, contactAreaCodePart, remContactNumber, strCaptrureDate);
                                service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Mobile Contact Record inserted");
                                System.out.println("Student Mobile Contact Records inserted.....");
                            }
                            String home_tel = "";
                            String remTel1ContactNumber = "";
                            String contactTel1AreaCodePart = "";
                            if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "home2")) {
                                contact_type = "Hom";
                                home_tel = (String) studRecord.get("home2");
                                contactTel1AreaCodePart = home_tel.substring(0, 3);
                                remTel1ContactNumber = home_tel.substring(3);
                                insertContactDataOnLocal(Long.parseLong(ec_studentnumber), contact_type, ec_nationality, contactTel1AreaCodePart, remTel1ContactNumber, strCaptrureDate);
                                service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Home Contact Record inserted");
                                System.out.println("Student Home Contact Records inserted.....");
                            }
                            String work_tel = "";
                            String contactTel2AreaCodePart = "";
                            String remTel2ContactNumber;
                            if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "telephone1")) {
                                work_tel = (String) studRecord.get("telephone1");
                                contact_type = "Wrk";
                                contactTel2AreaCodePart = work_tel.substring(0, 3);
                                remTel2ContactNumber = work_tel.substring(3);
                                insertContactDataOnLocal(Long.parseLong(ec_studentnumber), contact_type, ec_nationality, contactTel2AreaCodePart, remTel2ContactNumber, strCaptrureDate);
                                service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Work Contact Record inserted");
                                System.out.println("Student Work Contact Records inserted.....");
                            }

                        } else {
                            System.out.println("ID Number is null - Student number " + ec_studentnumber);
                            mailSender.sendMail("Null ID NUmber - Student number " + ec_studentnumber);
                            service_bus_error_log.append("\n" + formattedDateTime + ": ID Number is null - Student number " + ec_studentnumber);
                        }
                    } else {
                        System.out.println("Duplicate Student ID/Passport number found in the student stage - no action required. :" + idNumber);
                        service_bus_error_log.append("\n" + formattedDateTime + ": " + " Duplicate Student ID/Passport number found in the student stage - no action required:" + idNumber + " found in LIVE SBS Database");
                        DUP_STUD_ID_FOUND = true;
                        SRA_CRM_MESSAGE.append("\n - Duplicate Student ID/Passport number found in the student stage - no action required");
                    }
                } else {
                    System.out.println("CRM Generated student number already exists in student stage - REMOTE CHECKED");
                    service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " DUPLICATE Entry student stage check");
                    System.out.println(idNumber + ": CRM Generated student ID number already belongs to another student - REMOTE CHECKED");
                    service_bus_error_log.append("\n" + formattedDateTime + ": " + idNumber + " DUPLICATE Entry Student Stage");
                }
            } else {
                /*String ec_maidenname = "";
                String middlename = "";*/
                String OLD_STUDENT_NUM = getOldStudentNumber(GLOBAL_ID_NUMBER);

                if (!OLD_STUDENT_NUM.isEmpty()) {
                    if (!chekIfStudentExistsInStage_Student_table_use_STUDID(idNumber)) {
                        String hl_oth_fk = "";
                       saveStudentStage(ec_studentnumber, fileref_fk, sg_fk, ec_nationality, idNumber, crs_fk, title_fk, lastname, firstname, middlename, ec_maidenname, birthdate, gendercode, ec_homelanguage, hl_oth_fk, disabilityspecialneeds,
                                seeing, hearing, communicationSpeech, walking_rating_fk, remembering_rating_fk, selfcare_rating_fk, socioeconomicstatus, race, equity_oth_fk, emailaddress1, examvenue_fk, digital_pref, id_on_record, sc_on_record, hq_on_record, strCaptrureDate);
                        service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Contact Record inserted for existing ID number");
                        System.out.println("OLD_STUDENT_NUM = " + OLD_STUDENT_NUM);
                        System.out.println("6/7 Digit student number identified.");

                        System.out.println("Student Records inserted for existing ID number.....");
                        service_bus_txt_file.write("{ " + "\nStudNumber:" + ec_studentnumber + " \nfileref_fk:" + fileref_fk + "\nsg_fk: " + sg_fk +
                                "\nnationality_fk:" + ec_nationality + "\nid_number:" + idNumber + "\ncrs_fk:" + crs_fk + "\ntitle_fk:" + title_fk + "\nlast_name:" + lastname + "\nfirst_name:" + firstname + "\nmiddle_name:" + middlename +
                                "\nprevious_ln:" + ec_maidenname + "\ndob:" + birthdate + "\ngender_fk:" + gendercode + "\nhl_fk:" + ec_homelanguage + "\nds_fk:" + disabilityspecialneeds +
                                "\nseeing_rating_fk:" + seeing + "\nhearing_rating_fk:" + hearing + "\ncommunication_rating_fk:" + communicationSpeech + "\nses_fk:" + socioeconomicstatus + "\nequity_fk:" + race + "\nemail:" + emailaddress1 +
                                "\nexamvenue_fk:" + examvenue_fk + "\ndigital_pref:" + digital_pref + "\nid_on_record:" + id_on_record + "\nsc_on_record:" + sc_on_record + "\nhq_on_record:" + hq_on_record + "inactive_debt:" + "0" + "\nwithhold_qual:" + "0" + "\nstudent_cancelled:" + "0" +
                                "\nmysbs_active:" + mysbs_active + "\nec_province:" + ec_province + "\n}");
                        service_bus_txt_file.close();

                        String address1_line1 = "";
                        String address1_line2 = "";
                        String address1_line3 = "";
                        String address1_line4 = "";
                        String address1_postalcode = "";

                        String finalAddress1_line1 = "";
                        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address1_line1")) {
                            address1_line1 = (String) studRecord.get("address1_line1");
                            String spaceRem1 = address1_line1.replace("'", " ");
                            if (spaceRem1.length() > 55) {
                                finalAddress1_line1 = spaceRem1.substring(0, 52);
                            } else {
                                finalAddress1_line1 = address1_line1;
                            }
                        }
                        String finalAddress1_line2 = "";
                        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address1_line2")) {
                            address1_line2 = (String) studRecord.get("address1_line2");
                            String spaceRem2 = address1_line2.replace("'", " ");
                            if (spaceRem2.length() > 50) {
                                finalAddress1_line2 = spaceRem2.substring(0, 49);
                            } else {
                                finalAddress1_line2 = address1_line2;
                            }
                        }
                        String finalAddress1_line3 = "";
                        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address1_line3")) {
                            address1_line3 = (String) studRecord.get("address1_line3");
                            String spaceRem3 = address1_line3.replace("'", " ");
                            if (spaceRem3.length() > 50) {
                                finalAddress1_line3 = spaceRem3.substring(0, 49);
                            } else {
                                finalAddress1_line3 = address1_line3;
                            }
                        }
                        String finalAddress1_line4 = "";
                        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "address1_line4")) {
                            address1_line4 = (String) studRecord.get("address1_line4");
                            String spaceRem4 = address1_line4.replace("'", " ");
                            if (spaceRem4.length() > 50) {
                                finalAddress1_line4 = spaceRem4.substring(0, 49);
                            } else {
                                finalAddress1_line4 = address1_line4;
                            }
                        }
                        String addressType = "";
                        if (addressType1 == true) {
                            addressType = addres1Type;
                        }
                        if (addressType1 == false) {
                            addressType = addres2Type;
                        }
                        String spaceRemoved = address1_postalcode.replace(" ", "");
                        int intPostLength = spaceRemoved.length();
                        String finalPost = "";
                        if (intPostLength > 9) {
                            finalPost = spaceRemoved.substring(0, 9);
                        }
                        insertStudentAddressOnLocal(Long.parseLong(ec_studentnumber), addressType, postcountry, ec_province, finalAddress1_line1, finalAddress1_line2, finalAddress1_line3, finalAddress1_line4, finalPost, strCaptrureDate);
                        service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Address Record inserted into LocaL DB for existing ID number");
                        System.out.println("Student Address Records inserted for existing ID number.....");

                        String mobilephone = "";
                        String contact_type = "";
                        String contactAreaCodePart = "";
                        String remContactNumber = "";
                        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "mobilephone")) {
                            contact_type = "Cel";
                            mobilephone = (String) studRecord.get("mobilephone");
                            remContactNumber = mobilephone.substring(3);
                            contactAreaCodePart = mobilephone.substring(0, 3);
                            insertContactDataOnLocal(Long.parseLong(ec_studentnumber), contact_type, ec_nationality, contactAreaCodePart, remContactNumber, strCaptrureDate);
                            service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Mobile Contact Record inserted");
                            System.out.println("Student Mobile Contact Records inserted for existing ID number.....");
                        }
                        String home_tel = "";
                        String remTel1ContactNumber = "";
                        String contactTel1AreaCodePart = "";
                        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "telephone1")) {
                            contact_type = "Hom";
                            home_tel = (String) studRecord.get("telephone1");
                            contactTel1AreaCodePart = home_tel.substring(0, 2);
                            remTel1ContactNumber = home_tel.substring(3);
                            insertContactDataOnLocal(Long.parseLong(ec_studentnumber), contact_type, ec_nationality, contactTel1AreaCodePart, remContactNumber, strCaptrureDate);
                            service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Home Contact Record inserted");
                            System.out.println("Student Home Contact Records inserted for existing ID number.....");
                        }
                        String work_tel = "";
                        String contactTel2AreaCodePart = "";
                        String remTel2ContactNumber;
                        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "telephone2")) {
                            work_tel = (String) studRecord.get("telephone2");
                            contact_type = "Wrk";
                            contactTel2AreaCodePart = work_tel.substring(0, 2);
                            remTel2ContactNumber = work_tel.substring(3);
                            insertContactDataOnLocal(Long.parseLong(ec_studentnumber), contact_type, ec_nationality, contactTel2AreaCodePart, remTel2ContactNumber, strCaptrureDate);
                            service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Work Contact Record inserted");
                            System.out.println("Student Work Contact Records inserted for existing ID number.....");
                        }
                    } else {
                        System.out.println("Duplicate Student ID number :" + OLD_STUDENT_NUM);
                        service_bus_error_log.append("\n" + formattedDateTime + ": " + " Duplicate Student ID number :" + GLOBAL_ID_NUMBER + " found in Student Stageing");
                    }
                } else {
                    System.out.println("Duplicate Student ID number :" + GLOBAL_ID_NUMBER);
                    service_bus_error_log.append("\n" + formattedDateTime + ": " + " Duplicate Student ID number :" + GLOBAL_ID_NUMBER + " found in LIVE SBS Database");
                }
            }
        } else {

            System.out.println("CRM Generated student number already belongs to another student - REMOTE CHECKED");
            mailSender.sendMailToIT(GLOBAL_ID_NUMBER, GLOBAL_ID_NUMBER + " CRM Generated student number already belongs to another student - REMOTE CHECKED");
            service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " DUPLICATE Entry");
        }

    } /*else {
        String passport = "";
        service_bus_error_log.append("\n" + formattedDateTime + ": Student number field is missing, student ID NUMBER: " + GLOBAL_ID_NUMBER);
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "passport")) {
            passport = (String) studRecord.get("passport");
            if (!passport.equalsIgnoreCase("")) {
                GLOBAL_ID_NUMBER = passport;
                System.out.println("Passport Number is " + passport);
            }
        }
        String saidnumber = "";
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_saidnumber")) {
            saidnumber = (String) studRecord.get("ec_saidnumber");
            if (!saidnumber.equalsIgnoreCase("")) {
                GLOBAL_ID_NUMBER = saidnumber;
            }
        }
        String ec_namibianidnumber = "";
        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_namibianidnumber")) {
            ec_namibianidnumber = (String) studRecord.get("ec_namibianidnumber");
            if (!ec_namibianidnumber.equalsIgnoreCase("")) {
                GLOBAL_ID_NUMBER = ec_namibianidnumber;
            }
        }
        mailSender.sendMailToIT(GLOBAL_ID_NUMBER, GLOBAL_ID_NUMBER + " Student number field is missing - ID Number " + GLOBAL_ID_NUMBER);
        System.out.println("Student number field is missing - ID Number " + GLOBAL_ID_NUMBER);
        System.out.println(formattedDateTime);
    }*/


}

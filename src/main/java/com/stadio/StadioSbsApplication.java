package com.stadio;

import com.stadio.daos.StadioDao;
import com.stadio.daos.StadioStageDao;
import com.stadio.mail.MailSender;
import com.stadio.models.*;
import com.stadio.services.JsonKeyChecker;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EnableBinding({Source.class, Sink.class})
@SpringBootApplication
public class StadioSbsApplication {

    private String myDriver = "com.mysql.cj.jdbc.Driver";//?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
    private String myUrl = "jdbc:mysql://localhost:3306/sbs?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    public static void main(String[] args) {
        SpringApplication.run(StadioSbsApplication.class, args);
    }

    @StreamListener(Sink.INPUT)
    public void consume(Message<String> msg) throws IOException, ParseException {
        boolean NEW_APPLICATION_ARRIVED = false;
        boolean less_tha8_studNo_NO_identified = false;
        boolean MISSING_STUD_NO = false;
        boolean NO_CORRESPONDING_ADDRESS = false;
        boolean NO_CORRESPONDING_CONTACT = false;
        boolean ADDRESS_ALREADY_EXISTS = false;
        boolean CONTACT_ALREADY_EXISTS = false;
        boolean ID_NUMBER_ALREADY_EXISTS = false;
        boolean DUPLICATE_STUD_NO = false;
        boolean MISSING_START_SEM = false;
        boolean MISSING_SEM_CYCLE = false;
        boolean QUAL_ALREADY_EXISTS = false;
        boolean QUAL_HAS_NO_MAJOR = false;
        boolean MAJOR_DOES_NOT_EXIST = false;
        boolean UNABLE_TO_UPDATE_RECORD = false;
        boolean DUP_STUD_ID_FOUND = false;

        StringBuffer SRA_CRM_MESSAGE = new StringBuffer("CRM Student ");
        StringBuffer IT_CRM_MESSAGE = new StringBuffer("CRM Student ");
        MailSender mailSender = new MailSender();
        JsonKeyChecker jsonKeyChecker = new JsonKeyChecker();
        System.out.println("------------------------------------");
        //String payload = msg.getPayload();
		
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime todaysDate1 = LocalDateTime.now();
        String formattedDateTime1 = todaysDate1.format(formatter1);
		//FileWriter service_bus_main_message_log_1 = new FileWriter("C:\\sbs\\service-bus\\DUMP\\Dump_"+formattedDateTime1+".txt");
        //service_bus_main_message_log_1.append("\n" + payload);
		//service_bus_main_message_log_1.close();

        FileWriter service_bus_error_log = new FileWriter("C:\\sbs\\service-bus\\error_log.txt", true);
        FileWriter service_bus_audit_log = new FileWriter("C:\\sbs\\service-bus\\audit_log.txt", true);
        FileWriter service_bus_audit_trails = new FileWriter("C:\\sbs\\service-bus\\audit_report.txt", true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime todaysDate = LocalDateTime.now();
        String formattedDateTime = todaysDate.format(formatter);
        StadioDao stadioDao = null;
        StadioStageDao stadioStageDao = null;
        try {
            stadioDao = new StadioDao();
            stadioStageDao = new StadioStageDao();
            String payload = msg.getPayload();

            String GLOBAL_ID_NUMBER = "";
            String CONTACT_DATA = "#Microsoft.Dynamics.CRM.contact";
            String APPLICATION_DATA = "#Microsoft.Dynamics.CRM.ec_application";
            JSONParser jsonParser = new JSONParser();
            JSONObject studRecord = (JSONObject) jsonParser.parse(payload);

            String SRA_EMAIL_ADDRESS = "";
            String date2WithSeconds = "";
            if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "@odata.type")) {
                String data_type = (String) studRecord.get("@odata.type");

                String createdon = "";
                String strCreatedPM = "";
                String strRemovedPMAM = "";
                String strCaptrureDate = "";
                if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "createdon@OData.Community.Display.V1.FormattedValue")) {
                    createdon = (String) studRecord.get("createdon@OData.Community.Display.V1.FormattedValue");
                    date2WithSeconds = (String) studRecord.get("createdon");
                    strCreatedPM = createdon.replace("PM", "");
                    strRemovedPMAM = strCreatedPM.replace("AM", "");
                    strCaptrureDate = strRemovedPMAM.stripTrailing();
                }
                String newDate1 = date2WithSeconds.replace(":", "_");
                String newDate2 = newDate1.replaceAll("////", "-");
                System.out.println("New Date 2 is " + newDate2);
                String ec_studentnumber = "";
                String strEc_studentnumber = "";
                String ORPHAN = "";
                String data_type2 = data_type.replace("#Microsoft.Dynamics.CRM.", "");
                if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_studentnumber")) {
                    ec_studentnumber = (String) studRecord.get("ec_studentnumber");
                    strEc_studentnumber = ec_studentnumber;
                    if(!stadioStageDao.chekIfStudentExistsInStage_Student_table_use_STUDNO(ec_studentnumber)){
                        ORPHAN = "Orphan";
                    }
                } else {
                    strEc_studentnumber = "error";
                }
                String strFileName = strEc_studentnumber +"_"+ORPHAN+ "_" + data_type2 + "_" + newDate2;
                String file_name = strFileName.replace('/', '-') + ".txt";
                FileWriter service_bus_main_message_log = new FileWriter("C:\\sbs\\service-bus\\Main Files\\" + file_name);
                service_bus_main_message_log.append("\n" + payload);
                service_bus_main_message_log.close();
                if (!stadioStageDao.messageExists(file_name)) {
                    if (stadioStageDao.saveServiceBusMessage(file_name) > 0) {
                        System.out.println("File name created successfully");
                    } else {
                        System.out.println("Unable to save file");
                    }
                } else {
                    System.out.println("File already exists");
                }

                if (data_type.equalsIgnoreCase(CONTACT_DATA)) {
                    ec_studentnumber = (String) studRecord.get("ec_studentnumber");
                    if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_studentnumber")) {
                        //call processContactData method
                        stadioStageDao.processContactData(ec_studentnumber, GLOBAL_ID_NUMBER, studRecord, jsonKeyChecker, service_bus_audit_log, formattedDateTime, mailSender, service_bus_error_log, DUP_STUD_ID_FOUND, SRA_CRM_MESSAGE, strCaptrureDate);
                        System.out.println("Done processing contact");
                    } else {
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
                    }
                    /*ENDING CONTACT*/
                    System.out.println("Data Type is :" + data_type);
                    /*--------------------- -----------------APPLICATION ENDING HERE --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
                    /*--------------------- -----------------APPLICATION ENDING HERE--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
                    /*--------------------- -----------------APPLICATION ENDING HERE--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
                } else if (data_type.equalsIgnoreCase(APPLICATION_DATA)) {
                    StringBuffer AUDIT_REPORT_MESSAGE = new StringBuffer("CRM STUDENT ");
                    if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_studentnumber")) {
                        ec_studentnumber = (String) studRecord.get("ec_studentnumber");
                        FileWriter service_bus_app_txt_file = new FileWriter("C:\\sbs\\service-bus\\" + ec_studentnumber + " application data.txt");
                        String qual_name = "";
                        Boolean ec_coprincipaldebtorindicated = null;
                        String ec_erpcostcentre = "";
                        int ec_coprincipaldebtorindicated_ = 0;
                        String DEFAULT_SRA = "STADIO Student Advisor";
                        String strSRA_Name = "";
                        ArrayList<Student> students1 = (ArrayList<Student>) stadioStageDao.getStudentExistInStageStudent(ec_studentnumber);

                        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "_ec_qualificationname_value")) {
                            qual_name = (String) studRecord.get("_ec_qualificationname_value");
                            /*if(qual_name.equalsIgnoreCase("04290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Advanced Diploma in Management";
                            } if(qual_name.equalsIgnoreCase("aca3c07e-2f66-eb11-a812-0022489bf7fc")){
                                qual_name = "Bachelor of Arts in Law - DL";
                            } if(qual_name.equalsIgnoreCase("06290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Bachelor of Business Administration";
                            } if(qual_name.equalsIgnoreCase("08290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Bachelor of Business Administration (Honours)";
                            } if(qual_name.equalsIgnoreCase("0a290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Bachelor of Commerce in Law";
                            } if(qual_name.equalsIgnoreCase("0c290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Bachelor of Laws";
                            } if(qual_name.equalsIgnoreCase("0e290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Bachelor of Policing Practices";
                            } if(qual_name.equalsIgnoreCase("10290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Bachelor of Policing Practices (Honours)";
                            } if(qual_name.equalsIgnoreCase("12290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Diploma in Management";
                            } if(qual_name.equalsIgnoreCase("1c290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Diploma in Policing";
                            } if(qual_name.equalsIgnoreCase("1e290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Doctor of Management";
                            } if(qual_name.equalsIgnoreCase("20290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Doctor of Policing";
                            } if(qual_name.equalsIgnoreCase("14290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Higher Certificate in Management";
                            } if(qual_name.equalsIgnoreCase("16290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Higher Certificate in Paralegal Studies - DL";
                            } if(qual_name.equalsIgnoreCase("18290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Master of Management";
                            } if(qual_name.equalsIgnoreCase("1a290126-cce2-ea11-a817-000d3ab38d6a")){
                                qual_name = "Master of Policing Practice";
                            }*/
                        }

                        if (stadioStageDao.getSRA_GRP(qual_name) != null) {
                            SRA_EMAIL_ADDRESS = stadioStageDao.getSRA_GRP(qual_name);
                            if (!students1.isEmpty()) {
                                for (Student student : students1) {
                                    if(student.getNationality_fk().equalsIgnoreCase("NA")){
                                        SRA_EMAIL_ADDRESS = "staff-admin-nam@sbs.ac.za";
                                    }
                                }
                            }
                        } else {
                            SRA_EMAIL_ADDRESS = "crm-sra@sbs.ac.za";
                        }

                        String qualfk = stadioStageDao.getQualPk(qual_name);

                        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "_ownerid_value@OData.Community.Display.V1.FormattedValue")) {
                            strSRA_Name = (String) studRecord.get("_ownerid_value@OData.Community.Display.V1.FormattedValue");
                            String fName = strSRA_Name.substring(0, strSRA_Name.indexOf(' '));
                            System.out.println("SRA First name: " + fName);
                        }

                        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_coprincipaldebtorindicated")) {
                            ec_coprincipaldebtorindicated  = (boolean) studRecord.get("ec_coprincipaldebtorindicated");
                            if(ec_coprincipaldebtorindicated){
                                ec_coprincipaldebtorindicated_ = 1;
                            }
                            System.out.println("ec_coprincipaldebtorindicated: " + ec_coprincipaldebtorindicated +": ec_coprincipaldebtorindicated_: "+ec_coprincipaldebtorindicated_);

                        }
                        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_erpcostcentre")) {
                            ec_erpcostcentre  = (String) studRecord.get("ec_erpcostcentre");
                            if (!ec_erpcostcentre.equalsIgnoreCase("")) {
                                System.out.println("ec_erpcostcentre: " + ec_erpcostcentre + ": ec_erpcostcentre: " + ec_erpcostcentre);
                            }
                        }
                        String QUAL_FK = qualfk;
                        qual_name = stadioStageDao.getQualName(QUAL_FK);

                        //System.out.println("Qual fk at 785 " + QUAL_FK + " = " + qualfk);
                        String fname = "";
                        String lname = "";
                        String qual = "";
                        String idNo = "";
                        String MAJOR = "";
                        String STR_CURRENT_SEMESTER = "";
                        String startSemester = "";
                        String semesterCycle = "";
                        String complete_semester = startSemester + semesterCycle;
                        String sem_last_enrolled = complete_semester;
                        String studLatestQual = "";
                        String maxStudentSemester = "00000";
                        String DATE_FIRST_ENROLLED = "";
                        String QUAL_ACH_STATUS = "Enr";
                        String DATE_LAST_ENROLLED = "";
                        String QUAL_HONS_STATUS_PK = "N_A";
                        int CREDIT_AWARDED = 0;
                        String CURRENT_SEMESTER = "";

                        String major = "---";
                        String strMajorField = "";

                        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "specialisations")) {
                            Object specialisations_obj = studRecord.get("specialisations");

                            if (specialisations_obj instanceof JSONArray) {
                                JSONArray specialisations = (JSONArray) studRecord.get("specialisations");
                                //JSONArray specialisations = (JSONArray) studRecord.get("Id");
                                if (!specialisations.isEmpty()) {
                                    for (int i = 0; i < specialisations.size(); i++) {
                                        JSONObject spec = (JSONObject) specialisations.get(i);
                                        strMajorField = spec.get("Id").toString();
                                        System.out.println("Value of Specialisation " + Integer.toString(i + 1) + " is -> " + strMajorField);
                                    }
                                    //major = stadioStageDao.getMajorStream(strMajorField);
                                    major = stadioDao.getMajorStreamLive(strMajorField);
                                    if (!major.isEmpty()) {
                                        System.out.println("CRM Major" + strMajorField);
                                        System.out.println("Found Major " + major);
                                        SRA_CRM_MESSAGE.append("\n - The chosen Major for this application is "+major);
                                    } else {
                                        //Notify SRA =
                                        MAJOR_DOES_NOT_EXIST = true;
                                        service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " The chosen major does not exist on the database, please update major accordingly");
                                        System.out.println("The chosen major does not exist on the database, please update major accordingly");
                                        SRA_CRM_MESSAGE.append("\n - The chosen major does not exist on the database, please update major accordingly");
                                    }
                                }
                            }else {
                                System.out.println("Specialisation is not ARRAY");
                            }

                        } else {
                            QUAL_HAS_NO_MAJOR = true;
                            SRA_CRM_MESSAGE.append("\n - This is to notify you that the chosen application does not have a Major, no action is required, unless the student has chosen a Major. In which case you will need to delete the qualification and enroll the student for the selected Major/Stream");
                            System.out.println("Qualification has No Major " + qual_name);
                            service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " Qualification has No Major");
                        }

                        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_startdate@OData.Community.Display.V1.FormattedValue")) {
                            if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "ec_semesterorcycle@OData.Community.Display.V1.FormattedValue")) {
                                startSemester = (String) studRecord.get("ec_startdate@OData.Community.Display.V1.FormattedValue");
                                semesterCycle = (String) studRecord.get("ec_semesterorcycle@OData.Community.Display.V1.FormattedValue");
                                SRA_CRM_MESSAGE.append("\n - " + ec_studentnumber + " Enrolled for : "+startSemester+semesterCycle);
                            } else {
                                service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " Start Semester not specified during application process");
                                MISSING_START_SEM = true;
                                SRA_CRM_MESSAGE.append("\n - " + ec_studentnumber + " Start Semester not specified during application process");
                            }
                        } else {
                            System.out.println("Start Semester Cycle Start not specified during application process");
                            SRA_CRM_MESSAGE.append("\n - Start Semester Cycle Start not specified during application process");
                            MISSING_SEM_CYCLE = true;
                            service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " Start Semester Cycle not specified dusring application process");
                        }

                        //if (!startSemester.equalsIgnoreCase("") || !semesterCycle.equalsIgnoreCase("")) {
                            ArrayList<Semester> semesters = (ArrayList<Semester>) stadioDao.getSemester(startSemester, semesterCycle);
                            if (!semesters.isEmpty()) {
                                for (Semester semester : semesters) {
                                    CURRENT_SEMESTER = semester.getSem_pk();
                                    STR_CURRENT_SEMESTER = CURRENT_SEMESTER;
                                    DATE_FIRST_ENROLLED = semester.getSem_start_date();
                                    DATE_LAST_ENROLLED = semester.getSem_end_date();
                                }
                            } else {
                                System.out.println("Semester not found");
                                service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " Semester not found");
                            }
                       /* } else {
                            System.out.println("Semester cycle and start cannot be empty");
                            service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " Semester cycle and start cannot be empty");
                        }*/
                        String res = stadioDao.getStringStudentMaximumSemester(ec_studentnumber);
                        String studentObtained_sem_fk = "";
                        if (res != null) {
                            maxStudentSemester = res;
                            studLatestQual = stadioDao.getStudentMaximumSemesterQual_fk(maxStudentSemester);
                            System.out.println("studLatestQual " + studLatestQual + " ec_studentnumber " + ec_studentnumber);
                            System.out.println("studLatestQual is " + res);
                            SRA_CRM_MESSAGE.append("\n - Student was last enrolled for "+res);
                        } else {
                            SRA_CRM_MESSAGE.append("\n - Student was not previously enrolled.");
                            System.out.println("studLatestQual is null ");
                        }
                        if (jsonKeyChecker.checkIfKeyExists(studRecord.toJSONString(), "createdon@OData.Community.Display.V1.FormattedValue")) {
                            createdon = (String) studRecord.get("createdon@OData.Community.Display.V1.FormattedValue");
                            strCreatedPM = createdon.replace("PM", "");
                            strRemovedPMAM = strCreatedPM.replace("AM", "");
                            strCaptrureDate = strRemovedPMAM.stripTrailing();
                        }
                        studentObtained_sem_fk = stadioDao.getStudentObtainedSemester(ec_studentnumber, studLatestQual);
                        if (studentObtained_sem_fk != null) {
                            System.out.println("Student obtained sem " + studentObtained_sem_fk);
                        } else {
                            System.out.println("Student does not have obtained_sem_fk");
                        }
                        System.out.println("QUAL FK AT THE TOP " + qualfk + " " + qual_name);
                        String id_number = "";
                        String student_group = "";
                        String type_stu = "I";

                        ArrayList<Student> students = (ArrayList<Student>) stadioStageDao.getStudentExistInStageStudent(ec_studentnumber);
                        if (!students.isEmpty()) {
                            id_number = stadioStageDao.getLocalIDnumber(ec_studentnumber);
                            System.out.println("Local ID number is " + id_number);
                            //need to add ID number length check and not null || > 0
                            if (!id_number.equalsIgnoreCase("") || id_number != null) {
                                if (!stadioDao.liveliveStudentExistsWithID(id_number)) {
                                    System.out.println("===================================CAN ENTER HERE====================================================");
                                    if (!stadioDao.liveLiveStudentExists(ec_studentnumber)) {
                                        for (Student student : students) {
                                            fname = student.getFirst_name();
                                            lname = student.getLast_name();
                                            idNo = student.getId_number();
                                            id_number = idNo;
                                            System.out.println("ID NUMBER IS " + id_number);
                                            if(ec_coprincipaldebtorindicated_ == 1){
                                                student_group = "AAA001";
                                            }else {
                                                student_group = student.getSg_fk();
                                            }
                                            stadioDao.insertIntoStudentLiveDB(student.getStudent_pk(), student.getFileref_fk(), student_group, student.getNationality_fk(), student.getId_number(), student.getCrs_fk(), student.getTitle_fk(),
                                                    student.getLast_name(), student.getFirst_name(), student.getMiddle_name(), student.getPrevious_ln(), student.getDob(), student.getGender_fk(), student.getHl_fk(), student.getDs_fk(),
                                                    student.getSeeing_rating_fk(), student.getHearing_rating_fk(), student.getCommunication_rating_fk(), student.getWalking_rating_fk(), student.getRemembering_rating_fk(), student.getSelfcare_rating_fk(),
                                                    student.getSes_fk(), student.getEquity_fk(), student.getEquity_oth_fk(), student.getEmail(), student.getExamvenue_fk(), student.getDigital_pref(), student.getId_on_record(), student.getSc_on_record(),
                                                    student.getHq_on_record(), student.getInactive_debt(), student.getWithhold_qual(), student.getStudent_cancelled(), student.getMysbs_active(), student.getCaptured_by(), student.getCapture_date(), ec_studentnumber, 1,ec_coprincipaldebtorindicated_,ec_erpcostcentre,type_stu);
                                            service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Student Data Insertted into LIVE Database");
                                            System.out.println("Student Data Insertted into LIVE Database");
                                        }
                                        System.out.println("studentObtained_sem_fk " + studentObtained_sem_fk);
                                        QUAL_FK = qualfk;
                                        System.out.println("Qual FK is " + qualfk);
                                        if (!qualfk.isEmpty()) {
                                            System.out.println("New Student QUAL_FK " + QUAL_FK);
                                            if (!stadioDao.checkQualExistFirst(ec_studentnumber, qualfk)) {
                                                if (!maxStudentSemester.equalsIgnoreCase(complete_semester)) {
                                                    if (studentObtained_sem_fk.equalsIgnoreCase("")) {
                                                        System.out.println("studentObtained_sem_fk came back empty");
                                                        if (stadioDao.insertQualification(ec_studentnumber, qualfk, major, QUAL_ACH_STATUS, CURRENT_SEMESTER, DATE_FIRST_ENROLLED, CURRENT_SEMESTER, DATE_LAST_ENROLLED, CREDIT_AWARDED, QUAL_HONS_STATUS_PK) > 0) {
                                                            System.out.println("Qualification Fk: " + qualfk);
                                                            service_bus_app_txt_file.write("{ " + "\nStudNumber :" + ec_studentnumber +
                                                                    "\n QualificationName:" + qual_name +
                                                                    "\n Major:" + major +
                                                                    "\n Semester:" + CURRENT_SEMESTER +
                                                                    "\n SRA :" + strSRA_Name + "\n}");
                                                            System.out.println("Qualification inserted into  LIVE DB...");

                                                            service_bus_app_txt_file.close();
                                                            service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Qualification Data Inserted LIVE Database");
                                                            NEW_APPLICATION_ARRIVED = true;
                                                            SRA_CRM_MESSAGE.append("\n - A new application was made from CRM - Student ID Number " + idNo + " With Student Number : " + ec_studentnumber + "\nRegistered for :" + qual_name + ", please complete the registration in SBS Data Gateway");

                                                            complete_semester = startSemester + semesterCycle;

                                                            double stud_reg_fee = stadioDao.getARegFeeAmount(qualfk, complete_semester);
                                                            if (ec_studentnumber != "" & complete_semester != "" & qualfk != "" & strCaptrureDate != "") {
                                                               /* if (stadioDao.insertStudentAccount(ec_studentnumber, complete_semester, 58, qualfk, stud_reg_fee, 1, stud_reg_fee, strCaptrureDate, "Registration Fee", 99, strCaptrureDate) > 0) {
                                                                    System.out.println("Registration fee Inserted into LIVE Database....");
                                                                    service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Registration fee Inserted into LIVE Database");
                                                                } else {
                                                                    System.out.println("Failed to insert account details....");
                                                                }*/
                                                            } else {
                                                                System.out.println("Sorry all fields are mandatory ");
                                                            }

                                                        } else {
                                                            QUAL_ALREADY_EXISTS = true;
                                                            SRA_CRM_MESSAGE.append("\n - This qualification is already active for this student " + ec_studentnumber + " ");
                                                            System.out.println("This qualification is already active for this student " + ec_studentnumber + " please check the database to confirm 1");
                                                            service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " This qualification is already active for this student " + ec_studentnumber + " please check the database to confirm");
                                                        }

                                                    } else {
                                                        if (studentObtained_sem_fk != null) {
                                                            if (stadioDao.insertQualification(ec_studentnumber, qualfk, major, QUAL_ACH_STATUS, CURRENT_SEMESTER, DATE_FIRST_ENROLLED, CURRENT_SEMESTER, DATE_LAST_ENROLLED, CREDIT_AWARDED, QUAL_HONS_STATUS_PK) > 0) {
                                                                System.out.println("Qualification Fk: " + qualfk);
                                                                service_bus_app_txt_file.write("{ " + "\nStudNumber :" + ec_studentnumber +
                                                                        "\n QualificationName:" + qual_name +
                                                                        "\n Major:" + major +
                                                                        "\n Semester:" + CURRENT_SEMESTER +
                                                                        "\n SRA :" + strSRA_Name + "\n}");
                                                                System.out.println("Qualification inserted into  LIVE DB...");

                                                                service_bus_app_txt_file.close();
                                                                service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Qualification Data Inserted LIVE Database");
                                                                NEW_APPLICATION_ARRIVED = true;
                                                                SRA_CRM_MESSAGE.append("\n - A new application was made from CRM - Student ID Number " + idNo + " With Student Number : " + ec_studentnumber + "\nRegistered for :" + qual_name + ", please complete the registration in SBS Data Gateway");
                                                                complete_semester = startSemester + semesterCycle;

                                                                double stud_reg_fee = stadioDao.getARegFeeAmount(qualfk, complete_semester);
                                                                if (ec_studentnumber != "" & complete_semester != "" & qualfk != "" & strCaptrureDate != "") {
                                                                 /*   if (stadioDao.insertStudentAccount(ec_studentnumber, complete_semester, 58, qualfk, stud_reg_fee, 1, stud_reg_fee, strCaptrureDate, "Registration Fee", 99, strCaptrureDate) > 0) {
                                                                        System.out.println("Registration fee Inserted into LIVE Database....");
                                                                        service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Registration fee Inserted into LIVE Database");
                                                                    } else {
                                                                        System.out.println("Failed to insert account details....");
                                                                    }*/
                                                                } else {
                                                                    System.out.println("Sorry all fields are mandatory ");
                                                                }

                                                            } else {
                                                                QUAL_ALREADY_EXISTS = true;
                                                                SRA_CRM_MESSAGE.append("\n - This qualification is already active for this student " + ec_studentnumber + " ");
                                                                System.out.println("This qualification is already active for this student " + ec_studentnumber + " please check the database to confirm 1");
                                                                service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " This qualification is already active for this student " + ec_studentnumber + " please check the database to confirm");
                                                            }
                                                            System.out.println("studentObtained_sem_fk came back not null");
                                                        } else {
                                                            System.out.println("Unable to insert new qualification as the student is still in progress with their previous qualification");
                                                            SRA_CRM_MESSAGE.append("\n - Unable to insert new qualification as the student is still in progress with their previous qualification");
                                                        }
                                                        QUAL_ALREADY_EXISTS = true;
                                                        SRA_CRM_MESSAGE.append("\n - This qualification is already active for this student " + ec_studentnumber + " please check the database to confirm re-registration");
                                                        System.out.println("This qualification is already active for this student " + ec_studentnumber + " please check the database to confirm 3");
                                                    }
                                                } else {
                                                    QUAL_ALREADY_EXISTS = true;
                                                    SRA_CRM_MESSAGE.append("\n - This student is already registered for the current semester - Qualification Name: " + studLatestQual);
                                                    service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " This student is already registered for the current semester - Qualification Name: " + studLatestQual);
                                                }
                                            } else {
                                                QUAL_ALREADY_EXISTS = true;
                                                SRA_CRM_MESSAGE.append("\n - " + ec_studentnumber + " This qualification is already active for this student " + ec_studentnumber + " please check the database to confirm");
                                                System.out.println("This qualification is already active for this student " + ec_studentnumber + " please check the database to confirm 4");
                                            }
                                        } else {
                                            ////Notify SRA
                                            System.out.println("Qualification Name not found");
                                            service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " Qualification full name not found");
                                            SRA_CRM_MESSAGE.append("\n - Qualification name " + qual_name + " not found " + ec_studentnumber + " Qualification full name not found - please contact IT");
                                        }

                                        if (ec_studentnumber != "" || strCaptrureDate != "" || CURRENT_SEMESTER != "") {
                                            if (stadioDao.saveNote(ec_studentnumber, strCaptrureDate, CURRENT_SEMESTER) > 0) {
                                                System.out.println("Note Saved successfully");
                                                service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " Unable to Save Note");
                                            } else {
                                                service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " Unable to Save Note");
                                                System.out.println("Unable to Save Note");
                                            }
                                        } else {
                                            System.out.println("Could not insert into Note, notes fields cannot be empty.");
                                        }

                                    } else {
                                        System.out.println("QUAL CREATION FOR EXISTING STUDENTS IN LIVE DB");
                                        System.out.println("QUAL_FK IS " + QUAL_FK);
                                        if (QUAL_FK != "") {
                                            if (!stadioDao.checkQualExistFirst(ec_studentnumber, QUAL_FK)) {
                                                if (!maxStudentSemester.equalsIgnoreCase(complete_semester)) {
                                                    if (studentObtained_sem_fk.equalsIgnoreCase("")) {
                                                        if (stadioDao.insertQualification(ec_studentnumber, qualfk, major, QUAL_ACH_STATUS, CURRENT_SEMESTER, DATE_FIRST_ENROLLED, CURRENT_SEMESTER, DATE_LAST_ENROLLED, CREDIT_AWARDED, QUAL_HONS_STATUS_PK) > 0) {
                                                            System.out.println("Qualification has been inseted into LIVE DB: " + QUAL_FK);
                                                            System.out.println("Qualification Fk: " + QUAL_FK);
                                                            service_bus_app_txt_file.write("{ " + "\nStudNumber :" + ec_studentnumber +
                                                                    "\n QualificationName:" + QUAL_FK +
                                                                    "\n Major:" + MAJOR +
                                                                    "\n Semester:" + STR_CURRENT_SEMESTER +
                                                                    "\n SRA :" + strSRA_Name + "\n}");

                                                            complete_semester = startSemester + semesterCycle;

                                                            double stud_reg_fee = stadioDao.getARegFeeAmount(qualfk, complete_semester);
                                                            if (ec_studentnumber != "" & complete_semester != "" & qualfk != "" & strCaptrureDate != "") {
                                                              /*  if (stadioDao.insertStudentAccount(ec_studentnumber, complete_semester, 58, qualfk, stud_reg_fee, 1, stud_reg_fee, strCaptrureDate, "Registration Fee", 99, strCaptrureDate) > 0) {
                                                                    System.out.println("Registration fee Inserted into LIVE Database....");
                                                                    service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Registration fee Inserted into LIVE Database");
                                                                } else {
                                                                    System.out.println("Failed to insert account details....");
                                                                    SRA_CRM_MESSAGE.append("\n - Failed to insert account details....");
                                                                }*/
                                                            } else {
                                                                System.out.println("Sorry all fields are mandatory ");
                                                                SRA_CRM_MESSAGE.append("\n - Sorry all fields are mandatory....");
                                                            }
                                                        } else {
                                                            System.out.println("Failed to insert qualification for new student");
                                                            SRA_CRM_MESSAGE.append("\n - Failed to insert qualification for new student");
                                                        }

                                                    } else {

                                                        if (studentObtained_sem_fk != null) {
                                                            if (stadioDao.insertQualification(ec_studentnumber, qualfk, major, QUAL_ACH_STATUS, CURRENT_SEMESTER, DATE_FIRST_ENROLLED, CURRENT_SEMESTER, DATE_LAST_ENROLLED, CREDIT_AWARDED, QUAL_HONS_STATUS_PK) > 0) {
                                                                System.out.println("Qualification Fk: " + qualfk);
                                                                service_bus_app_txt_file.write("{ " + "\nStudNumber :" + ec_studentnumber +
                                                                        "\n QualificationName:" + qual_name +
                                                                        "\n Major:" + major +
                                                                        "\n Semester:" + CURRENT_SEMESTER +
                                                                        "\n SRA :" + strSRA_Name + "\n}");
                                                                System.out.println("Qualification inserted into  LIVE DB...");

                                                                service_bus_app_txt_file.close();
                                                                service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Qualification Data Inserted LIVE Database");
                                                                NEW_APPLICATION_ARRIVED = true;
                                                                SRA_CRM_MESSAGE.append("\n - A new application was made from CRM - Student ID Number " + idNo + " With Student Number : " + ec_studentnumber + "\nRegistered for :" + qual_name + ", please complete the registration in SBS Data Gateway");

                                                                complete_semester = startSemester + semesterCycle;

                                                                double stud_reg_fee = stadioDao.getARegFeeAmount(qualfk, complete_semester);
                                                                if (ec_studentnumber != "" & complete_semester != "" & qualfk != "" & strCaptrureDate != "") {
                                                                  /*  if (stadioDao.insertStudentAccount(ec_studentnumber, complete_semester, 58, qualfk, stud_reg_fee, 1, stud_reg_fee, strCaptrureDate, "Registration Fee", 99, strCaptrureDate) > 0) {
                                                                        System.out.println("Registration fee Inserted into LIVE Database....");
                                                                        service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Registration fee Inserted into LIVE Database");
                                                                    } else {
                                                                        System.out.println("Failed to insert account details....");
                                                                    }*/
                                                                } else {
                                                                    System.out.println("Sorry all fields are mandatory ");
                                                                }

                                                            } else {
                                                                QUAL_ALREADY_EXISTS = true;
                                                                SRA_CRM_MESSAGE.append("\n - This qualification is already active for this student " + ec_studentnumber + " ");
                                                                System.out.println("This qualification is already active for this student " + ec_studentnumber + " please check the database to confirm 1");
                                                                //mailSender.sendMailToIT(GLOBAL_ID_NUMBER,GLOBAL_ID_NUMBER + " This qualification is already active for this student "+ec_studentnumber+" please check the database to confirm");
                                                                service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " This qualification is already active for this student " + ec_studentnumber + " please check the database to confirm");
                                                            }
                                                            System.out.println("studentObtained_sem_fk came back not null");
                                                        } else {
                                                            System.out.println("Unable to insert new qualification as the student is still in progress with their previous qualification");
                                                            SRA_CRM_MESSAGE.append("\n - Unable to insert new qualification as the student is still in progress with their previous qualification");
                                                        }
                                                    }
                                                } else {
                                                    QUAL_ALREADY_EXISTS = true;
                                                    SRA_CRM_MESSAGE.append("\n - This student is already registered for the current semester - Qualification Name: " + studLatestQual);
                                                    service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " This student is already registered for the current semester - Qualification Name: " + studLatestQual);
                                                }
                                            } else {
                                                String idN = stadioDao.getIDnumber(ec_studentnumber);
                                                if (idN != null) {
                                                    if (stadioDao.updateCrmIssuedStudentNumber(ec_studentnumber, idN) > 0) {
                                                        ID_NUMBER_ALREADY_EXISTS = true;
                                                        SRA_CRM_MESSAGE.append("\n - Student ID number exists, duplicate record.");
                                                        System.out.println("Saved CRM tags " + ec_studentnumber + " - " + id_number);
                                                    } else {
                                                        System.out.println("Unable to save CRM tags");
                                                    }
                                                }

                                                System.out.println("This qualification already exist");
                                                SRA_CRM_MESSAGE.append("\n - This qualification already exist");
                                                service_bus_error_log.append("\n" + formattedDateTime + ": " + " This qualification already exist");
                                            }
                                        } else {
                                            System.out.println("QUAL_FK CANNOT BE NULL " + qualfk);
                                            SRA_CRM_MESSAGE.append("\n - QUAL_FK CANNOT BE NULL");
                                        }
                                    }
                                } else {
                                    String OLD_EC_STUD_NO = ec_studentnumber;
                                    System.out.println(ec_studentnumber + " EC_STUDENT NUMBER has been overridden for ID number " + id_number);
                                    System.out.println(stadioDao.getSBSStudentNumber(id_number) + " EC_STUDENT NUMBER has been overridden to " + ec_studentnumber);
                                    ec_studentnumber = stadioDao.getSBSStudentNumber(id_number);
                                    SRA_CRM_MESSAGE.append("\n- " + ec_studentnumber + " EC_STUDENT NUMBER has been overridden with " + stadioDao.getSBSStudentNumber(id_number) + " as student number or ID number already exists.");
                                    if (stadioDao.getOldStudentNumberBoolean(id_number)) {
                                        String OLD_STUD_NR = stadioDao.getOldStudentNumber(id_number);
                                        String GAM = "gam create alias " + OLD_EC_STUD_NO + "@sbs.ac.za user " + OLD_STUD_NR + "@sbs.ac.za";
                                        Process process = Runtime.getRuntime().exec(GAM);
                                        process.waitFor();
                                        System.out.println("GAM Process Results " + process.toString());
                                        service_bus_audit_log.append("\n" + formattedDateTime + ": " + OLD_STUD_NR + "Process Results " + process.toString());
                                        System.out.println("OLD_STUDENT_NUM = " + OLD_STUD_NR);
                                        if (OLD_STUD_NR != null) {
                                            if (!ec_studentnumber.equalsIgnoreCase("") || !strCaptrureDate.equalsIgnoreCase("") || !CURRENT_SEMESTER.equalsIgnoreCase("")) {
                                                if (stadioDao.saveNote_v2(ec_studentnumber, strCaptrureDate, CURRENT_SEMESTER, OLD_STUD_NR) > 0) {
                                                    System.out.println("Note Saved successfully");
                                                    service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " Unable to Save Note");
                                                } else {
                                                    service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " Unable to Save Note");
                                                    System.out.println("Unable to Save Note");
                                                }
                                            } else {
                                                System.out.println("Could not insert into Note, notes fields cannot be empty.");
                                            }
                                        } else {
                                            System.out.println("SBS OLD Student number is null");
                                        }
                                        service_bus_audit_log.append("\n" + formattedDateTime + ": " + stadioStageDao.getOldStudentNumber(id_number).toString() + " - 6/7 Digit student number updated to new CRM student Number :" + ec_studentnumber);
                                        less_tha8_studNo_NO_identified = true;
                                        SRA_CRM_MESSAGE.append("\n- " + stadioStageDao.getOldStudentNumber(id_number).toString() + " - Is less than 8 digits, 6/7 Digit student number has been updated to a new CRM student Number" + ec_studentnumber);
                                        System.out.println("Student Data Updated into LIVE Database");
                                      /*  if (stadioDao.updateOldStudentNumber(OLD_EC_STUD_NO, id_number) > 0) {
                                            System.out.println("Student number updated for student ID number :" + id_number);
                                            System.out.println("6/7 Digit student number identified.");

                                        } else {
                                            System.out.println("Unable to update record");
                                            UNABLE_TO_UPDATE_RECORD = true;
                                            IT_CRM_MESSAGE.append("\n- Unable to update record  6/7 Digit student number identified." + ec_studentnumber);
                                        }*/

                                    } else {
                                        if (stadioDao.updateCrmIssuedStudentNumber(OLD_EC_STUD_NO, id_number) > 0) {
                                            ID_NUMBER_ALREADY_EXISTS = true;
                                            IT_CRM_MESSAGE.append("\n - Student ID number exists, duplicate record. updated CRM issued student number and CRM EXCL");
                                            System.out.println("Student ID number exists, duplicate record. updated CRM issued student number and CRM EXCL");
                                        } else {
                                            System.out.println("Unable to update CRM issued student number and CRM EXCL");
                                        }
                                    }
                                }
                            } else {
                                System.out.println("ID NUMBER LENGTH IS 0 or ID number not found");
                                SRA_CRM_MESSAGE.append("\n- ID NUMBER FOR EXISTING STUDENT or ID number not found" + ec_studentnumber + " STUDENT NUMBER'S ID NUMBER LENGTH IS 0");
                            }
                            System.out.println("complete_semester " + startSemester + semesterCycle);

                            ArrayList<Contact> contacts = (ArrayList<Contact>) stadioStageDao.getStudentContact(ec_studentnumber);
                            if (!contacts.isEmpty()) {
                                int count = 1;
                                for (Contact contact : contacts) {
                                    if (!stadioDao.getExistingStudentContact(contact.getStudent_pk(), contact.getContact_type_fk(), contact.getContact_country_fk(), contact.getContact_area_code(), contact.getContact_number())) {
                                        stadioDao.insertContactData(contact.getStudent_pk(), contact.getContact_type_fk(), contact.getContact_country_fk(), contact.getContact_area_code(), contact.getContact_number(),
                                                contact.getCapture_date());
                                        System.out.println("Contact type is :" + contact.getContact_type_fk());
                                        service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Contact Data Inserted LIVE Database");
                                        System.out.println("Student Contact Data " + count + " inserted into LIVE Database");
                                        //service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Contact Data Inserted LIVE Database");
                                    } else {
                                        service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " This Contact details already exists for this student");
                                        System.out.println("This contact details already exists for this student");
                                        CONTACT_ALREADY_EXISTS = true;
                                        SRA_CRM_MESSAGE.append("\n - This contact details already exists for this student - duplicate contacts for student.");
                                    }
                                    count++;
                                }

                            } else {
                                NO_CORRESPONDING_CONTACT = true;
                                SRA_CRM_MESSAGE.append("\n - Contact is empty - No corresponding contact data found");
                                System.out.println("Contact is empty - No corresponding contact data found");
                            }
                            ArrayList<Address> addresses = (ArrayList<Address>) stadioStageDao.getStudentAddress(ec_studentnumber);
                            if (!addresses.isEmpty()) {
                                for (Address address : addresses) {
                                    if (!stadioDao.getExistingStudentAddress(address.getStudent_fk(), address.getType_of_address_fk(), address.getPostcountry_fk(), address.getPost_province_fk(), address.getAddress_1(), address.getAddress_2(), address.getAddress_3(), address.getAddress_4(), address.getPostcode())) {
                                        System.out.println("Address.getType_of_address_fk is " + address.getType_of_address_fk());
                                        if(!stadioDao.getExistingStudentAddressPostal(address.getStudent_fk(), address.getType_of_address_fk())){
                                            stadioDao.insertStudentAddress(address.getStudent_fk(), address.getType_of_address_fk(), address.getPostcountry_fk(), address.getPost_province_fk(), address.getAddress_1(), address.getAddress_2(), address.getAddress_3(), address.getAddress_4(), address.getPostcode(), address.getCapture_date());
                                            System.out.println("Student Address Data inserted into LIVE Database");
                                            service_bus_audit_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " - Address Data Inserted LIVE Database");
                                        }
                                    } else {
                                        service_bus_error_log.append("\n" + formattedDateTime + ": " + ec_studentnumber + " This Address details already exists for this student");
                                        System.out.println("This Address details already exists for this student");
                                        ADDRESS_ALREADY_EXISTS = true;
                                        SRA_CRM_MESSAGE.append("\n - This Address details already exists for this student");
                                    }
                                }
                            } else {
                                //Notify SRA
                                System.out.println("No Corresponding Address or Address is empty");
                                NO_CORRESPONDING_ADDRESS = true;
                                SRA_CRM_MESSAGE.append("\n - No Corresponding Address or Address is empty");
                            }


                        } else {
                            System.out.println("Corresponding student number not found.");
                            service_bus_error_log.append("\n" + formattedDateTime + ": " + " Corresponding student number not found :Application student number: " + ec_studentnumber);
                            System.out.println("Students list is empty");
                            NO_CORRESPONDING_CONTACT = true;
                            IT_CRM_MESSAGE.append("\n - Corresponding student number not found");
                        }
                        String report = SRA_CRM_MESSAGE + "," + IT_CRM_MESSAGE;
                        service_bus_audit_trails.append("\n" + ec_studentnumber + "," + idNo + "," + qual_name + "," + report.replace("\n", ","));
                        service_bus_audit_trails.close();

                    } else {
                        service_bus_error_log.append("\n" + formattedDateTime + ": " + GLOBAL_ID_NUMBER + " Student number is missing from JSON file");
                        MISSING_STUD_NO = true;
                        IT_CRM_MESSAGE.append("\n - Student number is missing from JSON file");
                        System.out.println("Student number is missing");
                    }
                   /* if (NEW_APPLICATION_ARRIVED) {
                        if (!SRA_EMAIL_ADDRESS.isEmpty()) {
                            mailSender.sendMailForExistingStudentToOneSRA(ec_studentnumber, SRA_EMAIL_ADDRESS, SRA_CRM_MESSAGE.toString());
                        } else {
                            SRA_EMAIL_ADDRESS = "crm-sra@sbs.ac.za";
                            mailSender.sendMailForExistingStudentToOneSRA(ec_studentnumber, SRA_EMAIL_ADDRESS, SRA_CRM_MESSAGE.toString());
                        }
                    }*/
                    //if (DUP_STUD_ID_FOUND || less_tha8_studNo_NO_identified || MISSING_STUD_NO || NO_CORRESPONDING_ADDRESS || NO_CORRESPONDING_CONTACT || ADDRESS_ALREADY_EXISTS || CONTACT_ALREADY_EXISTS || ID_NUMBER_ALREADY_EXISTS || DUPLICATE_STUD_NO || MISSING_START_SEM || MISSING_SEM_CYCLE || QUAL_ALREADY_EXISTS || QUAL_HAS_NO_MAJOR || MAJOR_DOES_NOT_EXIST) {
                        if (!SRA_EMAIL_ADDRESS.isEmpty()) {
                           // mailSender.sendMailForExistingStudentToOneSRA(ec_studentnumber+"/"+stadioStageDao.getLocalIDnumber(ec_studentnumber), SRA_EMAIL_ADDRESS, SRA_CRM_MESSAGE.toString());
                        } else {
                            SRA_EMAIL_ADDRESS = "crm-sra@sbs.ac.za";
                            mailSender.sendMailForExistingStudentToOneSRA(ec_studentnumber+"/"+stadioStageDao.getLocalIDnumber(ec_studentnumber), SRA_EMAIL_ADDRESS, SRA_CRM_MESSAGE.toString());
                        }
                    //}
                    if (UNABLE_TO_UPDATE_RECORD) {
                        if (!SRA_EMAIL_ADDRESS.isEmpty()) {
                           // mailSender.sendMailToIT(ec_studentnumber+"/"+stadioStageDao.getLocalIDnumber(ec_studentnumber), SRA_CRM_MESSAGE.toString());
                        } else {
                            SRA_EMAIL_ADDRESS = "crm-sra@sbs.ac.za";
                            mailSender.sendMailToIT(ec_studentnumber+"/"+stadioStageDao.getLocalIDnumber(ec_studentnumber), SRA_CRM_MESSAGE.toString());
                        }
                    }
                    System.out.println("Data Type is :" + data_type);
                    Thread.sleep(100);
                    System.out.println("Thread is waiting 10 sec to close database");
                    stadioDao.closeLIVEConnections();
                    stadioStageDao.closeLocalConnections();
                } else {
                    System.out.println("Issue incountered, unknown Data type encountered.....");
                    //mailSender.sendMailToIT(GLOBAL_ID_NUMBER, " \n Issue incountered, unknown Data type encountered");
                }

            } else {
                System.out.println("Data Type Field is missing from main JSON file.........");
                //mailSender.sendMailToIT(GLOBAL_ID_NUMBER, GLOBAL_ID_NUMBER + "  Data Type Field is missing from main JSON file");
                service_bus_error_log.append("\n" + formattedDateTime + ": Data Type Field is missing from main JSON file");
            }

        } catch (Exception e) {
            //mailSender.sendMailToIT("", e.getMessage());
            service_bus_error_log.append("\n" + formattedDateTime + ": Error occured: " + e.getMessage() + ", caused by " + e.getCause());
            e.printStackTrace();
        } finally {
            System.out.println("Finally closing the database connections");
            try {
                service_bus_audit_log.close();
                service_bus_error_log.close();
                stadioDao.closeLIVEConnections();
                stadioStageDao.closeLocalConnections();
            } catch (Exception e) {

            }
        }
    }
}



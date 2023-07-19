package com.stadio.mail;

import com.stadio.daos.StadioStageDao;
import com.stadio.models.Stadio_SRA;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;

public class MailSender {
    String from = "noreply1@sbs.ac.za";
    String host = "smtp.gmail.com";

    public void sendMailToSRA(String studentNumberOrIdNumber,String email_address) {
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");//587//465
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("noreply1@sbs.ac.za", "sens--11");
            }
        });
        // Used to debug SMTP issues
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email_address));
            // Set Subject: header field
            message.setSubject("Service Bus Message Error :"+studentNumberOrIdNumber);
            //message.setText("Dear Admin ,"+"\n"+fname+" "+lname+" ("+studentNumber+")"+"\nRegistered for :"+qual +", please complete the registration in SBS Data Gateqay");

            // Now set the actual message
            message.setText("Dear Admin,"+"\nThis is CRM notification message, see details below."+
                    "\nStudent Number/ID Number: "+studentNumberOrIdNumber);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (Exception mex) {
            mex.printStackTrace();
        }
        finally {

        }
    }

    public void sendMail(String studentNumberOrIdNumber) {
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");//587//465
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("noreply1@sbs.ac.za", "sens--11");
            }
        });
        // Used to debug SMTP issues
        //session.setDebug(true);
        try {
            StadioStageDao stadioStageDao = new StadioStageDao();
            ArrayList<Stadio_SRA> stadio_sras = (ArrayList<Stadio_SRA>) stadioStageDao.getSRAs();
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            for(Stadio_SRA stadio_SRAS:stadio_sras){
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(stadio_SRAS.getEmail()));
            }
            // Set Subject: header field
            message.setSubject("Service Bus Message Error :"+studentNumberOrIdNumber);
            //message.setText("Dear Admin ,"+"\n"+fname+" "+lname+" ("+studentNumber+")"+"\nRegistered for :"+qual +", please complete the registration in SBS Data Gateqay");

            // Now set the actual message
            message.setText("Dear Admin,"+"\nThis is a CRM notification message, see details below."+
            "\nStudent Number/ID Number: "+studentNumberOrIdNumber);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
            stadioStageDao.closeLocalConnections();
        } catch (Exception mex) {
            mex.printStackTrace();
        }
        finally {

        }
    }
    public void sendMail(String fname,String lname,String qual,String studentNumber) {
        // Recipient's email ID needs to be mentioned.

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");//587//465
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("noreply1@sbs.ac.za", "sens--11");
            }
        });
        // Used to debug SMTP issues
        //session.setDebug(true);
        try {
            StadioStageDao stadioStageDao = new StadioStageDao();
            ArrayList<Stadio_SRA> stadio_sras = (ArrayList<Stadio_SRA>) stadioStageDao.getSRAs();
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            for(Stadio_SRA stadio_SRAS:stadio_sras){
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(stadio_SRAS.getEmail()));
            }
            // Set Subject: header field
            message.setSubject("CRM Notification Message :"+studentNumber);
            message.setText("Dear Admin ,"+"\n"+fname+" "+lname+" ("+studentNumber+")"+"\nRegistered for :"+qual +", please complete the registration in SBS Data Gateway");
            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
            stadioStageDao.closeLocalConnections();
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }
    public void sendMailToIT(String idNo,String text) {
        // Recipient's email ID needs to be mentioned.

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");//587//465
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("noreply1@sbs.ac.za", "sens--11");
            }
        });
        // Used to debug SMTP issues
        //session.setDebug(true);
        try {
            StadioStageDao stadioStageDao = new StadioStageDao();
            String itEmail = stadioStageDao.getItEmailAddress();
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(itEmail));
            // Set Subject: header field
            message.setSubject("CRM Student ");
           // message.setText("Dear IT ,"+"\n"+idNo+ " Has applied through CRM but their details already exists in SBS Database,"+" \nPlease complete the registration in SBS Data Gateway");
            message.setText("Dear IT ,"+"\n"+ text);
            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
            stadioStageDao.closeLocalConnections();
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }
    public void sendMailForExistingStudentToOneSRA(String StudOrIdNo,String email_address,String text) {
        // Recipient's email ID needs to be mentioned.

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");//587//465
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("noreply1@sbs.ac.za", "sens--11");
            }
        });
        // Used to debug SMTP issues
        //session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email_address));
            // Set Subject: header field
            message.setSubject("CRM Student "+StudOrIdNo);
            message.setText("Dear Admin ,"+  text);
            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }
    public void sendMailForExistingStudentToAGroup(String idNo,String email_address) {
        // Recipient's email ID needs to be mentioned.

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");//587//465
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("noreply1@sbs.ac.za", "sens--11");
            }
        });
        // Used to debug SMTP issues
        //session.setDebug(true);
        try {
            StadioStageDao stadioStageDao = new StadioStageDao();
            String sraGroupEmail = stadioStageDao.getGroupSRAEmail();
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(sraGroupEmail));
            // Set Subject: header field
            message.setSubject("CRM Student ID Number Already Exists :"+idNo);
            message.setText("Dear Admin ,"+"\n"+idNo+ " Has applied through CRM but their details already exists in SBS Database,"+" \nPlease check the details in SBS Data Gateway");
            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }
}

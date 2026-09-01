package com.sunrise.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private static final String SENDER_EMAIL = "yohanj0204@gmail.com";
    private static final String APP_PASSWORD = "qitfgmvuhdzyrpax";

    public static boolean sendAppointmentConfirmation(String recipientEmail, int appointmentId, String patientName,
                                                      String dentistName, String treatmentType,
                                                      String appointmentDate, String appointmentTime) {

        if (recipientEmail == null || recipientEmail.isEmpty()) {
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, "Sunrise Dental Clinic"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Appointment Confirmation - Sunrise Dental Clinic");

            String htmlBody = buildEmailHtml(appointmentId, patientName, dentistName, treatmentType, appointmentDate, appointmentTime);

            message.setContent(htmlBody, "text/html; charset=utf-8");

            Transport.send(message);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String buildEmailHtml(int appointmentId, String patientName, String dentistName,
                                         String treatmentType, String appointmentDate, String appointmentTime) {

        return "<html><body style='margin:0; padding:0; background-color:#F4F8FC; font-family:Arial, sans-serif;'>"
                + "<table width='100%' cellpadding='0' cellspacing='0' style='background-color:#F4F8FC; padding:30px 0;'>"
                + "<tr><td align='center'>"
                + "<table width='500' cellpadding='0' cellspacing='0' style='background-color:#FFFFFF; border-radius:10px; overflow:hidden; box-shadow:0 2px 10px rgba(22,58,95,0.1);'>"

                + "<tr><td style='background-color:#163A5F; padding:24px; text-align:center;'>"
                + "<span style='color:#FFFFFF; font-size:20px; font-weight:bold;'>Sunrise Dental Clinic</span>"
                + "</td></tr>"

                + "<tr><td style='padding:30px;'>"
                + "<p style='font-size:15px; color:#172033; margin-top:0;'>Dear <strong>" + patientName + "</strong>,</p>"
                + "<p style='font-size:14px; color:#172033;'>Your appointment has been successfully booked. Here are your details:</p>"

                + "<table width='100%' cellpadding='8' cellspacing='0' style='margin-top:15px; background-color:#F4F8FC; border-radius:8px;'>"
                + "<tr><td style='font-size:13px; color:#64748B; width:40%;'>Appointment Number</td><td style='font-size:14px; color:#163A5F; font-weight:bold;'>#" + appointmentId + "</td></tr>"
                + "<tr><td style='font-size:13px; color:#64748B;'>Dentist</td><td style='font-size:14px; color:#172033;'>" + dentistName + "</td></tr>"
                + "<tr><td style='font-size:13px; color:#64748B;'>Treatment</td><td style='font-size:14px; color:#172033;'>" + treatmentType + "</td></tr>"
                + "<tr><td style='font-size:13px; color:#64748B;'>Date</td><td style='font-size:14px; color:#172033;'>" + appointmentDate + "</td></tr>"
                + "<tr><td style='font-size:13px; color:#64748B;'>Time</td><td style='font-size:14px; color:#172033;'>" + appointmentTime + "</td></tr>"
                + "</table>"

                + "<p style='font-size:13px; color:#64748B; margin-top:20px;'>Please quote your appointment number for any future reference, and arrive 10 minutes early.</p>"

                + "<p style='font-size:13px; color:#172033; margin-top:25px;'>Thank you for choosing Sunrise Dental Clinic.</p>"
                + "</td></tr>"

                + "<tr><td style='background-color:#F4F8FC; padding:16px; text-align:center;'>"
                + "<span style='font-size:11px; color:#64748B;'>&copy; Sunrise Dental Clinic — Colombo, Sri Lanka</span>"
                + "</td></tr>"

                + "</table>"
                + "</td></tr>"
                + "</table>"
                + "</body></html>";
    }
}


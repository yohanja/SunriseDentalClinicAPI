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
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Appointment Confirmation - Sunrise Dental Clinic");

            String body = "Dear " + patientName + ",\n\n"
                    + "Your appointment has been successfully booked.\n\n"
                    + "Appointment Number: " + appointmentId + "\n"
                    + "Dentist: " + dentistName + "\n"
                    + "Treatment: " + treatmentType + "\n"
                    + "Date: " + appointmentDate + "\n"
                    + "Time: " + appointmentTime + "\n\n"
                    + "Please quote your Appointment Number for any future reference.\n"
                    + "Please arrive 10 minutes early.\n\n"
                    + "Thank you for choosing Sunrise Dental Clinic.";

            message.setText(body);

            Transport.send(message);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

}
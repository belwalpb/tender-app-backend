package com.tender.utils;
import com.tender.constants.TenderConstants;
import com.tender.entity.TempUser;
import com.tender.entity.User;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtils {
    private EmailUtils() {}

    private static void sendEmail(String msg, String subject, String recepient) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(TenderConstants.EMAIL_ACCOUNT, TenderConstants.EMAIL_PASSWORD);
            }
        });
        // Used to debug SMTP issues
        session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(TenderConstants.EMAIL_ACCOUNT));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject(subject);
            message.setText(msg);
            //send message
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (MessagingException e) { e.printStackTrace();;}
    }

    public static void sendLoginOtp(String otp, User user) {
        String subject = "Login Otp For Tender Application";
        String message = "Dear "+ user.getName() + ", Your Login Otp Is : "+otp + ". Kindly do not share it with anyone";
        sendEmail(message,subject,user.getEmail());
    }

    public static void sendSignUpOtp(String otp, TempUser user) {
        String subject = "Sign Up Otp For Tender Application";
        String message = "Dear "+ user.getName() + ", Your SIgn Up Otp Is : "+otp + ". Kindly do not share it with anyone";
        sendEmail(message,subject,user.getEmail());
    }
}

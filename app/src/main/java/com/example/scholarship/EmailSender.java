package com.example.scholarship;

import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    public static void sendOtp(String toEmail, String otp) {
        final String fromEmail = "chatt2440@gmail.com"; // ✅ Use your Gmail
        final String password = "badi xfvz gxjg kcdo";     // ✅ App Password, not regular password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
        props.put("mail.smtp.port", "587");            // TLS Port
        props.put("mail.smtp.auth", "true");           // Enable authentication
        props.put("mail.smtp.starttls.enable", "true");// Enable STARTTLS

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });
        session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "scholarship"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your OTP Code");
            message.setText("Your verification code is: " + otp);

            Transport.send(message);
            System.out.println("✅ OTP email successfully sent to: " + toEmail);

        } catch (Exception e) {
            Log.e("EMAIL", "Failed to send OTP: " + e.getMessage(), e);
        }

    }
}

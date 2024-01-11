package com.ElectricityAutomationInitiative.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    private static final int OTP_LENGTH = 6;

    public void sendRegistrationEmail(String recipientEmail, String username,String CustomerId) {
        String subject = "Registration Successful";
        String message = "Dear " + username + ",\n\nThank you for registering with our electricity automation initiative. "
                + "We appreciate your interest and support\n Your Unique Customer Id is "+CustomerId+ " !\n\nBest regards,\nThe Electricity Automation Team ";

        sendEmail(recipientEmail, subject, message);
    }
   private String otp;
    public String sendOTP(String recipientEmail) {
          otp = generateOTP();

        String subject = "OTP Verification";
        String message = "Dear User,\n\nYour OTP for verification is: "
                + otp + "\n\nPlease enter this OTP to complete your registration.\n" +
                "\nBest regards,\nThe Electricity Automation Team";

        sendEmail(recipientEmail, subject, message);
        return otp;
    }

    private void sendEmail(String recipientEmail, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText(message);
        javaMailSender.send(email);
    }
    public void sendConnectionApprovedEmail(String recipientEmail, String username, String connectionId) {
        String subject = "Connection Approved";
        String message = "Dear " + username + ",\n\nWe are pleased to inform you that your electricity connection (ID: " + connectionId + ") has been approved."
                + " You can now enjoy our electricity services.\n\nBest regards,\nThe Electricity Automation Team ";

        sendEmail(recipientEmail, subject, message);
    }
    public void sendConnectionRejectionEmail(String recipientEmail, String username, String reason) {
        String subject = "Connection Rejected";
        String message = "Dear " + username + ",\n\nWe regret to inform you that your electricity connection request has been rejected."
                + " Unfortunately, we are unable to install the connection at the provided address due to the following reason:\n"
                + reason + "\n\nPlease feel free to contact us for further assistance, and we will work together to resolve the issue and provide you with electricity services in the future.\n\nBest regards,\nThe Electricity Automation Team ";

        sendEmail(recipientEmail, subject, message);
    }
    public String sendPasswordResetEmail(String recipientEmail) {
        // Create a SimpleMailMessage
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        String otp = generateOTP();
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("Your OTP for password reset is: " + otp);

        // Send the email
        javaMailSender.send(mailMessage);
        return otp;
    }

    private String generateOTP() {
        String digits = "0123456789";
        Random random = new Random();
        StringBuilder otpBuilder = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            int randomIndex = random.nextInt(digits.length());
            char digit = digits.charAt(randomIndex);
            otpBuilder.append(digit);
        }
        return otpBuilder.toString();
    }

}


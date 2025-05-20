package com.projet.locationvoiture.mail;

import com.projet.locationvoiture.entity.EmailDetails;
import com.projet.locationvoiture.entity.User;

public interface EmailService {
    void sendVerificationEmail(User user);
    String sendSimpleMail(String to, String subject, String text);
    String sendMailWithAttachment(EmailDetails details);
    public void sendPasswordResetEmail(User user, String resetToken);
}

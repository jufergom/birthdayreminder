package com.gruposcout21.birthdayreminder.service;

import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPlainText(List<String> to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to.toArray(String[]::new));
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendHtml(List<String> to, String subject, String htmlBody, Map<String, Resource> inlineResources) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to.toArray(String[]::new));
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        if (inlineResources != null) {
            for (Map.Entry<String, Resource> entry : inlineResources.entrySet()) {
                helper.addInline(entry.getKey(), entry.getValue());
            }
        }

        mailSender.send(message);
    }
}

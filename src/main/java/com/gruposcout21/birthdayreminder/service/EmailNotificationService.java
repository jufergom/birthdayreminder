package com.gruposcout21.birthdayreminder.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.gruposcout21.birthdayreminder.entity.Contact;
import com.gruposcout21.birthdayreminder.entity.Person;
import com.gruposcout21.birthdayreminder.repository.ContactRepository;

import jakarta.mail.MessagingException;

@Service("emailNotificationService")
public class EmailNotificationService implements NotificationService {

    private ContactRepository contactRepository;

    private TemplateService templateService;

    private MailService mailService;

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

    public EmailNotificationService(ContactRepository contactRepository, TemplateService templateService, MailService mailService) {
        this.contactRepository = contactRepository;
        this.templateService = templateService;
        this.mailService = mailService;
    }

    @Override
    public void notifyBirthdays(List<Person> birthdayPersons) {
        LocalDate today = LocalDate.now();
        Locale spanishLocale = Locale.of("es", "ES");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "EEEE d 'de' MMMM 'del' yyyy", spanishLocale
        );

        String spanishFormattedTodayDate = today.format(formatter);
        
        Map<String, Object> variables = Map.of(
            "birthdayPersons", birthdayPersons,
            "today", spanishFormattedTodayDate
        );
        String htmlEmailBody = templateService.renderTemplate("birthday-reminder.html", variables);

        List<Contact> contacts = contactRepository.findAll();
        List<String> contactEmails = contacts.stream()
            .map(Contact::getEmail)
            .toList();

        Map<String, Resource> resources = new HashMap<>();
        resources.put("gs21-logo", new ClassPathResource("static/images/logo-gs21.jpg"));

        try {
            mailService.sendHtml(contactEmails, "Cumpleaños Clan Atlantis hoy " + spanishFormattedTodayDate, htmlEmailBody, resources);
        }
        catch (MessagingException e) {
            logger.error(e.getMessage(), e);
        }
    }

}

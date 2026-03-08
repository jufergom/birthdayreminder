package com.gruposcout21.birthdayreminder.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gruposcout21.birthdayreminder.entity.Contact;
import com.gruposcout21.birthdayreminder.entity.Person;
import com.gruposcout21.birthdayreminder.repository.ContactRepository;
import com.gruposcout21.birthdayreminder.repository.PersonRepository;

import jakarta.mail.MessagingException;

@Service
public class BirthdayServiceImpl implements BirthdayService {

    private PersonRepository personRepository;

    private ContactRepository contactRepository;

    private TemplateService templateService;

    private MailService mailService;

    private static final Logger logger = LoggerFactory.getLogger(BirthdayServiceImpl.class);

    public BirthdayServiceImpl(PersonRepository personRepository, ContactRepository contactRepository, TemplateService templateService, MailService mailService) {
        this.personRepository = personRepository;
        this.contactRepository = contactRepository;
        this.templateService = templateService;
        this.mailService = mailService;
    }

    @Override
    public void sendBirthdayReminders() {
        Locale spanishLocale = Locale.of("es", "ES");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "EEEE d 'de' MMMM 'del' yyyy", spanishLocale
        );

        LocalDate today = LocalDate.now();
        String spanishFormattedTodayDate = today.format(formatter);
        List<Person> birthdayPersons = personRepository.findByBirthday(today.getMonthValue(), today.getDayOfMonth());

        if (birthdayPersons.isEmpty()) {
            return;
        }

        Map<String, Object> variables = Map.of(
            "birthdayPersons", birthdayPersons,
            "today", spanishFormattedTodayDate
        );
        String htmlEmailBody = templateService.renderTemplate("birthday-reminder.html", variables);

        List<Contact> contacts = contactRepository.findAll();
        List<String> contactEmails = contacts.stream()
            .map(Contact::getEmail)
            .toList();

        try {
            mailService.sendHtml(contactEmails, "Cumpleaños Clan Atlantis hoy " + spanishFormattedTodayDate, htmlEmailBody);
        }
        catch (MessagingException e) {
            logger.error(e.getMessage(), e);
        }

    }
}

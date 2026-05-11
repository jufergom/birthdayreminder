package com.gruposcout21.birthdayreminder.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.gruposcout21.birthdayreminder.entity.Person;
import com.gruposcout21.birthdayreminder.repository.PersonRepository;

@Service
public class BirthdayServiceImpl implements BirthdayService {

    private PersonRepository personRepository;

    private NotificationService notificationService;

    public BirthdayServiceImpl(PersonRepository personRepository, @Qualifier("discordNotificationService") NotificationService notificationService) {
        this.personRepository = personRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void notifyTodayBirthdays() {
        LocalDate today = LocalDate.now();
        List<Person> birthdayPersons = personRepository.findByBirthday(today.getMonthValue(), today.getDayOfMonth());
        birthdayPersons.add(new Person("El chavo del 8", LocalDate.of(1998, today.getMonthValue(), today.getDayOfMonth())));
        if (birthdayPersons.isEmpty()) {
            return;
        }

        notificationService.notifyBirthdays(birthdayPersons);
    }
}

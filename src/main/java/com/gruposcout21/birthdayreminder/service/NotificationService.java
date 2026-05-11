package com.gruposcout21.birthdayreminder.service;

import java.util.List;

import com.gruposcout21.birthdayreminder.entity.Person;

public interface NotificationService {
    void notifyBirthdays(List<Person> birthdayPersons);
}

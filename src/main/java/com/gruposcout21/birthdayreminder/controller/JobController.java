package com.gruposcout21.birthdayreminder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gruposcout21.birthdayreminder.service.BirthdayService;

@RestController
public class JobController {

    private BirthdayService birthdayService;

    public JobController(BirthdayService birthdayService) {
        this.birthdayService = birthdayService;
    }

    @PostMapping("/jobs/birthday-reminder")
    public ResponseEntity<Void> sendBirthdayReminder() {
        birthdayService.sendBirthdayReminders();
        return ResponseEntity.ok().build();
    }

}

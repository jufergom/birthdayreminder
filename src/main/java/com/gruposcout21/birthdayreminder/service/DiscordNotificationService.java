package com.gruposcout21.birthdayreminder.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.gruposcout21.birthdayreminder.discord.dto.Embed;
import com.gruposcout21.birthdayreminder.discord.dto.Field;
import com.gruposcout21.birthdayreminder.discord.dto.WebhookRequest;
import com.gruposcout21.birthdayreminder.entity.Person;

@Service("discordNotificationService")
public class DiscordNotificationService implements NotificationService {

    private final RestClient restClient;

    private static final Logger logger = LoggerFactory.getLogger(DiscordNotificationService.class);

    public DiscordNotificationService(RestClient.Builder restClientBuilder,
            @Value("${discord.webhook.url}") String webhookUrl) {
        this.restClient = restClientBuilder.baseUrl(webhookUrl).build();
    }

    @Override
    public void notifyBirthdays(List<Person> birthdayPersons) {
        LocalDate today = LocalDate.now();
        Locale spanishLocale = Locale.of("es", "ES");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "EEEE d 'de' MMMM 'del' yyyy", spanishLocale
        );

        String spanishFormattedTodayDate = today.format(formatter);

        List<Field> fields = birthdayPersons.stream()
            .map(person -> new Field(person.getName(), String.format("%d años", person.getAge()), true))
            .toList();
        List<Embed> embeds = new ArrayList<>(){
            {
                add(new Embed(
                    String.format("🎉 Cumpleaños Clan Atlantis hoy %s", spanishFormattedTodayDate),
                    "15258703",
                    fields
                ));
            }
        };
        WebhookRequest discordWebhookRequest = new WebhookRequest(embeds);

        ResponseEntity<Void> response = restClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(discordWebhookRequest)
                .retrieve()
                .toBodilessEntity();
        
        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.error("Error sending notification to Discord: " + response.getStatusCode());
        }
    }
}

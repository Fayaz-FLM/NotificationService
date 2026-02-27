package com.medsync.notification.service.impl;

import com.medsync.notification.config.SendGridProperties;
import com.medsync.notification.dto.EmailRequest;
import com.medsync.notification.service.NotificationService;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridNotificationService implements NotificationService {

    private final SendGridProperties properties;

    public SendGridNotificationService(SendGridProperties properties) {
        this.properties = properties;
    }

    @Override
    @Async
    public void sendEmail(EmailRequest requestDto) {

        Email from = new Email(
                properties.getFromEmail(),
                properties.getFromName()
        );

        Email to = new Email(requestDto.getTo());

        Content content = new Content("text/html", requestDto.getBody());

        Mail mail = new Mail(from, requestDto.getSubject(), to, content);

        SendGrid sendGrid = new SendGrid(properties.getApiKey());

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            if (response.getStatusCode() != 202) {
                throw new RuntimeException("Failed to send email. Status Code: "
                        + response.getStatusCode());
            }

        } catch (IOException e) {
            throw new RuntimeException("SendGrid Error: " + e.getMessage());
        }
    }
}
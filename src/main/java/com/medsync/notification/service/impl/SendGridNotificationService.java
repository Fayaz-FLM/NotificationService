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
    public void sendEmail(EmailRequest requestDto) {

        System.out.println("=== SendGridNotificationService: Starting email send ===");
        System.out.println("To: " + requestDto.getTo());
        System.out.println("Subject: " + requestDto.getSubject());
        System.out.println("SendGrid API Key configured: " + (properties.getApiKey() != null && !properties.getApiKey().isEmpty()));
        System.out.println("SendGrid API Key (first 10 chars): " + (properties.getApiKey() != null ? properties.getApiKey().substring(0, Math.min(10, properties.getApiKey().length())) : "null"));
        System.out.println("From Email: " + properties.getFromEmail());
        System.out.println("From Name: " + properties.getFromName());

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

            System.out.println("=== SendGrid Response ===");
            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
            System.out.println("Response Headers: " + response.getHeaders());

            if (response.getStatusCode() != 202) {
                String errorMsg = "Failed to send email. Status Code: " + response.getStatusCode() + ", Body: " + response.getBody();
                System.err.println(errorMsg);
                throw new RuntimeException(errorMsg);
            }
            
            System.out.println("=== Email sent successfully via SendGrid to: " + requestDto.getTo() + " ===");

        } catch (IOException e) {
            System.err.println("=== SendGrid IOException ===");
            System.err.println("Error Message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("SendGrid Error: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("=== Unexpected Exception in SendGrid ===");
            System.err.println("Error Message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Unexpected error sending email: " + e.getMessage(), e);
        }
    }
}
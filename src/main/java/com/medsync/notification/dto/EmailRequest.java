package com.medsync.notification.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailRequest {

    @Email
    @NotBlank
    private String to;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;
    
    public EmailRequest(@Email @NotBlank String to, @NotBlank String subject, @NotBlank String body) {
		super();
		this.to = to;
		this.subject = subject;
		this.body = body;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

    
}
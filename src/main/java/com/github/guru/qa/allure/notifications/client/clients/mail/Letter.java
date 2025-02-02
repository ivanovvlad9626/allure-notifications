package com.github.guru.qa.allure.notifications.client.clients.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.github.guru.qa.allure.notifications.utils.MailSettingsHelper.mailTo;
import static com.github.guru.qa.allure.notifications.utils.MailUtils.parseRecipientsFromString;

public class Letter {
    private final Logger LOG = LoggerFactory.getLogger(Letter.class);
    private final LetterBody body = new LetterBody();
    private final Message letter;

    public Letter(Session session) throws MessagingException {
        letter = new MimeMessage(session);
    }

    public Letter from(String from) throws MessagingException {
        if (!from.isEmpty()) {
            letter.setFrom(new InternetAddress(from));
        }
        return this;
    }

    public Letter to(String to) throws MessagingException {
        letter.setRecipients(Message.RecipientType.TO, parseRecipientsFromString(to));
        return this;
    }

    public Letter subject(String subject) throws MessagingException {
        letter.setSubject(subject);
        return this;
    }

    public Letter text(String content) throws MessagingException {
        body.addText(content);
        return this;
    }

    public Letter image(String imagePath) throws MessagingException {
        body.addImage(imagePath);
        return this;
    }

    public void send() throws MessagingException {
        letter.setContent(body.getMimeMultipart());
        LOG.info("Trying to send mail...");
        Transport.send(letter);
        LOG.info("Mail sent to {}", mailTo());
    }
}

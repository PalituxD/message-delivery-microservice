package org.justfoolingaround.apps.messagedelivery.services.senders.impl;

import lombok.extern.slf4j.Slf4j;
import org.justfoolingaround.apps.messagedelivery.domain.enums.MessageType;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;
import org.justfoolingaround.apps.messagedelivery.services.senders.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("emailSenderService")
@Slf4j
public class EmailSenderService implements SenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void send(Message message) {
        sendMessage(message);
    }

    @Override
    public MessageType getSupportedMessageType() {
        return MessageType.E_MAIL;
    }

    private void sendMessage(Message message) {

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(message.getTo());
        email.setSubject(message.getSubject());
        email.setText(message.getContent());
        email.setFrom(message.getFrom());
        email.setReplyTo(message.getReplyTo());

        log.debug("Sending the email: {}", email);

        mailSender.send(email);
    }
}

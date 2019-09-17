package org.justfoolingaround.apps.messagedelivery.services.senders.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.justfoolingaround.apps.messagedelivery.domain.enums.MessageType;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;
import org.justfoolingaround.apps.messagedelivery.rules.SmtpServerRule;
import org.justfoolingaround.apps.messagedelivery.services.senders.SenderService;
import org.justfoolingaround.apps.messagedelivery.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EmailSenderServiceTest {

    @Autowired
    @Qualifier("emailSenderService")
    private SenderService service;

    @Autowired
    @Rule
    public SmtpServerRule smtpServerRule;

    @Test
    public void send() throws IOException, MessagingException {

        final String CONTENT = Util.randomValue();

        Message message = create();

        message.setContent(CONTENT);

        service.send(message);

        Assert.assertTrue(smtpServerRule.getMessages().stream().findFirst().isPresent());

        String messageContent = String.valueOf(smtpServerRule.getMessages().stream().findFirst().get().getContent()).trim();

        Assert.assertTrue(messageContent.equalsIgnoreCase(CONTENT));
    }

    @Test
    public void getSupportedMessageType() {

        Assert.assertEquals(MessageType.E_MAIL, service.getSupportedMessageType());
    }

    private Message create() {
        return create(BigInteger.ZERO.intValue(), Util.randomValue());
    }

    private Message create(int hoursForSchedule, String codeReference) {

        Message message = Util.generateMessage();
        message.setCodeReference(codeReference);
        message.setScheduledDeliveryDate(Calendar.getInstance().getTime());
        message.setScheduledDeliveryDate(Util.increaseTimeInHours(message.getScheduledDeliveryDate(), hoursForSchedule));

        log.info("create::: Message: {}", message);

        return message;
    }
}
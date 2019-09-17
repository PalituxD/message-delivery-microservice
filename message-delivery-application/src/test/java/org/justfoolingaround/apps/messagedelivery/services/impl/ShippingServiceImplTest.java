package org.justfoolingaround.apps.messagedelivery.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.justfoolingaround.apps.messagedelivery.domain.enums.MessageType;
import org.justfoolingaround.apps.messagedelivery.domain.operations.MessageOperations;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;
import org.justfoolingaround.apps.messagedelivery.rules.SmtpServerRule;
import org.justfoolingaround.apps.messagedelivery.services.ShippingService;
import org.justfoolingaround.apps.messagedelivery.utils.Util;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShippingServiceImplTest {

    @Autowired
    private ShippingService service;

    @MockBean
    private MessageOperations messageOperations;

    @Autowired
    @Rule
    public SmtpServerRule smtpServerRule;

    @Before
    public void init() {

        Mockito.doNothing().when(messageOperations).update(Mockito.any());
    }

    @Test
    public void send() {

        Message badMessage = generate();
        badMessage.setType(null);

        List<Message> messages = Arrays.asList(generate(), generate(), generate(), badMessage);

        int numberOfSentMessages = service.send(messages);

        Assert.assertTrue(numberOfSentMessages < messages.size());
    }

    private Message generate() {

        String key = Util.generateKey();
        Message message = Message.builder().build();

        message.setPayloadId("PAYLOAD" + key);
        message.setSource("SOURCE" + key);
        message.setType(MessageType.E_MAIL);

        message.setContent("Content" + key);
        message.setFrom("FROM" + key);
        message.setReplyTo("REPLYTO" + key);

        message.setScheduledDeliveryDate(Calendar.getInstance().getTime());

        message.setSubject("SUBJECT" + key);
        message.setTo("EMAIL" + key + "@GMAIL.COM");

        message.setId(null);
        return message;
    }
}
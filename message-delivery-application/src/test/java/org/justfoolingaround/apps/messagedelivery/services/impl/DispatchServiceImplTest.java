package org.justfoolingaround.apps.messagedelivery.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Batch;
import org.justfoolingaround.apps.messagedelivery.rules.SmtpServerRule;
import org.justfoolingaround.apps.messagedelivery.services.DispatchService;
import org.justfoolingaround.apps.messagedelivery.services.PostalService;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.justfoolingaround.apps.messagedelivery.utils.Util.generateKey;
import static org.justfoolingaround.apps.messagedelivery.utils.Util.generateMessage;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
public class DispatchServiceImplTest {

    private final static int MAX_NUMBER_OF_MESSAGES = 100;

    @Autowired
    private DispatchService service;

    @Autowired
    private PostalService postalService;

    @Autowired
    @Rule
    public SmtpServerRule smtpServerRule;

    @Test
    public void dispatch() {

        final int numberOfMessages = RandomUtils.nextInt(MAX_NUMBER_OF_MESSAGES);

        log.info("dispatch::: generating {} messages", numberOfMessages);

        for (int index = 0; index < numberOfMessages; index++) {

            generateAndReceiveMessage(generateKey());
        }

        service.dispatch();

        Assert.assertTrue(service.getPendingBatches().isEmpty());

        List<Batch> pendingBatches = service.getPendingBatches();

        Assert.assertTrue(pendingBatches.isEmpty());
    }

    @Test
    public void dispatchNoPendingMessages() {

        service.dispatch();

        Assert.assertTrue(service.getPendingBatches().isEmpty());

        service.dispatchPendingMessages();

        List<Batch> pendingBatches = service.getPendingBatches();

        Assert.assertTrue(pendingBatches.isEmpty());
    }

    @Test
    public void dispatchPendingMessages() {

        final int numberOfMessages = RandomUtils.nextInt(MAX_NUMBER_OF_MESSAGES);

        log.info("dispatchPendingMessages::: generating {} messages", numberOfMessages);

        for (int index = 0; index < numberOfMessages; index++) {

            generateAndReceiveMessage(generateKey());
        }

        final MessageDto wrongMessage1 = generateMessage("wm1", BigDecimal.ZERO.intValue());
        final MessageDto wrongMessage2 = generateMessage("wm2", BigDecimal.ZERO.intValue());
        wrongMessage1.getHeaderDto().setType("OTHER");
        wrongMessage2.getHeaderDto().setType("OTHER");

        List<MessageDto> wrongMessages = Arrays.asList(wrongMessage1, wrongMessage2);

        for (MessageDto messageDto : wrongMessages) {

            postalService.receive(messageDto);
        }

        service.dispatch();

        List<Batch> pendingBatches = service.getPendingBatches();

        Assert.assertFalse(pendingBatches.isEmpty());

        Batch pendingBatch = pendingBatches.stream().findFirst().get();
        long numberOfPendingMessages = pendingBatch.getMessages().stream().filter(message -> !message.isSent()).count();

        Assert.assertEquals(wrongMessages.size(), numberOfPendingMessages);

        service.dispatchPendingMessages();

        pendingBatches = service.getPendingBatches();

        Assert.assertFalse(pendingBatches.isEmpty());
    }

    private MessageDto generateAndReceiveMessage(final String key) {

        return sendValidMessageToBeSentAfter(key, BigDecimal.ZERO.intValue());
    }

    private MessageDto sendValidMessageToBeSentAfter(final String key, int hours) {

        final MessageDto message = generateMessage(key, hours);

        log.info("sendValidMessageToBeSentAfter:::{}", new JSONObject(message).toString());

        postalService.receive(message);

        return message;
    }
}
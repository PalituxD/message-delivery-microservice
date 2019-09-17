package org.justfoolingaround.apps.messagedelivery.domain.operations.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.justfoolingaround.apps.messagedelivery.domain.operations.MessageOperations;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;
import org.justfoolingaround.apps.messagedelivery.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
public class MessageOperationsImpTest {

    private static final int MAX_NUMBER_OF_MESSAGES = 1000;

    @Autowired
    private MessageOperations operations;

    @Test
    public void save() {

        log.info("save:::");

        Message message = create();

        Assert.assertFalse(message.isSent());
    }

    @Test
    public void update() {

        log.info("update:::");

        Message message = create();

        Assert.assertFalse(message.isSent());

        message.setSent(Boolean.TRUE);

        operations.update(message);

        Optional<Message> optionalMessage = operations.searchByCodeReference(message.getCodeReference());

        Assert.assertTrue(optionalMessage.isPresent());

        Assert.assertTrue(optionalMessage.get().isSent());
    }

    @Test
    public void searchBySourceAndPayloadId() {

        log.info("searchBySourceAndPayloadId:::");

        Message message = create();

        Optional<Message> optionalMessage = operations.searchBySourceAndPayloadId(message.getSource(), message.getPayloadId());

        Assert.assertTrue(optionalMessage.isPresent());
    }

    @Test
    public void searchBySourceAndPayloadIdThenNoResult() {

        log.info("searchBySourceAndPayloadIdThenNoResult:::");

        Message message = create();

        Optional<Message> optionalMessage = operations.searchBySourceAndPayloadId(message.getSource(), Util.randomValue());

        Assert.assertFalse(optionalMessage.isPresent());
    }

    @Test
    public void searchByCodeReference() {

        log.info("searchByCodeReference:::");

        Message message = create();

        Optional<Message> optionalMessage = operations.searchByCodeReference(message.getCodeReference());

        Assert.assertTrue(optionalMessage.isPresent());
    }

    @Test
    public void searchByCodeReferenceThenNoResult() {

        log.info("searchByCodeReferenceThenNoResult:::");

        create();

        Optional<Message> optionalMessage = operations.searchByCodeReference(Util.randomValue());

        Assert.assertFalse(optionalMessage.isPresent());
    }

    @Test
    public void searchReadyMessagesToBeSentBeforeThan() {

        final int NUMBER_OF_MESSAGES = RandomUtils.nextInt(MAX_NUMBER_OF_MESSAGES);

        log.info("searchReadyMessagesToBeSentBeforeThan::: creating {} messages", NUMBER_OF_MESSAGES);

        for (int index = 0; index < NUMBER_OF_MESSAGES; index++) {

            create();
        }

        List<Message> messages = operations.searchReadyMessagesToBeSentBeforeThan(Calendar.getInstance().getTime());

        Assert.assertEquals(NUMBER_OF_MESSAGES, messages.size());
    }

    @Test
    public void searchReadyMessagesToBeSentBeforeThanThenNoResults() {

        Date currentDate = Calendar.getInstance().getTime();

        Date limitDate = Util.decreaseTimeInHours(currentDate, RandomUtils.nextInt(MAX_NUMBER_OF_MESSAGES));

        List<Message> messages = operations.searchReadyMessagesToBeSentBeforeThan(limitDate);

        Assert.assertTrue(messages.isEmpty());
    }

    private Message create() {
        return create(BigInteger.ZERO.intValue(), Util.randomValue());
    }

    private Message create(int hoursForSchedule, String codeReference) {

        log.info("create::: hoursForSchedule {} codeReference {}", hoursForSchedule, codeReference);

        Message message = Util.generateMessage();
        message.setCodeReference(codeReference);
        message.setScheduledDeliveryDate(Calendar.getInstance().getTime());
        message.setScheduledDeliveryDate(Util.increaseTimeInHours(message.getScheduledDeliveryDate(), hoursForSchedule));

        Message saved = operations.save(message);

        Assert.assertNotNull(saved.getId());

        log.info("created::: Message: {}", message);

        return saved;
    }
}
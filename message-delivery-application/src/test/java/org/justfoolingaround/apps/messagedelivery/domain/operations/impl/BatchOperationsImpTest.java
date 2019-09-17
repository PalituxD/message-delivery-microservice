package org.justfoolingaround.apps.messagedelivery.domain.operations.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.justfoolingaround.apps.messagedelivery.domain.enums.BatchStatus;
import org.justfoolingaround.apps.messagedelivery.domain.operations.BatchOperations;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Batch;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;
import org.justfoolingaround.apps.messagedelivery.domain.repository.MessageRepository;
import org.justfoolingaround.apps.messagedelivery.utils.Util;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
public class BatchOperationsImpTest {

    @Autowired
    private BatchOperations operations;

    @MockBean
    private MessageRepository messageRepository;

    @Before
    public void init() {

        Mockito.doNothing().when(messageRepository)
                .updateBatchReference(Mockito.anyList(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void save() {

        Batch batch = create();

        Assert.assertEquals(BatchStatus.CREATED, batch.getStatus());
    }

    @Test
    public void saveWithMessages() {

        List<Message> messages = Arrays.asList(Util.generateMessage(), Util.generateMessage(), Util.generateMessage());

        Batch batch = create(messages);

        Assert.assertEquals(BatchStatus.CREATED, batch.getStatus());
        Assert.assertEquals(messages.size(), batch.getMessages().size());
    }

    @Test
    public void update() {

        final BatchStatus STATUS_TO_BE_UPDATED_FOR = BatchStatus.PARTIAL_PROCESSED;

        Batch batch = create();

        Assert.assertEquals(BatchStatus.CREATED, batch.getStatus());

        batch.setStatus(STATUS_TO_BE_UPDATED_FOR);

        operations.update(batch);

        List<Batch> batches = operations.getBatches(STATUS_TO_BE_UPDATED_FOR);

        Assert.assertTrue(batches.stream().findFirst().isPresent());
    }

    @Test
    public void getBatchesByStatusAndIntents() {

        final int MAX_NUMBER_OF_INTENTS = 3;

        List<Batch> batches = Arrays.asList(create(), create(), create());

        batches.forEach(batch -> batch.setStatus(BatchStatus.PARTIAL_PROCESSED));

        batches.stream().findAny().ifPresent(batch -> batch.setIntent(MAX_NUMBER_OF_INTENTS + 1));

        batches.forEach(operations::update);

        List<Batch> batchesFound = operations.getBatches(BatchStatus.PARTIAL_PROCESSED, MAX_NUMBER_OF_INTENTS);

        Assert.assertEquals(batches.size() - 1, batchesFound.size());

    }

    @Test
    public void getBatchesByStatus() {

        int quantityOfBatches = RandomUtils.nextInt(BigDecimal.TEN.intValue());

        for (int i = 0; i < quantityOfBatches; i++) {
            create();
        }

        List<Batch> batches = operations.getBatches(BatchStatus.CREATED);

        long count = batches.stream()
                .filter(batch -> BatchStatus.CREATED.equals(batch.getStatus()))
                .count();

        Assert.assertTrue(quantityOfBatches <= count);
    }

    private Batch create() {

        return create(null);
    }

    private Batch create(List<Message> messages) {

        Batch batch = Util.generateBatch();

        batch.setMessages(messages);

        Batch saved = operations.save(batch);

        Assert.assertNotNull(saved.getId());

        log.info("create::: Batch: {}", batch);

        return saved;
    }
}
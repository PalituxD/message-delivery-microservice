package org.justfoolingaround.apps.messagedelivery.domain.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.justfoolingaround.apps.messagedelivery.domain.entity.BatchEntity;
import org.justfoolingaround.apps.messagedelivery.domain.entity.MessageEntity;
import org.justfoolingaround.apps.messagedelivery.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository repository;

    @Autowired
    private BatchRepository batchRepository;

    @Test
    public void create() {

        log.info("create:::");

        MessageEntity entity = createEntity();

        Assert.assertFalse(entity.isDeleted());
    }

    @Test
    public void update() {

        log.info("update:::");

        final boolean VALUE_FOR_UPDATE = Boolean.TRUE;

        MessageEntity entity = createEntity();

        entity.setSent(VALUE_FOR_UPDATE);

        MessageEntity updated = repository.save(entity);

        Optional<MessageEntity> optionalUpdatedEntity = repository.findById(updated.getId());

        Assert.assertTrue(optionalUpdatedEntity.isPresent());
        Assert.assertEquals(VALUE_FOR_UPDATE, optionalUpdatedEntity.get().isSent());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void remove() {

        log.info("remove:::");

        MessageEntity entity = createEntity();

        repository.delete(entity);
    }

    @Test
    public void findBySourceAndPayloadId() {

        log.info("findBySourceAndPayloadId:::");

        MessageEntity entity = createEntity();

        Assert.assertNotNull(repository.findBySourceAndPayloadId(entity.getSource(), entity.getPayloadId()));
    }

    @Test
    public void findByCodeReference() {

        log.info("findByCodeReference:::");

        MessageEntity entity = createEntity();

        Assert.assertNotNull(repository.findByCodeReference(entity.getCodeReference()));
    }

    @Test
    public void findAllByScheduledDeliveryDateBeforeAndBatchIsNullAndSentFalse() {

        log.info("findAllByScheduledDeliveryDateBeforeAndBatchIsNullAndSentFalse:::");

        Date scheduledDeliveryDate = Util.increaseTimeInHours(Calendar.getInstance().getTime(), BigDecimal.TEN.intValue());

        createEntity();
        createEntity();
        createEntity(scheduledDeliveryDate);

        Date currentDate = Calendar.getInstance().getTime();
        List<MessageEntity> messages = repository.findAllByScheduledDeliveryDateBeforeAndBatchIsNullAndSentFalse(currentDate);

        Assert.assertEquals(2, messages.size());
    }

    @Test
    public void updateBatchReference() {

        log.info("updateBatchReference:::");

        List<Long> ids = Arrays.asList(createEntity().getId(), createEntity().getId());

        Optional<BatchEntity> optionalBatchEntity = batchRepository.findById(1l);
        Assert.assertTrue(optionalBatchEntity.isPresent());

        BatchEntity batchEntity = optionalBatchEntity.get();
        log.info("updateBatchReference::: batch: id: {}, status: {}", batchEntity.getId(), batchEntity.getStatus());

        String modificationUser = Util.randomValue();
        Date modificationDate = Calendar.getInstance().getTime();

        repository.updateBatchReference(ids, batchEntity, modificationUser, modificationDate);

        List<MessageEntity> messageEntities = repository.findAllById(ids);

        Assert.assertFalse(messageEntities.isEmpty());

        for (MessageEntity messageEntity : messageEntities) {
            Assert.assertEquals(batchEntity.getId(), messageEntity.getBatch().getId());
        }
    }

    private MessageEntity createEntity() {

        return createEntity(Calendar.getInstance().getTime());
    }

    private MessageEntity createEntity(Date scheduledDeliveryDate) {

        MessageEntity entity = Util.generateMessageEntity();
        entity.setScheduledDeliveryDate(scheduledDeliveryDate);

        log.info("createEntity:::{}", entity);

        MessageEntity saved = repository.save(entity);

        Optional<MessageEntity> optionalEntity = repository.findById(saved.getId());

        Assert.assertTrue(optionalEntity.isPresent());

        return optionalEntity.get();
    }
}
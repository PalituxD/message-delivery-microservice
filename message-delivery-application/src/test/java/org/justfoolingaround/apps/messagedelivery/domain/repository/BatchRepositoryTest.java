package org.justfoolingaround.apps.messagedelivery.domain.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.justfoolingaround.apps.messagedelivery.domain.entity.BatchEntity;
import org.justfoolingaround.apps.messagedelivery.domain.enums.BatchStatus;
import org.justfoolingaround.apps.messagedelivery.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
public class BatchRepositoryTest {

    @Autowired
    private BatchRepository repository;

    @Test
    public void create() {

        log.info("create:::");

        BatchEntity entity = createEntity();

        Assert.assertFalse(entity.isDeleted());
    }

    @Test
    public void update() {

        log.info("update:::");

        final BatchStatus STATUS_FOR_UPDATE = BatchStatus.PARTIAL_PROCESSED;

        BatchEntity entity = createEntity();

        entity.setStatus(STATUS_FOR_UPDATE);

        BatchEntity updated = repository.save(entity);

        Optional<BatchEntity> optionalUpdatedEntity = repository.findById(updated.getId());

        Assert.assertTrue(optionalUpdatedEntity.isPresent());
        Assert.assertEquals(STATUS_FOR_UPDATE, optionalUpdatedEntity.get().getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void remove() {

        log.info("remove:::");

        BatchEntity entity = createEntity();

        repository.delete(entity);
    }

    @Test
    public void findAllByStatus() {

        log.info("findAllByStatus:::");

        List<BatchEntity> batches = Arrays.asList(createEntity(), createEntity());

        batches.forEach(batchEntity -> batchEntity.setStatus(BatchStatus.PARTIAL_PROCESSED));

        batches.forEach(repository::save);

        List<BatchEntity> allByStatus = repository.findAllByStatus(BatchStatus.PARTIAL_PROCESSED);

        Assert.assertEquals(batches.size(), allByStatus.size());
    }

    @Test
    public void findAllByStatusAndIntentLessThanEqual() {

        log.info("findAllByStatusAndMaxIntent:::");

        final int MAX_NUMBER_OF_INTENTS = 3;

        List<BatchEntity> batches = Arrays.asList(createEntity(), createEntity(), createEntity());

        batches.forEach(batchEntity -> batchEntity.setStatus(BatchStatus.PARTIAL_PROCESSED));

        batches.stream().findAny().ifPresent(batchEntity -> batchEntity.setIntent(MAX_NUMBER_OF_INTENTS + 1));

        batches.forEach(repository::save);

        List<BatchEntity> allByStatus = repository.findAllByStatusAndIntentLessThanEqual(BatchStatus.PARTIAL_PROCESSED, MAX_NUMBER_OF_INTENTS);

        Assert.assertEquals(batches.size() - 1, allByStatus.size());
    }

    private BatchEntity createEntity() {

        BatchEntity entity = Util.generateBatchEntity();

        log.info("createEntity:::{}", entity);

        BatchEntity saved = repository.save(entity);

        Optional<BatchEntity> optionalEntity = repository.findById(saved.getId());

        Assert.assertTrue(optionalEntity.isPresent());

        return optionalEntity.get();
    }
}
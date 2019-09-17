package org.justfoolingaround.apps.messagedelivery.domain.operations.impl;

import lombok.extern.slf4j.Slf4j;
import org.justfoolingaround.apps.messagedelivery.domain.entity.BatchEntity;
import org.justfoolingaround.apps.messagedelivery.domain.entity.MessageEntity;
import org.justfoolingaround.apps.messagedelivery.domain.enums.BatchStatus;
import org.justfoolingaround.apps.messagedelivery.domain.operations.BatchOperations;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Batch;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;
import org.justfoolingaround.apps.messagedelivery.domain.repository.BatchRepository;
import org.justfoolingaround.apps.messagedelivery.domain.repository.MessageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BatchOperationsImp implements BatchOperations {

    @Autowired
    private BatchRepository repository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public Batch save(Batch batch) {

        log.debug("save::: batch: {}", batch);

        List<Message> messages = batch.getMessages();

        BatchEntity entity = new BatchEntity();
        modelMapper.map(batch, entity);

        BatchEntity saved = repository.save(entity);
        modelMapper.map(saved, batch);

        if (messages != null && !messages.isEmpty()) {
            List<Long> ids = messages.stream().map(message -> message.getId()).collect(Collectors.toList());

            messageRepository.updateBatchReference(ids,
                    entity,
                    entity.getLastModificationUser(),
                    entity.getCreationDate());
        }

        batch.setMessages(messages);

        return batch;
    }

    @Override
    public void update(Batch batch) {

        log.debug("update::: batch: {}", batch);

        repository.findById(batch.getId()).ifPresent(updateBatch(batch));
    }

    @Override
    public List<Batch> getBatches(BatchStatus batchStatus, int maxNumberOfIntents) {

        log.debug("getBatches::: batchStatus: {}, maxNumberOfIntents: {}", batchStatus, maxNumberOfIntents);

        List<BatchEntity> pendingBatches = repository.findAllByStatusAndIntentLessThanEqual(batchStatus, maxNumberOfIntents);

        List<Batch> batches = pendingBatches.stream()
                .map(batchEntity -> buildBatchFrom(batchEntity, batchEntity.getMessages()))
                .collect(Collectors.toList());

        return batches;
    }

    @Override
    public List<Batch> getBatches(BatchStatus batchStatus) {

        log.debug("getBatches::: batchStatus: {}", batchStatus);

        List<BatchEntity> pendingBatches = repository.findAllByStatus(batchStatus);

        List<Batch> batches = new ArrayList<>(pendingBatches.size());
        for (BatchEntity batchEntity : pendingBatches) {
            Batch batch = buildBatchFrom(batchEntity, batchEntity.getMessages());
            batches.add(batch);
        }

        return batches;
    }

    private Consumer<BatchEntity> updateBatch(Batch batch) {

        return entity -> {
            modelMapper.map(batch, entity);
            repository.save(entity);
        };
    }

    private Batch buildBatchFrom(BatchEntity batchEntity, Collection<MessageEntity> entityMessages) {

        Batch batch = Batch.builder().build();
        modelMapper.map(batchEntity, batch);

        List<Message> messages = entityMessages.stream().map(this::buildMessageFrom).collect(Collectors.toList());

        batch.setMessages(messages);

        return batch;
    }

    private Message buildMessageFrom(MessageEntity entity) {

        Message message = Message.builder().build();
        modelMapper.map(entity, message);
        return message;
    }
}

package org.justfoolingaround.apps.messagedelivery.domain.operations.impl;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.justfoolingaround.apps.messagedelivery.domain.entity.MessageEntity;
import org.justfoolingaround.apps.messagedelivery.domain.operations.MessageOperations;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;
import org.justfoolingaround.apps.messagedelivery.domain.repository.BatchRepository;
import org.justfoolingaround.apps.messagedelivery.domain.repository.MessageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageOperationsImp implements MessageOperations {

    @Autowired
    private MessageRepository repository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Message save(Message message) {

        log.debug("save::: message: {}", message);

        MessageEntity entity = new MessageEntity();
        modelMapper.map(message, entity);

        MessageEntity save = repository.save(entity);
        modelMapper.map(save, message);

        return message;
    }

    @Override
    public void update(Message message) {

        log.debug("update::: message: {}", message);

        repository.findById(message.getId()).ifPresent(updateMessage(message));
    }

    @Override
    public Optional<Message> searchBySourceAndPayloadId(String source, String payloadId) {

        log.debug("Looking for an entity message, searching by source {} and payload {}", source, payloadId);

        MessageEntity entity = repository.findBySourceAndPayloadId(source, payloadId);

        if (entity == null) {

            log.debug("Entity Message not found, searched by source {} and payload {}", source, payloadId);

            return Optional.empty();
        }

        return Optional.of(buildMessageFrom(entity));
    }

    @Override
    public Optional<Message> searchByCodeReference(String codeReference) {

        log.debug("Looking for an entity message, searching by codeReference {}", codeReference);

        MessageEntity entity = repository.findByCodeReference(codeReference);

        if (entity == null) {

            log.debug("Entity Message not found, searched by codeReference {}", codeReference);

            return Optional.empty();
        }

        log.debug("Entity Message found {}, searched by by codeReference {}",
                new JSONObject(entity).toString(), codeReference);

        return Optional.of(buildMessageFrom(entity));
    }

    @Override
    public List<Message> searchReadyMessagesToBeSentBeforeThan(Date date) {

        log.debug("Looking for ready messages to be sent before than {}", date);

        List<MessageEntity> entities = repository.findAllByScheduledDeliveryDateBeforeAndBatchIsNullAndSentFalse(date);

        if (entities.isEmpty()) {

            log.debug("There is not ready messages at {}", date);
        }

        log.debug("Number of messages found {} at {}", entities.size(), date);

        return entities.stream().map(this::buildMessageFrom).collect(Collectors.toList());
    }

    private Consumer<MessageEntity> updateMessage(Message message) {

        return entity -> {
            modelMapper.map(message, entity);
            repository.save(entity);
        };
    }

    private Message buildMessageFrom(MessageEntity entity) {

        Message message = Message.builder().build();
        modelMapper.map(entity, message);
        return message;
    }
}

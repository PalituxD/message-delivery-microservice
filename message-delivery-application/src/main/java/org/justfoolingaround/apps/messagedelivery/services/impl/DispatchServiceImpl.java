package org.justfoolingaround.apps.messagedelivery.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.justfoolingaround.apps.messagedelivery.base.Constants;
import org.justfoolingaround.apps.messagedelivery.domain.enums.BatchStatus;
import org.justfoolingaround.apps.messagedelivery.domain.operations.BatchOperations;
import org.justfoolingaround.apps.messagedelivery.domain.operations.MessageOperations;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Batch;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;
import org.justfoolingaround.apps.messagedelivery.services.DispatchService;
import org.justfoolingaround.apps.messagedelivery.services.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DispatchServiceImpl implements DispatchService {

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private BatchOperations batchOperations;

    @Autowired
    private MessageOperations messageOperations;

    @Value(Constants.MAX_NUMBER_OF_INTENTS_FOR_BATCHES)
    private int maxNumberOfIntentsForBatches;

    @Override
    public void dispatch() {

        Date currentDate = Calendar.getInstance().getTime();

        List<Message> messages = messageOperations.searchReadyMessagesToBeSentBeforeThan(currentDate);

        if (messages.isEmpty()) {

            return;
        }

        send(createBatchFor(messages));
    }

    @Override
    public void dispatchPendingMessages() {

        List<Batch> pendingBatches = batchOperations.getBatches(BatchStatus.PARTIAL_PROCESSED, maxNumberOfIntentsForBatches);

        if (pendingBatches.isEmpty()) {

            log.info("There is not pending batches to be sent");
            return;
        }

        pendingBatches.stream().forEach(this::send);
    }

    @Override
    public List<Batch> getPendingBatches() {

        List<Batch> batches = batchOperations.getBatches(BatchStatus.PARTIAL_PROCESSED);

        log.debug("Pending batches quantity: {}, batches {} ", batches.size(), batches);

        return batches;
    }

    private Batch createBatchFor(List<Message> messages) {

        Batch batch = Batch.builder()
                .intent(0)
                .result(BatchStatus.CREATED.toString())
                .status(BatchStatus.CREATED)
                .messages(messages)
                .build();

        return batchOperations.save(batch);
    }

    private void send(Batch batch) {

        final List<Message> messagesToBeSent = batch.getMessages().stream()
                .filter(message -> !message.isSent())
                .collect(Collectors.toList());

        final int expectedMessagesBeSent = messagesToBeSent.size();

        prepare(batch);

        int numberOfSentMessages = shippingService.send(messagesToBeSent);

        updateWithResults(batch, expectedMessagesBeSent, numberOfSentMessages);
    }

    private void prepare(Batch batch) {

        batch.setStatus(BatchStatus.PROCESSING);
        batch.setIntent(batch.getIntent() + 1);
        batchOperations.update(batch);
    }

    private void updateWithResults(Batch batch, int expectedMessagesBeSent, int numberOfSentMessages) {

        if (expectedMessagesBeSent > numberOfSentMessages) {

            batch.setStatus(BatchStatus.PARTIAL_PROCESSED);
            batch.setResult(String.valueOf(batch.getStatus()));
        } else {

            batch.setStatus(BatchStatus.PROCESSED);
            batch.setResult(String.valueOf(batch.getStatus()));
        }

        batchOperations.update(batch);
    }
}

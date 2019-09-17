package org.justfoolingaround.apps.messagedelivery.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.justfoolingaround.apps.messagedelivery.domain.operations.MessageOperations;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;
import org.justfoolingaround.apps.messagedelivery.services.ShippingService;
import org.justfoolingaround.apps.messagedelivery.services.senders.MessageDealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Collection;

@Service
@Slf4j
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private MessageDealerService messageDealerService;

    @Autowired
    private MessageOperations messageOperations;

    @Override
    @Transactional()
    public int send(Collection<Message> messages) {

        int numberOfSentMessages = 0;

        for (Message message : messages) {

            log.debug("Sending... {}", message.getPayloadId());
            try {

                messageDealerService.send(message);
                updateAsSent(message);
                numberOfSentMessages++;
            } catch (Exception e) {

                log.info(e.getMessage(), e);
            }
        }

        return numberOfSentMessages;
    }

    private void updateAsSent(Message message) {
        message.setSent(Boolean.TRUE);
        message.setDeliveryDate(Calendar.getInstance().getTime());
        messageOperations.update(message);
    }
}

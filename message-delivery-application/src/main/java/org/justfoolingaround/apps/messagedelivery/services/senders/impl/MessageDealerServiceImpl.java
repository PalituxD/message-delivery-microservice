package org.justfoolingaround.apps.messagedelivery.services.senders.impl;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.justfoolingaround.apps.messagedelivery.domain.enums.MessageType;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;
import org.justfoolingaround.apps.messagedelivery.services.senders.MessageDealerService;
import org.justfoolingaround.apps.messagedelivery.services.senders.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.EnumMap;

@Service
@Slf4j
public class MessageDealerServiceImpl implements MessageDealerService {

    @Autowired
    @Qualifier("emailSenderService")
    private SenderService emailSenderService;

    private EnumMap<MessageType, SenderService> dealersMap;

    @PostConstruct
    private void postConstruct() {

        dealersMap = new EnumMap<>(MessageType.class);

        dealersMap.put(emailSenderService.getSupportedMessageType(), emailSenderService);

        log.info("Supported dealers: {}", new JSONObject(dealersMap).toString());
    }

    @Override
    public void send(Message message) throws UnsupportedOperationException {

        SenderService senderService = dealersMap.get(MessageType.valueOf(String.valueOf(message.getType())));

        if (senderService == null) {

            log.error("This type of message is not supported yet: {}" + message.getType());

            throw new UnsupportedOperationException("This type of message is not supported yet: " + message.getType());
        }

        log.debug("Sending the message: {} using the sender: {}", message, senderService.getClass());

        senderService.send(message);
    }
}

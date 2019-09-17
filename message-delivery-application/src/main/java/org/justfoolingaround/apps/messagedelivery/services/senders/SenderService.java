package org.justfoolingaround.apps.messagedelivery.services.senders;

import org.justfoolingaround.apps.messagedelivery.domain.enums.MessageType;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;

public interface SenderService {

    void send(Message message);

    MessageType getSupportedMessageType();
}

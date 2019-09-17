package org.justfoolingaround.apps.messagedelivery.services.senders;

import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;

public interface MessageDealerService {

    void send(Message message) throws UnsupportedOperationException;
}

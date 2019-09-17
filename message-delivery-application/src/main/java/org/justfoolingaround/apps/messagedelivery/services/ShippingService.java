package org.justfoolingaround.apps.messagedelivery.services;

import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;

import java.util.Collection;

public interface ShippingService {

    int send(Collection<Message> messages);
}

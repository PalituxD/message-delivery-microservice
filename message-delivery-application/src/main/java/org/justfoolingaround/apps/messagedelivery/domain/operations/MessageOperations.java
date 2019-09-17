package org.justfoolingaround.apps.messagedelivery.domain.operations;

import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MessageOperations {

    Message save(Message message);

    void update(Message message);

    Optional<Message> searchBySourceAndPayloadId(String source, String payloadId);

    Optional<Message> searchByCodeReference(String codeReference);

    List<Message> searchReadyMessagesToBeSentBeforeThan(Date date);
}

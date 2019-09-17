package org.justfoolingaround.apps.messagedelivery.services;

import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageDto;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageHeaderDto;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageInfoDto;

import java.util.Optional;

public interface PostalService {

    String receive(MessageDto message);

    Optional<MessageInfoDto> lookup(MessageHeaderDto messageHeader);

    Optional<MessageInfoDto> lookup(String codeReference);
}

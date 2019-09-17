package org.justfoolingaround.apps.messagedelivery.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.justfoolingaround.apps.messagedelivery.domain.operations.MessageOperations;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;
import org.justfoolingaround.apps.messagedelivery.services.CodeGeneratorService;
import org.justfoolingaround.apps.messagedelivery.services.PostalService;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageDto;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageHeaderDto;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageInfoDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PostalServiceImpl implements PostalService {

    @Autowired
    private MessageOperations messageOperator;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Override
    public String receive(MessageDto messageDto) {

        log.debug("New message received: {}", new JSONObject(messageDto.getHeaderDto()).toString());

        Message message = Message.builder().build();

        modelMapper.map(messageDto, message);
        modelMapper.map(messageDto.getHeaderDto(), message);

        message.setCodeReference(codeGeneratorService.generate());

        messageOperator.save(message);

        return message.getCodeReference();
    }

    @Override
    public Optional<MessageInfoDto> lookup(MessageHeaderDto messageHeaderDto) {

        log.debug("Looking for a message, searching by: {}", new JSONObject(messageHeaderDto).toString());

        Optional<Message> optionalMessage = messageOperator.searchBySourceAndPayloadId(messageHeaderDto.getSource(),
                messageHeaderDto.getPayloadId());

        if (!optionalMessage.isPresent()) {

            log.debug("Message not found, searched by: {}", new JSONObject(messageHeaderDto).toString());

            return Optional.empty();
        }

        return Optional.of(getMessageInfo(optionalMessage.get()));
    }

    @Override
    public Optional<MessageInfoDto> lookup(String codeReference) {
        log.debug("Looking for a message, searching by: {}", codeReference);

        Optional<Message> optionalMessage = messageOperator.searchByCodeReference(codeReference);

        if (!optionalMessage.isPresent()) {

            log.debug("Message not found, searched by: {}", codeReference);

            return Optional.empty();
        }

        return Optional.of(getMessageInfo(optionalMessage.get()));
    }

    private MessageInfoDto getMessageInfo(Message message) {

        MessageInfoDto messageInfoDto = MessageInfoDto.builder().build();
        MessageHeaderDto messageHeaderDto = MessageHeaderDto.builder().build();

        modelMapper.map(message, messageInfoDto);
        modelMapper.map(message, messageHeaderDto);

        messageInfoDto.setHeaderDto(messageHeaderDto);

        log.debug("Message found {}, searched by: {}",
                new JSONObject(messageInfoDto).toString(),
                new JSONObject(messageHeaderDto).toString());

        return messageInfoDto;
    }
}

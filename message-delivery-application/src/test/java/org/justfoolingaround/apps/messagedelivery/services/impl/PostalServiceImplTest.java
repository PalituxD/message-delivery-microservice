package org.justfoolingaround.apps.messagedelivery.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.justfoolingaround.apps.messagedelivery.services.CodeGeneratorService;
import org.justfoolingaround.apps.messagedelivery.services.PostalService;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageDto;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageHeaderDto;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageInfoDto;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.justfoolingaround.apps.messagedelivery.utils.Util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
public class PostalServiceImplTest {

    private static final String MOCK_CODE_REFERENCE = "MY-CUSTOM-CODE";

    @Autowired
    private PostalService service;

    @MockBean
    private CodeGeneratorService codeGeneratorService;

    @Before
    public void before() {
        Mockito.when(codeGeneratorService.generate()).thenReturn(MOCK_CODE_REFERENCE);
    }

    @Test
    public void receive() {

        final MessageDto message = generateMessage(randomValue(), 0);

        log.info("receive:::{}", new JSONObject(message).toString());

        String codeReference = service.receive(message);

        Assert.assertEquals(MOCK_CODE_REFERENCE, codeReference);
    }

    @Test
    public void lookupByHeaderDto() {

        final String messageKey = generateKey();
        log.info("lookupHeaderDto:::{}", messageKey);

        final MessageDto message = generateAndReceiveMessage(messageKey);

        MessageHeaderDto headerDto = message.getHeaderDto();

        Optional<MessageInfoDto> optionalMessageInfoDto = service.lookup(headerDto);

        log.info("lookupHeaderDto:::{}", optionalMessageInfoDto);

        Assert.assertTrue(optionalMessageInfoDto.isPresent());
        Assert.assertFalse(optionalMessageInfoDto.get().isSent());
        Assert.assertNull(optionalMessageInfoDto.get().getDeliveryDate());
        Assert.assertEquals(message.getScheduledDeliveryDate(), optionalMessageInfoDto.get().getScheduledDeliveryDate());
    }

    @Test
    public void lookupByHeaderDtoThenNotResults() {

        final String messageKey = generateKey();
        log.info("lookupByHeaderDtoThenNotResults:::{}", messageKey);

        final MessageDto message = generateAndReceiveMessage(messageKey);

        MessageHeaderDto headerDto = message.getHeaderDto();

        headerDto.setPayloadId(randomValue());

        Optional<MessageInfoDto> optionalMessageInfoDto = service.lookup(headerDto);

        log.info("lookupByHeaderDtoThenNotResults:::{}", optionalMessageInfoDto);

        Assert.assertFalse(optionalMessageInfoDto.isPresent());
    }

    @Test
    public void lookupByCodeReference() {

        final String messageKey = generateKey();
        log.info("lookupByCodeReference:::{}", messageKey);

        final MessageDto message = generateAndReceiveMessage(messageKey);

        Optional<MessageInfoDto> optionalMessageInfoDto = service.lookup(MOCK_CODE_REFERENCE);

        log.info("lookupByCodeReference:::{}", optionalMessageInfoDto);

        Assert.assertTrue(optionalMessageInfoDto.isPresent());
        Assert.assertFalse(optionalMessageInfoDto.get().isSent());
        Assert.assertNull(optionalMessageInfoDto.get().getDeliveryDate());
        Assert.assertEquals(message.getScheduledDeliveryDate(), optionalMessageInfoDto.get().getScheduledDeliveryDate());
    }

    @Test
    public void lookupByCodeReferenceThenNotResults() {

        Optional<MessageInfoDto> optionalMessageInfoDto = service.lookup(randomValue());

        log.info("lookupByCodeReferenceThenNotResults:::{}", optionalMessageInfoDto);

        Assert.assertFalse(optionalMessageInfoDto.isPresent());
    }

    private MessageDto generateAndReceiveMessage(final String key) {

        return receive(key, BigDecimal.ZERO.intValue());
    }

    private MessageDto receive(final String key, int hours) {

        final MessageDto message = generateMessage(key, hours);

        log.info("sendValidMessageToBeSentAfter:::{}", new JSONObject(message).toString());

        String codeReference = service.receive(message);

        Assert.assertEquals(MOCK_CODE_REFERENCE, codeReference);

        return message;
    }
}
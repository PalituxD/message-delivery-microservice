package org.justfoolingaround.apps.messagedelivery.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.json.JSONObject;
import org.justfoolingaround.apps.messagedelivery.domain.entity.BatchEntity;
import org.justfoolingaround.apps.messagedelivery.domain.entity.MessageEntity;
import org.justfoolingaround.apps.messagedelivery.domain.enums.BatchStatus;
import org.justfoolingaround.apps.messagedelivery.domain.enums.MessageType;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Batch;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Message;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageDto;
import org.justfoolingaround.apps.messagedelivery.web.v1.dto.MessageHeaderDto;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class Util {

    public static MessageDto generateMessage(String key, int timeInHoursToBeSent) {

        MessageHeaderDto messageHeaderDto = new MessageHeaderDto();
        messageHeaderDto.setPayloadId("PAYLOAD" + key);
        messageHeaderDto.setSource("SOURCE" + key);
        messageHeaderDto.setType("E_MAIL");

        MessageDto messageDto = new MessageDto();
        messageDto.setContent("Content" + key);
        messageDto.setFrom("FROM" + key + "@GMAIL.COM");
        messageDto.setHeaderDto(messageHeaderDto);
        messageDto.setReplyTo("REPLYTO" + key);
        messageDto.setScheduledDeliveryDate(Calendar.getInstance().getTime());
        messageDto.setSubject("SUBJECT" + key);
        messageDto.setTo("EMAIL" + key + "@GMAIL.COM");

        messageDto.setScheduledDeliveryDate(increaseTimeInHours(messageDto.getScheduledDeliveryDate(), timeInHoursToBeSent));

        log.debug("Generated MessageDto: {}", messageDto);

        return messageDto;
    }

    public static Date increaseTimeInHours(Date date, int hours) {

        return Date.from(date.toInstant().plus(Duration.ofHours(hours)));
    }

    public static Date decreaseTimeInHours(Date date, int hours) {

        return Date.from(date.toInstant().minus(Duration.ofHours(hours)));
    }

    public static String generateKey() {

        return RandomStringUtils.randomAlphanumeric(BigDecimal.TEN.intValue());
    }

    public static String randomValue() {

        return UUID.randomUUID().toString();
    }

    public static Batch generateBatch() {

        return Batch.builder()
                .status(BatchStatus.CREATED)
                .result(BatchStatus.CREATED.toString())
                .intent(0)
                .id(null)
                .build();
    }

    public static BatchEntity generateBatchEntity() {

        BatchEntity entity = new BatchEntity();

        entity.setIntent(0);
        entity.setResult(randomValue());
        entity.setStatus(BatchStatus.CREATED);

        return entity;
    }

    public static MessageEntity generateMessageEntity() {

        MessageEntity entity = new MessageEntity();

        entity.setBatch(null);
        entity.setCodeReference(randomValue());
        entity.setContent(randomValue());
        entity.setDeliveryDate(null);
        entity.setFrom(randomValue());
        entity.setPayloadId(randomValue());
        entity.setReplyTo(randomValue());
        entity.setCodeReference(randomValue());
        entity.setScheduledDeliveryDate(Calendar.getInstance().getTime());
        entity.setSent(Boolean.FALSE);
        entity.setSource(randomValue());
        entity.setSubject(randomValue());
        entity.setTo(randomValue());
        entity.setType(MessageType.E_MAIL);

        return entity;
    }

    public static Message generateMessage() {

        return generateMessage(generateKey());
    }

    public static Message generateMessage(String key) {
        return Message.builder()
                .codeReference(null)
                .content("content" + key)
                .deliveryDate(null)
                .from("from" + key + "@GMAIL.COM")
                .id(null)
                .payloadId("payloadId" + key)
                .replyTo("replyTo" + key)
                .scheduledDeliveryDate(Calendar.getInstance().getTime())
                .sent(Boolean.FALSE)
                .source("source" + key)
                .subject("subject" + key)
                .to("content" + key + "@GMAIL.COM")
                .type(MessageType.E_MAIL)
                .build();
    }
}

package org.justfoolingaround.apps.messagedelivery.domain.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.justfoolingaround.apps.messagedelivery.domain.enums.MessageType;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
public class Message {

    private Long id;
    private String payloadId;
    private String source;
    private String codeReference;
    private MessageType type;
    private String subject;
    private String from;
    private String to;
    private String content;
    private String replyTo;
    private Date scheduledDeliveryDate;
    private Date deliveryDate;
    private boolean sent;
}

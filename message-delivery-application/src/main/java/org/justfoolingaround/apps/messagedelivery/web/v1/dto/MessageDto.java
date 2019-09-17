package org.justfoolingaround.apps.messagedelivery.web.v1.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
public class MessageDto {

    private MessageHeaderDto headerDto;
    private String subject;
    private String from;
    private String to;
    private String content;
    private String replyTo;
    private Date scheduledDeliveryDate;
}

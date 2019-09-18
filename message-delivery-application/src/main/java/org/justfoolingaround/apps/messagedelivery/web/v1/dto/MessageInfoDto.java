package org.justfoolingaround.apps.messagedelivery.web.v1.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class MessageInfoDto {

    private MessageHeaderDto headerDto;

    private String codeReference;

    private Date scheduledDeliveryDate;

    private Date deliveryDate;

    private boolean sent;
}
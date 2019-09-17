package org.justfoolingaround.apps.messagedelivery.web.v1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class MessageHeaderDto {

    private String payloadId;
    private String source;
    private String type;
}

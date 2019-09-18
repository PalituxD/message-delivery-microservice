package org.justfoolingaround.apps.messagedelivery.web.v1.dto;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class MessageHeaderDto {

    @NotBlank
    @ApiParam(value = "Request PayloadId", format = "text", required = true)
    private String payloadId;

    @NotBlank
    @ApiParam(value = "Request client", format = "text", required = true)
    private String source;

    @NotBlank
    @ApiParam(value = "Message type", format = "text", required = true)
    private String type;
}

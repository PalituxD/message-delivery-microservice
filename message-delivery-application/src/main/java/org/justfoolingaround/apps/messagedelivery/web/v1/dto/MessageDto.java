package org.justfoolingaround.apps.messagedelivery.web.v1.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.justfoolingaround.apps.messagedelivery.base.Constants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@ToString
public class MessageDto {

    @NotNull
    private MessageHeaderDto headerDto;

    @NotBlank
    @ApiParam(value = "Email subject", required = true, format = "text")
    private String subject;

    @NotBlank
    @ApiParam(value = "Email sender name", required = true, format = "text")
    private String from;

    @Email
    @NotBlank
    @ApiParam(value = "Destination email", required = true, format = "email")
    private String to;

    @NotBlank
    @ApiParam(value = "Email content", required = true, format = "text")
    private String content;

    @ApiParam(value = "Reply email to", required = true, format = "text")
    private String replyTo;

    @NotNull
    @DateTimeFormat(pattern = Constants.DEFAULT_DATE_FORMAT)
    @ApiParam(value = "Expected date time to be sent", required = true, format = Constants.DEFAULT_DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DEFAULT_DATE_FORMAT)
    private Date scheduledDeliveryDate;
}

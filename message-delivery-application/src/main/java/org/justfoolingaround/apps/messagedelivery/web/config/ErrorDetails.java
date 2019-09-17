package org.justfoolingaround.apps.messagedelivery.web.config;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
}
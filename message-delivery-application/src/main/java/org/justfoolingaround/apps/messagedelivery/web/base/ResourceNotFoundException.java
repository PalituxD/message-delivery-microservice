package org.justfoolingaround.apps.messagedelivery.web.base;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String detail;

    public ResourceNotFoundException(String message, String detail) {
        super(message);
        this.detail = detail;
    }
}

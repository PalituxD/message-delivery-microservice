package org.justfoolingaround.apps.messagedelivery.services.impl;

import org.justfoolingaround.apps.messagedelivery.services.CodeGeneratorService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CodeGeneratorServiceImpl implements CodeGeneratorService {

    @Override
    public String generate() {

        return UUID.randomUUID().toString();
    }
}

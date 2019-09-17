package org.justfoolingaround.apps.messagedelivery.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.justfoolingaround.apps.messagedelivery.services.CodeGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CodeGeneratorServiceImplTest {

    @Autowired
    private CodeGeneratorService service;

    @Test
    public void generate() {
        Assert.assertNotNull(service.generate());
    }
}
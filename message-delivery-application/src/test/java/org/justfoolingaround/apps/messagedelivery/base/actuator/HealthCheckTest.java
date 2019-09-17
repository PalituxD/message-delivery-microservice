package org.justfoolingaround.apps.messagedelivery.base.actuator;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Slf4j
public class HealthCheckTest {

    @Autowired
    private HealthCheck healthCheck;

    @Test
    public void health() {
        
        Health health = healthCheck.health();

        Assert.assertEquals(Status.UP, health.getStatus());
    }
}
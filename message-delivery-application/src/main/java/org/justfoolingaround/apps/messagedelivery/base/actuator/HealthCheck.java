package org.justfoolingaround.apps.messagedelivery.base.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HealthCheck implements HealthIndicator {

    public static final int STATUS_CODE_SUCCESS = 200;
    public static final int STATUS_CODE_FAILURE = 500;

    @Override
    public Health health() {
        int check = check();
        if (check == STATUS_CODE_SUCCESS) {
            return Health.up().build();
        } else {
            Map<String, Object> details = new HashMap();
            details.put("Error code", String.valueOf(check));
            return Health.down().withDetails(details).build();
        }
    }

    private int check() {
        try {
            return STATUS_CODE_SUCCESS;
        } catch (Exception e) {
            return STATUS_CODE_FAILURE;
        }
    }
}

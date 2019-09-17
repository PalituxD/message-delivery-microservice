package org.justfoolingaround.apps.messagedelivery.tasks;

import lombok.extern.slf4j.Slf4j;
import org.justfoolingaround.apps.messagedelivery.base.Constants;
import org.justfoolingaround.apps.messagedelivery.services.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class ScheduledTasks {

    private AtomicInteger count = new AtomicInteger(0);

    @Autowired
    private DispatchService dispatchService;

    @Scheduled(initialDelayString = Constants.SUBMIT_MESSAGES_INITIAL_DELAY_IN_MILLISECONDS,
            fixedRateString = Constants.SUBMIT_MESSAGES_FIXED_RATE_IN_MILLISECONDS)
    public void dispatchMessages() {

        this.count.incrementAndGet();

        log.info("Shipping messages process is starting at {}", Calendar.getInstance().getTime());

        dispatchService.dispatch();

        dispatchService.dispatchPendingMessages();

        log.info("Shipping messages process has been finished at {}", Calendar.getInstance().getTime());
    }

    public int getInvocationCount() {
        return this.count.get();
    }
}

package org.justfoolingaround.apps.messagedelivery.tasks;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.justfoolingaround.apps.messagedelivery.services.DispatchService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class ScheduledTasksTest {

    @InjectMocks
    private ScheduledTasks scheduledTasks;

    @Mock
    private DispatchService dispatchService;

    @Before
    public void init() {

        Mockito.doNothing().when(dispatchService).dispatch();
        Mockito.doNothing().when(dispatchService).dispatchPendingMessages();
    }

    @Test
    public void dispatchMessages() {

        scheduledTasks.dispatchMessages();

        Assert.assertTrue(scheduledTasks.getInvocationCount() > 0);
    }
}
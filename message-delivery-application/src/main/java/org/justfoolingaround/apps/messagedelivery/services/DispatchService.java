package org.justfoolingaround.apps.messagedelivery.services;

import org.justfoolingaround.apps.messagedelivery.domain.pojo.Batch;

import java.util.List;

public interface DispatchService {

    void dispatch();

    void dispatchPendingMessages();

    List<Batch> getPendingBatches();
}

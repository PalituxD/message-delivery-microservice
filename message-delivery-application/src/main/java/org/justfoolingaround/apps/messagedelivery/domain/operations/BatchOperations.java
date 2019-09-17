package org.justfoolingaround.apps.messagedelivery.domain.operations;


import org.justfoolingaround.apps.messagedelivery.domain.enums.BatchStatus;
import org.justfoolingaround.apps.messagedelivery.domain.pojo.Batch;

import java.util.List;

public interface BatchOperations {

    Batch save(Batch batch);

    void update(Batch batch);

    List<Batch> getBatches(BatchStatus batchStatus, int maxNumberOfIntents);

    List<Batch> getBatches(BatchStatus batchStatus);
}

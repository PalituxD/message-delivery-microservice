package org.justfoolingaround.apps.messagedelivery.domain.repository;

import org.justfoolingaround.apps.messagedelivery.domain.entity.BatchEntity;
import org.justfoolingaround.apps.messagedelivery.domain.enums.BatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<BatchEntity, Long> {

    List<BatchEntity> findAllByStatusAndIntentLessThanEqual(BatchStatus status, int limitOfIntents);

    List<BatchEntity> findAllByStatus(BatchStatus status);
}
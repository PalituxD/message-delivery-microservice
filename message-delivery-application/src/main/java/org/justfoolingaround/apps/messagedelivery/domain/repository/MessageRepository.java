package org.justfoolingaround.apps.messagedelivery.domain.repository;

import org.justfoolingaround.apps.messagedelivery.domain.entity.BatchEntity;
import org.justfoolingaround.apps.messagedelivery.domain.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    MessageEntity findBySourceAndPayloadId(String source, String payloadId);

    MessageEntity findByCodeReference(String codeReference);

    List<MessageEntity> findAllByScheduledDeliveryDateBeforeAndBatchIsNullAndSentFalse(Date limitDate);

    @Modifying
    @Query("UPDATE MessageEntity u " +
            "SET u.batch = :batch, " +
            "u.lastModificationUser = :modificationUser, " +
            "lastModificationDate = :modificationDate " +
            "WHERE u.id in :ids ")
    @Transactional(readOnly = true)
    void updateBatchReference(@Param("ids") List<Long> ids,
                              @Param("batch") BatchEntity batchEntity,
                              @Param("modificationUser") String modificationUser,
                              @Param("modificationDate") Date modificationDate);
}

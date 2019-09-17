package org.justfoolingaround.apps.messagedelivery.domain.listeners;

import lombok.extern.slf4j.Slf4j;
import org.justfoolingaround.apps.messagedelivery.domain.entity.BaseEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.util.Calendar;

@Slf4j
public class AuditListener {

    @PrePersist
    private void beforeCreateOperation(Object object) {

        BaseEntity entity = (BaseEntity) object;
        entity.setDeleted(Boolean.FALSE);
        entity.setCreationDate(Calendar.getInstance().getTime());
        entity.setCreationUser("");
        entity.setLastModificationUser(entity.getCreationUser());
        entity.setLastModificationDate(entity.getCreationDate());

        log.debug("Saving a new record: {}", object);
    }

    @PreUpdate
    private void beforeUpdateOperation(Object object) {

        BaseEntity entity = (BaseEntity) object;
        entity.setLastModificationUser("");
        entity.setLastModificationDate(Calendar.getInstance().getTime());

        log.debug("Updating an existing record: {}", object);
    }

    @PreRemove
    private void beforeRemoveOperation(Object object) {

        throw new UnsupportedOperationException();
    }
}

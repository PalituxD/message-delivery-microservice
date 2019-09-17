package org.justfoolingaround.apps.messagedelivery.domain.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;
import org.justfoolingaround.apps.messagedelivery.domain.enums.BatchStatus;
import org.justfoolingaround.apps.messagedelivery.domain.listeners.AuditListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "batch")
@EntityListeners(AuditListener.class)
@Where(clause = "deleted is false")
@ToString(callSuper = true, exclude = "messages")
public class BatchEntity extends BaseEntity implements Serializable {

    @Enumerated(EnumType.STRING)
    private BatchStatus status;
    private int intent;
    private String result;
    @OneToMany(mappedBy = "batch", fetch = FetchType.EAGER)
    private List<MessageEntity> messages;
}
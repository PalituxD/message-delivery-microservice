package org.justfoolingaround.apps.messagedelivery.domain.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Where;
import org.justfoolingaround.apps.messagedelivery.domain.enums.MessageType;
import org.justfoolingaround.apps.messagedelivery.domain.listeners.AuditListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "message")
@EntityListeners(AuditListener.class)
@Where(clause = "deleted = false")
@ToString(callSuper = true)
public class MessageEntity extends BaseEntity implements Serializable {

    private String payloadId;
    private String source;
    @Enumerated(EnumType.STRING)
    private MessageType type;
    private String codeReference;
    private String subject;
    @Column(name = "from_account")
    private String from;
    @Column(name = "to_account")
    private String to;
    private String content;
    private String replyTo;
    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduledDeliveryDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;
    @ManyToOne
    private BatchEntity batch;
    private boolean sent;
}
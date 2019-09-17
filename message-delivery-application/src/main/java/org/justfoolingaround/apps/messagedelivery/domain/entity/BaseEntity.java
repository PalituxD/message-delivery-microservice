package org.justfoolingaround.apps.messagedelivery.domain.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@MappedSuperclass
@ToString
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean deleted;
    private String creationUser;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    private String lastModificationUser;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModificationDate;
}

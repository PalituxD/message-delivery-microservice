package org.justfoolingaround.apps.messagedelivery.domain.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.justfoolingaround.apps.messagedelivery.domain.enums.BatchStatus;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class Batch {

    private Long id;
    private BatchStatus status;
    private int intent;
    private String result;
    private List<Message> messages;
}

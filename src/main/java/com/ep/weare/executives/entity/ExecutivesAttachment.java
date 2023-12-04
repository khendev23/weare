package com.ep.weare.executives.entity;

import com.ep.weare.ministry.entity.Ministry;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "executives_attachment")
public class ExecutivesAttachment {

    @Id
    private int executivesAttachmentId;
    private int executivesId;
    private String executivesOriginalFilename;
    private String executivesRenamedFilename;

    @ManyToOne
    @JoinColumn(name = "executivesId", insertable = false, updatable = false)
    private Executives executives;

    @Override
    public String toString() {
        return "ExecutivesAttachment{" +
                "executivesAttachmentId=" + executivesAttachmentId +
                ", executivesId=" + executivesId +
                ", executivesOriginalFilename='" + executivesOriginalFilename + '\'' +
                ", executivesRenamedFilename='" + executivesRenamedFilename + '\'' +
                '}';
    }
}

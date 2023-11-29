package com.ep.weare.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kelly")
public class Kelly {

    @Id
    private Integer kellyId;
    private String kellyOriginalFilename;
    private String kellyRenamedFilename;

}

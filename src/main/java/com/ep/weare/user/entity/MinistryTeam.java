package com.ep.weare.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "ministry_team")
public class MinistryTeam {

    @Id
    private String msteamName;

}

package com.ep.weare.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "executives_rank")
public class ExecutivesRank {
    @Id
    private String rankName;
}

package com.ep.weare.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "authority")
public class Authority {

    @Id
    private String userId;
    private String authority;

}

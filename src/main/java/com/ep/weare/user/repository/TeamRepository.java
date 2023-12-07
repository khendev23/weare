package com.ep.weare.user.repository;

import com.ep.weare.user.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, String> {
}

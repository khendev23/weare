package com.ep.weare.admin.repository;

import com.ep.weare.admin.entity.Kelly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KellyRepository extends JpaRepository<Kelly, Long> {

    Optional<Kelly> findFirstByOrderByKellyIdDesc();

    List<Kelly> findTop3ByOrderByKellyIdDesc();

    List<Kelly> findAllByOrderByKellyIdDesc();
}

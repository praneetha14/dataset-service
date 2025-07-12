package com.dataset.service.repository;

import com.dataset.service.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, UUID> {
}

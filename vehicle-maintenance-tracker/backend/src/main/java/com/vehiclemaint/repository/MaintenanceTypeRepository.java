package com.vehiclemaint.repository;

import com.vehiclemaint.model.MaintenanceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceTypeRepository extends JpaRepository<MaintenanceType, Long> {

    boolean existsByNombre(String nombre);
}

package com.vehiclemaint.repository;

import com.vehiclemaint.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByPlaca(String placa);

    boolean existsByPlaca(String placa);

    List<Vehicle> findByOwnerId(Long ownerId);
}

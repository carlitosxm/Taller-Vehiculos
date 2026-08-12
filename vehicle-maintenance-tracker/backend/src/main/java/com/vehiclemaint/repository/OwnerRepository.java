package com.vehiclemaint.repository;

import com.vehiclemaint.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Optional<Owner> findByCedulaORuc(String cedulaORuc);

    boolean existsByCedulaORuc(String cedulaORuc);
}

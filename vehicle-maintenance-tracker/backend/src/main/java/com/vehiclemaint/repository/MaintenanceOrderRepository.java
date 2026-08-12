package com.vehiclemaint.repository;

import com.vehiclemaint.model.MaintenanceOrder;
import com.vehiclemaint.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceOrderRepository extends JpaRepository<MaintenanceOrder, Long> {

    List<MaintenanceOrder> findByEstado(OrderStatus estado);

    List<MaintenanceOrder> findByVehicleId(Long vehicleId);

    List<MaintenanceOrder> findByFechaProgramadaBetweenAndEstadoNot(
            LocalDate start, LocalDate end, OrderStatus estado);

    List<MaintenanceOrder> findByFechaRealizacionBetween(LocalDate start, LocalDate end);
}

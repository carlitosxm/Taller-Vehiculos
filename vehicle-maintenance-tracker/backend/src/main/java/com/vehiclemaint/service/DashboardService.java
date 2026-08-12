package com.vehiclemaint.service;

import com.vehiclemaint.dto.DashboardAlertDTO;
import com.vehiclemaint.dto.DashboardCostDTO;
import com.vehiclemaint.dto.DashboardSummaryDTO;
import com.vehiclemaint.model.MaintenanceOrder;
import com.vehiclemaint.model.OrderStatus;
import com.vehiclemaint.repository.MaintenanceOrderRepository;
import com.vehiclemaint.repository.OwnerRepository;
import com.vehiclemaint.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private static final int URGENT_THRESHOLD_DAYS = 3;
    private static final int UPCOMING_WINDOW_DAYS = 7;
    private static final int COSTS_WINDOW_DAYS = 30;

    private final VehicleRepository vehicleRepository;
    private final OwnerRepository ownerRepository;
    private final MaintenanceOrderRepository maintenanceOrderRepository;

    public DashboardService(VehicleRepository vehicleRepository,
                             OwnerRepository ownerRepository,
                             MaintenanceOrderRepository maintenanceOrderRepository) {
        this.vehicleRepository = vehicleRepository;
        this.ownerRepository = ownerRepository;
        this.maintenanceOrderRepository = maintenanceOrderRepository;
    }

    public DashboardSummaryDTO getSummary() {
        DashboardSummaryDTO summary = new DashboardSummaryDTO();
        summary.setTotalVehicles(vehicleRepository.count());
        summary.setTotalOwners(ownerRepository.count());
        summary.setPendingOrders(maintenanceOrderRepository.findByEstado(OrderStatus.PENDING).size());
        summary.setInProcessOrders(maintenanceOrderRepository.findByEstado(OrderStatus.IN_PROCESS).size());
        summary.setCompletedOrders(maintenanceOrderRepository.findByEstado(OrderStatus.COMPLETED).size());
        return summary;
    }

    public List<DashboardAlertDTO> getAlerts() {
        LocalDate today = LocalDate.now();
        LocalDate windowEnd = today.plusDays(UPCOMING_WINDOW_DAYS);
        return maintenanceOrderRepository
                .findByFechaProgramadaBetweenAndEstadoNot(today, windowEnd, OrderStatus.COMPLETED)
                .stream()
                .sorted(Comparator.comparing(MaintenanceOrder::getFechaProgramada))
                .map(order -> toAlertDTO(order, today))
                .toList();
    }

    public List<DashboardCostDTO> getCosts() {
        LocalDate today = LocalDate.now();
        LocalDate windowStart = today.minusDays(COSTS_WINDOW_DAYS);
        List<MaintenanceOrder> recentOrders = maintenanceOrderRepository
                .findByFechaRealizacionBetween(windowStart, today);

        Map<Long, List<MaintenanceOrder>> byVehicle = recentOrders.stream()
                .collect(Collectors.groupingBy(order -> order.getVehicle().getId()));

        return byVehicle.values().stream()
                .map(orders -> {
                    BigDecimal total = orders.stream()
                            .map(MaintenanceOrder::getCosto)
                            .filter(costo -> costo != null)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new DashboardCostDTO(
                            orders.get(0).getVehicle().getId(),
                            orders.get(0).getVehicle().getPlaca(),
                            total);
                })
                .sorted(Comparator.comparing(DashboardCostDTO::getTotalCosto).reversed())
                .toList();
    }

    private DashboardAlertDTO toAlertDTO(MaintenanceOrder order, LocalDate today) {
        DashboardAlertDTO dto = new DashboardAlertDTO();
        dto.setOrderId(order.getId());
        dto.setVehiclePlaca(order.getVehicle().getPlaca());
        dto.setMaintenanceTypeNombre(order.getMaintenanceType().getNombre());
        dto.setFechaProgramada(order.getFechaProgramada());
        long daysUntil = ChronoUnit.DAYS.between(today, order.getFechaProgramada());
        dto.setUrgencia(daysUntil <= URGENT_THRESHOLD_DAYS ? "URGENTE" : "PROXIMO");
        return dto;
    }
}

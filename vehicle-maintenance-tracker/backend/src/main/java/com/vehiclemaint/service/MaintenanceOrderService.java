package com.vehiclemaint.service;

import com.vehiclemaint.dto.MaintenanceOrderDTO;
import com.vehiclemaint.exception.ResourceNotFoundException;
import com.vehiclemaint.model.MaintenanceOrder;
import com.vehiclemaint.model.MaintenanceType;
import com.vehiclemaint.model.OrderStatus;
import com.vehiclemaint.model.Vehicle;
import com.vehiclemaint.repository.MaintenanceOrderRepository;
import com.vehiclemaint.repository.MaintenanceTypeRepository;
import com.vehiclemaint.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MaintenanceOrderService {

    private final MaintenanceOrderRepository maintenanceOrderRepository;
    private final VehicleRepository vehicleRepository;
    private final MaintenanceTypeRepository maintenanceTypeRepository;

    public MaintenanceOrderService(MaintenanceOrderRepository maintenanceOrderRepository,
                                    VehicleRepository vehicleRepository,
                                    MaintenanceTypeRepository maintenanceTypeRepository) {
        this.maintenanceOrderRepository = maintenanceOrderRepository;
        this.vehicleRepository = vehicleRepository;
        this.maintenanceTypeRepository = maintenanceTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<MaintenanceOrderDTO> findAll() {
        return maintenanceOrderRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public MaintenanceOrderDTO findById(Long id) {
        return toDTO(findEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<MaintenanceOrderDTO> findByStatus(OrderStatus status) {
        return maintenanceOrderRepository.findByEstado(status).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<MaintenanceOrderDTO> findByVehicleId(Long vehicleId) {
        return maintenanceOrderRepository.findByVehicleId(vehicleId).stream().map(this::toDTO).toList();
    }

    public MaintenanceOrderDTO create(MaintenanceOrderDTO dto) {
        Vehicle vehicle = findVehicleById(dto.getVehicleId());
        MaintenanceType type = findTypeById(dto.getMaintenanceTypeId());
        MaintenanceOrder order = new MaintenanceOrder();
        applyDTO(order, dto, vehicle, type);
        return toDTO(maintenanceOrderRepository.save(order));
    }

    public MaintenanceOrderDTO update(Long id, MaintenanceOrderDTO dto) {
        MaintenanceOrder order = findEntityById(id);
        Vehicle vehicle = findVehicleById(dto.getVehicleId());
        MaintenanceType type = findTypeById(dto.getMaintenanceTypeId());
        applyDTO(order, dto, vehicle, type);
        return toDTO(maintenanceOrderRepository.save(order));
    }

    public void delete(Long id) {
        MaintenanceOrder order = findEntityById(id);
        maintenanceOrderRepository.delete(order);
    }

    private MaintenanceOrder findEntityById(Long id) {
        return maintenanceOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de mantenimiento no encontrada con id: " + id));
    }

    private Vehicle findVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con id: " + id));
    }

    private MaintenanceType findTypeById(Long id) {
        return maintenanceTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de mantenimiento no encontrado con id: " + id));
    }

    private void applyDTO(MaintenanceOrder order, MaintenanceOrderDTO dto, Vehicle vehicle, MaintenanceType type) {
        order.setVehicle(vehicle);
        order.setMaintenanceType(type);
        order.setDescripcion(dto.getDescripcion());
        order.setEstado(dto.getEstado() != null ? dto.getEstado() : OrderStatus.PENDING);
        order.setFechaProgramada(dto.getFechaProgramada());
        order.setFechaRealizacion(dto.getFechaRealizacion());
        order.setCosto(dto.getCosto());
        order.setTecnicoResponsable(dto.getTecnicoResponsable());
        order.setNotas(dto.getNotas());
    }

    MaintenanceOrderDTO toDTO(MaintenanceOrder order) {
        MaintenanceOrderDTO dto = new MaintenanceOrderDTO();
        dto.setId(order.getId());
        dto.setVehicleId(order.getVehicle().getId());
        dto.setVehiclePlaca(order.getVehicle().getPlaca());
        dto.setMaintenanceTypeId(order.getMaintenanceType().getId());
        dto.setMaintenanceTypeNombre(order.getMaintenanceType().getNombre());
        dto.setDescripcion(order.getDescripcion());
        dto.setEstado(order.getEstado());
        dto.setFechaProgramada(order.getFechaProgramada());
        dto.setFechaRealizacion(order.getFechaRealizacion());
        dto.setCosto(order.getCosto());
        dto.setTecnicoResponsable(order.getTecnicoResponsable());
        dto.setNotas(order.getNotas());
        dto.setFechaCreacion(order.getFechaCreacion());
        return dto;
    }
}

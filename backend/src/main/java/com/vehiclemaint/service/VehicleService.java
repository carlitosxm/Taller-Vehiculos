package com.vehiclemaint.service;

import com.vehiclemaint.dto.MaintenanceOrderDTO;
import com.vehiclemaint.dto.VehicleDTO;
import com.vehiclemaint.exception.DuplicateResourceException;
import com.vehiclemaint.exception.ResourceNotFoundException;
import com.vehiclemaint.model.Owner;
import com.vehiclemaint.model.Vehicle;
import com.vehiclemaint.model.VehicleStatus;
import com.vehiclemaint.repository.OwnerRepository;
import com.vehiclemaint.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final OwnerRepository ownerRepository;
    private final MaintenanceOrderService maintenanceOrderService;

    public VehicleService(VehicleRepository vehicleRepository,
                           OwnerRepository ownerRepository,
                           MaintenanceOrderService maintenanceOrderService) {
        this.vehicleRepository = vehicleRepository;
        this.ownerRepository = ownerRepository;
        this.maintenanceOrderService = maintenanceOrderService;
    }

    @Transactional(readOnly = true)
    public List<VehicleDTO> findAll() {
        return vehicleRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public VehicleDTO findById(Long id) {
        return toDTO(findEntityById(id));
    }

    @Transactional(readOnly = true)
    public VehicleDTO findByPlaca(String placa) {
        Vehicle vehicle = vehicleRepository.findByPlaca(placa)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con placa: " + placa));
        return toDTO(vehicle);
    }

    @Transactional(readOnly = true)
    public List<MaintenanceOrderDTO> findMaintenanceHistory(Long vehicleId) {
        findEntityById(vehicleId);
        return maintenanceOrderService.findByVehicleId(vehicleId);
    }

    public VehicleDTO create(VehicleDTO dto) {
        if (vehicleRepository.existsByPlaca(dto.getPlaca())) {
            throw new DuplicateResourceException("Ya existe un vehículo con placa: " + dto.getPlaca());
        }
        Owner owner = findOwnerById(dto.getOwnerId());
        Vehicle vehicle = new Vehicle();
        applyDTO(vehicle, dto, owner);
        return toDTO(vehicleRepository.save(vehicle));
    }

    public VehicleDTO update(Long id, VehicleDTO dto) {
        Vehicle vehicle = findEntityById(id);
        if (!vehicle.getPlaca().equals(dto.getPlaca()) && vehicleRepository.existsByPlaca(dto.getPlaca())) {
            throw new DuplicateResourceException("Ya existe un vehículo con placa: " + dto.getPlaca());
        }
        Owner owner = findOwnerById(dto.getOwnerId());
        applyDTO(vehicle, dto, owner);
        return toDTO(vehicleRepository.save(vehicle));
    }

    public void delete(Long id) {
        Vehicle vehicle = findEntityById(id);
        vehicleRepository.delete(vehicle);
    }

    private Vehicle findEntityById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con id: " + id));
    }

    private Owner findOwnerById(Long ownerId) {
        return ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado con id: " + ownerId));
    }

    private void applyDTO(Vehicle vehicle, VehicleDTO dto, Owner owner) {
        vehicle.setPlaca(dto.getPlaca());
        vehicle.setMarca(dto.getMarca());
        vehicle.setModelo(dto.getModelo());
        vehicle.setAnio(dto.getAnio());
        vehicle.setOwner(owner);
        vehicle.setEstado(dto.getEstado() != null ? dto.getEstado() : VehicleStatus.ACTIVO);
    }

    private VehicleDTO toDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setPlaca(vehicle.getPlaca());
        dto.setMarca(vehicle.getMarca());
        dto.setModelo(vehicle.getModelo());
        dto.setAnio(vehicle.getAnio());
        dto.setOwnerId(vehicle.getOwner().getId());
        dto.setOwnerNombreCompleto(vehicle.getOwner().getNombre() + " " + vehicle.getOwner().getApellido());
        dto.setEstado(vehicle.getEstado());
        dto.setFechaRegistro(vehicle.getFechaRegistro());
        return dto;
    }
}

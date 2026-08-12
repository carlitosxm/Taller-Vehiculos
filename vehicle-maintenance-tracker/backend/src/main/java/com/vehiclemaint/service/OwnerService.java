package com.vehiclemaint.service;

import com.vehiclemaint.dto.OwnerDTO;
import com.vehiclemaint.dto.VehicleDTO;
import com.vehiclemaint.exception.DuplicateResourceException;
import com.vehiclemaint.exception.ResourceNotFoundException;
import com.vehiclemaint.model.Owner;
import com.vehiclemaint.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Transactional(readOnly = true)
    public List<OwnerDTO> findAll() {
        return ownerRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public OwnerDTO findById(Long id) {
        return toDTO(findEntityById(id));
    }

    @Transactional(readOnly = true)
    public OwnerDTO findByCedulaORuc(String cedulaORuc) {
        Owner owner = ownerRepository.findByCedulaORuc(cedulaORuc)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado con cédula/RUC: " + cedulaORuc));
        return toDTO(owner);
    }

    @Transactional(readOnly = true)
    public List<VehicleDTO> findVehiclesByOwnerId(Long ownerId) {
        Owner owner = findEntityById(ownerId);
        return owner.getVehicles().stream().map(vehicle -> {
            VehicleDTO dto = new VehicleDTO();
            dto.setId(vehicle.getId());
            dto.setPlaca(vehicle.getPlaca());
            dto.setMarca(vehicle.getMarca());
            dto.setModelo(vehicle.getModelo());
            dto.setAnio(vehicle.getAnio());
            dto.setOwnerId(owner.getId());
            dto.setOwnerNombreCompleto(owner.getNombre() + " " + owner.getApellido());
            dto.setEstado(vehicle.getEstado());
            dto.setFechaRegistro(vehicle.getFechaRegistro());
            return dto;
        }).toList();
    }

    public OwnerDTO create(OwnerDTO dto) {
        if (ownerRepository.existsByCedulaORuc(dto.getCedulaORuc())) {
            throw new DuplicateResourceException("Ya existe un propietario con cédula/RUC: " + dto.getCedulaORuc());
        }
        Owner owner = new Owner();
        applyDTO(owner, dto);
        return toDTO(ownerRepository.save(owner));
    }

    public OwnerDTO update(Long id, OwnerDTO dto) {
        Owner owner = findEntityById(id);
        if (!owner.getCedulaORuc().equals(dto.getCedulaORuc())
                && ownerRepository.existsByCedulaORuc(dto.getCedulaORuc())) {
            throw new DuplicateResourceException("Ya existe un propietario con cédula/RUC: " + dto.getCedulaORuc());
        }
        applyDTO(owner, dto);
        return toDTO(ownerRepository.save(owner));
    }

    public void delete(Long id) {
        Owner owner = findEntityById(id);
        ownerRepository.delete(owner);
    }

    private Owner findEntityById(Long id) {
        return ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado con id: " + id));
    }

    private void applyDTO(Owner owner, OwnerDTO dto) {
        owner.setNombre(dto.getNombre());
        owner.setApellido(dto.getApellido());
        owner.setDireccion(dto.getDireccion());
        owner.setTelefono(dto.getTelefono());
        owner.setCedulaORuc(dto.getCedulaORuc());
    }

    private OwnerDTO toDTO(Owner owner) {
        OwnerDTO dto = new OwnerDTO();
        dto.setId(owner.getId());
        dto.setNombre(owner.getNombre());
        dto.setApellido(owner.getApellido());
        dto.setDireccion(owner.getDireccion());
        dto.setTelefono(owner.getTelefono());
        dto.setCedulaORuc(owner.getCedulaORuc());
        dto.setFechaRegistro(owner.getFechaRegistro());
        return dto;
    }
}

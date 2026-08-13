package com.vehiclemaint.service;

import com.vehiclemaint.dto.MaintenanceTypeDTO;
import com.vehiclemaint.exception.DuplicateResourceException;
import com.vehiclemaint.model.MaintenanceType;
import com.vehiclemaint.repository.MaintenanceTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MaintenanceTypeService {

    private final MaintenanceTypeRepository maintenanceTypeRepository;

    public MaintenanceTypeService(MaintenanceTypeRepository maintenanceTypeRepository) {
        this.maintenanceTypeRepository = maintenanceTypeRepository;
    }

    @Transactional(readOnly = true)
    public List<MaintenanceTypeDTO> findAll() {
        return maintenanceTypeRepository.findAll().stream().map(this::toDTO).toList();
    }

    public MaintenanceTypeDTO create(MaintenanceTypeDTO dto) {
        if (maintenanceTypeRepository.existsByNombre(dto.getNombre())) {
            throw new DuplicateResourceException("Ya existe un tipo de mantenimiento con nombre: " + dto.getNombre());
        }
        MaintenanceType type = new MaintenanceType(dto.getNombre(), dto.getDescripcion());
        return toDTO(maintenanceTypeRepository.save(type));
    }

    private MaintenanceTypeDTO toDTO(MaintenanceType type) {
        MaintenanceTypeDTO dto = new MaintenanceTypeDTO();
        dto.setId(type.getId());
        dto.setNombre(type.getNombre());
        dto.setDescripcion(type.getDescripcion());
        return dto;
    }
}

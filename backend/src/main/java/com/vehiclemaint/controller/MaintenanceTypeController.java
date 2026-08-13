package com.vehiclemaint.controller;

import com.vehiclemaint.dto.MaintenanceTypeDTO;
import com.vehiclemaint.service.MaintenanceTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance-types")
public class MaintenanceTypeController {

    private final MaintenanceTypeService maintenanceTypeService;

    public MaintenanceTypeController(MaintenanceTypeService maintenanceTypeService) {
        this.maintenanceTypeService = maintenanceTypeService;
    }

    @GetMapping
    public List<MaintenanceTypeDTO> findAll() {
        return maintenanceTypeService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaintenanceTypeDTO create(@Valid @RequestBody MaintenanceTypeDTO dto) {
        return maintenanceTypeService.create(dto);
    }
}

package com.vehiclemaint.controller;

import com.vehiclemaint.dto.MaintenanceOrderDTO;
import com.vehiclemaint.dto.VehicleDTO;
import com.vehiclemaint.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<VehicleDTO> findAll() {
        return vehicleService.findAll();
    }

    @GetMapping("/{id}")
    public VehicleDTO findById(@PathVariable Long id) {
        return vehicleService.findById(id);
    }

    @GetMapping("/placa/{placa}")
    public VehicleDTO findByPlaca(@PathVariable String placa) {
        return vehicleService.findByPlaca(placa);
    }

    @GetMapping("/{id}/maintenance")
    public List<MaintenanceOrderDTO> findMaintenanceHistory(@PathVariable Long id) {
        return vehicleService.findMaintenanceHistory(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleDTO create(@Valid @RequestBody VehicleDTO dto) {
        return vehicleService.create(dto);
    }

    @PutMapping("/{id}")
    public VehicleDTO update(@PathVariable Long id, @Valid @RequestBody VehicleDTO dto) {
        return vehicleService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

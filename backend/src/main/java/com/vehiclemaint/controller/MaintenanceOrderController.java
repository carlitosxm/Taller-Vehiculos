package com.vehiclemaint.controller;

import com.vehiclemaint.dto.MaintenanceOrderDTO;
import com.vehiclemaint.model.OrderStatus;
import com.vehiclemaint.service.MaintenanceOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance-orders")
public class MaintenanceOrderController {

    private final MaintenanceOrderService maintenanceOrderService;

    public MaintenanceOrderController(MaintenanceOrderService maintenanceOrderService) {
        this.maintenanceOrderService = maintenanceOrderService;
    }

    @GetMapping
    public List<MaintenanceOrderDTO> findAll(@RequestParam(required = false) OrderStatus status) {
        return status != null ? maintenanceOrderService.findByStatus(status) : maintenanceOrderService.findAll();
    }

    @GetMapping("/{id}")
    public MaintenanceOrderDTO findById(@PathVariable Long id) {
        return maintenanceOrderService.findById(id);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<MaintenanceOrderDTO> findByVehicle(@PathVariable Long vehicleId) {
        return maintenanceOrderService.findByVehicleId(vehicleId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaintenanceOrderDTO create(@Valid @RequestBody MaintenanceOrderDTO dto) {
        return maintenanceOrderService.create(dto);
    }

    @PutMapping("/{id}")
    public MaintenanceOrderDTO update(@PathVariable Long id, @Valid @RequestBody MaintenanceOrderDTO dto) {
        return maintenanceOrderService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maintenanceOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

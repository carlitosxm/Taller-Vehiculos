package com.vehiclemaint.controller;

import com.vehiclemaint.dto.OwnerDTO;
import com.vehiclemaint.dto.VehicleDTO;
import com.vehiclemaint.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public List<OwnerDTO> findAll() {
        return ownerService.findAll();
    }

    @GetMapping("/{id}")
    public OwnerDTO findById(@PathVariable Long id) {
        return ownerService.findById(id);
    }

    @GetMapping("/cedula/{cedulaOruc}")
    public OwnerDTO findByCedula(@PathVariable String cedulaOruc) {
        return ownerService.findByCedulaORuc(cedulaOruc);
    }

    @GetMapping("/{id}/vehicles")
    public List<VehicleDTO> findVehicles(@PathVariable Long id) {
        return ownerService.findVehiclesByOwnerId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OwnerDTO create(@Valid @RequestBody OwnerDTO dto) {
        return ownerService.create(dto);
    }

    @PutMapping("/{id}")
    public OwnerDTO update(@PathVariable Long id, @Valid @RequestBody OwnerDTO dto) {
        return ownerService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ownerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

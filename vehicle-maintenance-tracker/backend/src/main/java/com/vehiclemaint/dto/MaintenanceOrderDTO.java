package com.vehiclemaint.dto;

import com.vehiclemaint.model.OrderStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MaintenanceOrderDTO {

    private Long id;

    @NotNull(message = "El vehículo es obligatorio")
    private Long vehicleId;

    private String vehiclePlaca;

    @NotNull(message = "El tipo de mantenimiento es obligatorio")
    private Long maintenanceTypeId;

    private String maintenanceTypeNombre;

    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    private String descripcion;

    private OrderStatus estado;

    @FutureOrPresent(message = "La fecha programada debe ser hoy o posterior")
    private LocalDate fechaProgramada;

    private LocalDate fechaRealizacion;

    @DecimalMin(value = "0.0", inclusive = true, message = "El costo debe ser mayor o igual a 0")
    @Digits(integer = 8, fraction = 2, message = "El costo admite máximo 2 decimales")
    private BigDecimal costo;

    @Size(max = 100, message = "El técnico responsable no puede superar 100 caracteres")
    private String tecnicoResponsable;

    @Size(max = 1000, message = "Las notas no pueden superar 1000 caracteres")
    private String notas;

    private LocalDateTime fechaCreacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehiclePlaca() {
        return vehiclePlaca;
    }

    public void setVehiclePlaca(String vehiclePlaca) {
        this.vehiclePlaca = vehiclePlaca;
    }

    public Long getMaintenanceTypeId() {
        return maintenanceTypeId;
    }

    public void setMaintenanceTypeId(Long maintenanceTypeId) {
        this.maintenanceTypeId = maintenanceTypeId;
    }

    public String getMaintenanceTypeNombre() {
        return maintenanceTypeNombre;
    }

    public void setMaintenanceTypeNombre(String maintenanceTypeNombre) {
        this.maintenanceTypeNombre = maintenanceTypeNombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public OrderStatus getEstado() {
        return estado;
    }

    public void setEstado(OrderStatus estado) {
        this.estado = estado;
    }

    public LocalDate getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDate fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public LocalDate getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(LocalDate fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public String getTecnicoResponsable() {
        return tecnicoResponsable;
    }

    public void setTecnicoResponsable(String tecnicoResponsable) {
        this.tecnicoResponsable = tecnicoResponsable;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}

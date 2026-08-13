package com.vehiclemaint.dto;

import java.time.LocalDate;

public class DashboardAlertDTO {

    private Long orderId;
    private String vehiclePlaca;
    private String maintenanceTypeNombre;
    private LocalDate fechaProgramada;
    private String urgencia;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getVehiclePlaca() {
        return vehiclePlaca;
    }

    public void setVehiclePlaca(String vehiclePlaca) {
        this.vehiclePlaca = vehiclePlaca;
    }

    public String getMaintenanceTypeNombre() {
        return maintenanceTypeNombre;
    }

    public void setMaintenanceTypeNombre(String maintenanceTypeNombre) {
        this.maintenanceTypeNombre = maintenanceTypeNombre;
    }

    public LocalDate getFechaProgramada() {
        return fechaProgramada;
    }

    public void setFechaProgramada(LocalDate fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public String getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(String urgencia) {
        this.urgencia = urgencia;
    }
}

package com.vehiclemaint.dto;

import java.math.BigDecimal;

public class DashboardCostDTO {

    private Long vehicleId;
    private String vehiclePlaca;
    private BigDecimal totalCosto;

    public DashboardCostDTO() {
    }

    public DashboardCostDTO(Long vehicleId, String vehiclePlaca, BigDecimal totalCosto) {
        this.vehicleId = vehicleId;
        this.vehiclePlaca = vehiclePlaca;
        this.totalCosto = totalCosto;
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

    public BigDecimal getTotalCosto() {
        return totalCosto;
    }

    public void setTotalCosto(BigDecimal totalCosto) {
        this.totalCosto = totalCosto;
    }
}

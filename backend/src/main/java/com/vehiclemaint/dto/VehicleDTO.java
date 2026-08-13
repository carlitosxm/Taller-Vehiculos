package com.vehiclemaint.dto;

import com.vehiclemaint.model.VehicleStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class VehicleDTO {

    private Long id;

    @NotBlank(message = "La placa es obligatoria")
    @Pattern(regexp = "^[A-Z]{3}-\\d{3,4}$", message = "Formato de placa inválido (ej: ABC-1234)")
    private String placa;

    @NotBlank(message = "La marca es obligatoria")
    @Size(min = 2, max = 100, message = "La marca debe tener entre 2 y 100 caracteres")
    private String marca;

    private String modelo;

    @Min(value = 1950, message = "El año debe ser mayor o igual a 1950")
    @Max(value = 2050, message = "El año debe ser menor o igual a 2050")
    private Integer anio;

    @NotNull(message = "El propietario es obligatorio")
    private Long ownerId;

    private String ownerNombreCompleto;

    private VehicleStatus estado;

    private LocalDateTime fechaRegistro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerNombreCompleto() {
        return ownerNombreCompleto;
    }

    public void setOwnerNombreCompleto(String ownerNombreCompleto) {
        this.ownerNombreCompleto = ownerNombreCompleto;
    }

    public VehicleStatus getEstado() {
        return estado;
    }

    public void setEstado(VehicleStatus estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}

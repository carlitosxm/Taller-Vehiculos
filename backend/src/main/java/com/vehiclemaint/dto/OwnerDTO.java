package com.vehiclemaint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class OwnerDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String apellido;

    @Size(max = 200, message = "La dirección no puede superar 200 caracteres")
    private String direccion;

    @Pattern(regexp = "^$|^[0-9+\\-\\s]{7,20}$", message = "Formato de teléfono inválido")
    private String telefono;

    @NotBlank(message = "La cédula/RUC es obligatoria")
    @Pattern(regexp = "^\\d{10}(\\d{3})?$", message = "Formato de cédula/RUC inválido")
    private String cedulaORuc;

    private LocalDateTime fechaRegistro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCedulaORuc() {
        return cedulaORuc;
    }

    public void setCedulaORuc(String cedulaORuc) {
        this.cedulaORuc = cedulaORuc;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}

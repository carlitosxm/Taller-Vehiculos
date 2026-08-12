package com.vehiclemaint.config;

import com.vehiclemaint.model.MaintenanceType;
import com.vehiclemaint.repository.MaintenanceTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    private static final List<MaintenanceType> DEFAULT_TYPES = List.of(
            new MaintenanceType("Cambio de aceite", "Sustitución de aceite y filtro de motor"),
            new MaintenanceType("Revisión de frenos", "Inspección y ajuste del sistema de frenos"),
            new MaintenanceType("Cambio de llantas", "Sustitución de neumáticos"),
            new MaintenanceType("Revisión general", "Chequeo general del vehículo"),
            new MaintenanceType("Alineación", "Alineación de dirección"),
            new MaintenanceType("Balanceo", "Balanceo de ruedas"),
            new MaintenanceType("Revisión de suspensión", "Inspección del sistema de suspensión"),
            new MaintenanceType("Revisión de motor", "Diagnóstico y revisión del motor")
    );

    @Bean
    CommandLineRunner seedMaintenanceTypes(MaintenanceTypeRepository repository) {
        return args -> DEFAULT_TYPES.stream()
                .filter(type -> !repository.existsByNombre(type.getNombre()))
                .forEach(repository::save);
    }
}

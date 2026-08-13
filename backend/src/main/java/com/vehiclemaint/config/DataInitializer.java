package com.vehiclemaint.config;

import com.vehiclemaint.model.MaintenanceOrder;
import com.vehiclemaint.model.MaintenanceType;
import com.vehiclemaint.model.OrderStatus;
import com.vehiclemaint.model.Owner;
import com.vehiclemaint.model.Vehicle;
import com.vehiclemaint.model.VehicleStatus;
import com.vehiclemaint.repository.MaintenanceOrderRepository;
import com.vehiclemaint.repository.MaintenanceTypeRepository;
import com.vehiclemaint.repository.OwnerRepository;
import com.vehiclemaint.repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    @Order(1)
    CommandLineRunner seedMaintenanceTypes(MaintenanceTypeRepository repository) {
        return args -> DEFAULT_TYPES.stream()
                .filter(type -> !repository.existsByNombre(type.getNombre()))
                .forEach(repository::save);
    }

    @Bean
    @Order(2)
    CommandLineRunner seedDemoData(OwnerRepository ownerRepository,
                                    VehicleRepository vehicleRepository,
                                    MaintenanceOrderRepository maintenanceOrderRepository,
                                    MaintenanceTypeRepository maintenanceTypeRepository) {
        return args -> {
            if (ownerRepository.count() > 0) {
                return;
            }

            Owner owner1 = ownerRepository.save(newOwner("María", "González", "Av. Amazonas N32-15, Quito", "0991234567", "1712345678"));
            Owner owner2 = ownerRepository.save(newOwner("Carlos", "Pérez", "Av. 6 de Diciembre y Colón, Quito", "0987654321", "1723456789"));
            Owner owner3 = ownerRepository.save(newOwner("Transportes del Valle S.A.", "", "Panamericana Sur km 12, Quito", "022345678", "1790123456001"));
            Owner owner4 = ownerRepository.save(newOwner("Ana", "Torres", "Calle Rumipamba y Naciones Unidas, Quito", "0976543210", "1734567890"));
            Owner owner5 = ownerRepository.save(newOwner("Diego", "Vásquez", "Av. Eloy Alfaro N45-20, Quito", "0965432109", "1745678901"));

            Vehicle vehicle1 = vehicleRepository.save(newVehicle("PBX-1234", "Chevrolet", "Aveo", 2019, owner1, VehicleStatus.ACTIVO));
            Vehicle vehicle2 = vehicleRepository.save(newVehicle("PCA-5678", "Toyota", "Corolla", 2021, owner2, VehicleStatus.ACTIVO));
            Vehicle vehicle3 = vehicleRepository.save(newVehicle("GKM-4455", "Hino", "300", 2018, owner3, VehicleStatus.ACTIVO));
            Vehicle vehicle4 = vehicleRepository.save(newVehicle("GKM-4487", "Hino", "300", 2020, owner3, VehicleStatus.ACTIVO));
            Vehicle vehicle5 = vehicleRepository.save(newVehicle("PDF-9012", "Mazda", "CX-5", 2022, owner4, VehicleStatus.ACTIVO));
            Vehicle vehicle6 = vehicleRepository.save(newVehicle("PEG-3344", "Kia", "Sportage", 2017, owner5, VehicleStatus.INACTIVO));
            Vehicle vehicle7 = vehicleRepository.save(newVehicle("PFH-6677", "Chevrolet", "D-Max", 2023, owner5, VehicleStatus.ACTIVO));

            Map<String, MaintenanceType> types = maintenanceTypeRepository.findAll().stream()
                    .collect(java.util.stream.Collectors.toMap(MaintenanceType::getNombre, t -> t));

            LocalDate today = LocalDate.now();

            // Órdenes completadas (últimos 30 días) → alimentan el reporte de costos
            saveOrder(maintenanceOrderRepository, vehicle1, types.get("Cambio de aceite"),
                    "Cambio de aceite 5W-30 y filtro", OrderStatus.COMPLETED,
                    today.minusDays(20), today.minusDays(20), new BigDecimal("45.00"), "Luis Ramírez", null);
            saveOrder(maintenanceOrderRepository, vehicle1, types.get("Revisión de frenos"),
                    "Cambio de pastillas delanteras", OrderStatus.COMPLETED,
                    today.minusDays(12), today.minusDays(12), new BigDecimal("80.00"), "Luis Ramírez", null);
            saveOrder(maintenanceOrderRepository, vehicle2, types.get("Alineación"),
                    "Alineación y balanceo", OrderStatus.COMPLETED,
                    today.minusDays(8), today.minusDays(8), new BigDecimal("35.00"), "Jorge Salazar", null);
            saveOrder(maintenanceOrderRepository, vehicle3, types.get("Revisión general"),
                    "Mantenimiento preventivo 20,000 km", OrderStatus.COMPLETED,
                    today.minusDays(25), today.minusDays(24), new BigDecimal("150.00"), "Luis Ramírez", "Vehículo de flota, revisar próxima cada 20,000 km");
            saveOrder(maintenanceOrderRepository, vehicle5, types.get("Cambio de llantas"),
                    "Cambio de las 4 llantas", OrderStatus.COMPLETED,
                    today.minusDays(5), today.minusDays(5), new BigDecimal("320.00"), "Jorge Salazar", null);

            // Órdenes en proceso
            saveOrder(maintenanceOrderRepository, vehicle4, types.get("Revisión de motor"),
                    "Diagnóstico por pérdida de potencia", OrderStatus.IN_PROCESS,
                    today.minusDays(1), null, null, "Luis Ramírez", "Esperando repuesto de inyector");
            saveOrder(maintenanceOrderRepository, vehicle7, types.get("Balanceo"),
                    "Balanceo tras cambio de llantas", OrderStatus.IN_PROCESS,
                    today, null, null, "Jorge Salazar", null);

            // Órdenes pendientes próximas (dentro de la ventana de alertas del dashboard)
            saveOrder(maintenanceOrderRepository, vehicle2, types.get("Revisión de suspensión"),
                    "Ruido en suspensión delantera", OrderStatus.PENDING,
                    today.plusDays(2), null, null, null, null);
            saveOrder(maintenanceOrderRepository, vehicle3, types.get("Cambio de aceite"),
                    "Cambio de aceite programado", OrderStatus.PENDING,
                    today.plusDays(3), null, new BigDecimal("60.00"), null, null);
            saveOrder(maintenanceOrderRepository, vehicle5, types.get("Revisión de frenos"),
                    "Revisión periódica de frenos", OrderStatus.PENDING,
                    today.plusDays(6), null, null, null, null);
            saveOrder(maintenanceOrderRepository, vehicle1, types.get("Revisión general"),
                    "Mantenimiento preventivo semestral", OrderStatus.PENDING,
                    today.plusDays(15), null, null, null, "Fuera de la ventana de alertas, sirve para probar filtros");
        };
    }

    private Owner newOwner(String nombre, String apellido, String direccion, String telefono, String cedulaORuc) {
        Owner owner = new Owner();
        owner.setNombre(nombre);
        owner.setApellido(apellido);
        owner.setDireccion(direccion);
        owner.setTelefono(telefono);
        owner.setCedulaORuc(cedulaORuc);
        return owner;
    }

    private Vehicle newVehicle(String placa, String marca, String modelo, int anio, Owner owner, VehicleStatus estado) {
        Vehicle vehicle = new Vehicle();
        vehicle.setPlaca(placa);
        vehicle.setMarca(marca);
        vehicle.setModelo(modelo);
        vehicle.setAnio(anio);
        vehicle.setOwner(owner);
        vehicle.setEstado(estado);
        return vehicle;
    }

    private void saveOrder(MaintenanceOrderRepository repository, Vehicle vehicle, MaintenanceType type,
                            String descripcion, OrderStatus estado, LocalDate fechaProgramada,
                            LocalDate fechaRealizacion, BigDecimal costo, String tecnico, String notas) {
        MaintenanceOrder order = new MaintenanceOrder();
        order.setVehicle(vehicle);
        order.setMaintenanceType(type);
        order.setDescripcion(descripcion);
        order.setEstado(estado);
        order.setFechaProgramada(fechaProgramada);
        order.setFechaRealizacion(fechaRealizacion);
        order.setCosto(costo);
        order.setTecnicoResponsable(tecnico);
        order.setNotas(notas);
        repository.save(order);
    }
}

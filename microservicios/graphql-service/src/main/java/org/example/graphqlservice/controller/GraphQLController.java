package org.example.graphqlservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.graphqlservice.model.FleetSummary;
import org.example.graphqlservice.model.OrderDTO;
import org.example.graphqlservice.model.OrderFilterInput;
import org.example.graphqlservice.model.VehicleDTO;
import org.example.graphqlservice.service.GraphQLDataService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GraphQLController {
    private final GraphQLDataService dataService;

    // se mapea la query "orders" del schema, cuando alguien pide { orders... } se entra aquí
    @QueryMapping
    public List<OrderDTO> orders(@Argument OrderFilterInput filter) {
        String location = (filter != null) ? filter.deliveryLocation() : null;
        String status = (filter != null) ? filter.status() : null;

        return dataService.getOrders(location, status);
    }

    // se mapea la query "activeFleet" del schema
    @QueryMapping
    public FleetSummary activeFleet(@Argument String location) {
        // se simulan datos para cumplir el requisit, en un futuro, esto llamaría a un endpoint de estadisticas de FleetService
        return new FleetSummary(15, 10, 5);
    }

    // resolver anidado (Schema Mapping), esto conecta el microservicio de Pedidos con el de Flotas, se ejecuta solo si el usuario pide el campo "vehicle"
    @SchemaMapping(typeName = "Order")
    public VehicleDTO vehicle(OrderDTO order) {
        if (order.assignedVehicleId() == null) {
            return null; // Si la orden no tiene vehículo, no hacemos llamada
        }
        // se usa el ID que viene de la orden para buscar el vehículo en el otro servicio
        return dataService.getVehicleById(order.assignedVehicleId());
    }
}

package org.example.graphqlservice.service;

import lombok.RequiredArgsConstructor;
import org.example.graphqlservice.model.OrderDTO;
import org.example.graphqlservice.model.VehicleDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GraphQLDataService {
    private final WebClient.Builder webClientBuilder;

    private final String ORDER_SERVICE_URL = "http://localhost:8083/orders";
    private final String FLEET_SERVICE_URL = "http://localhost:8082/fleet";

    // Obtener todos los pedidos, recibe filtros opcionales (location, status)
    public List<OrderDTO> getOrders(String deliveryLocation, String status) {

        return webClientBuilder.build()        // se llama al microservicio de Orders
                .get()
                .uri(ORDER_SERVICE_URL) // se asume que GET /api/orders devuelve la lista
                .retrieve()
                .bodyToFlux(OrderDTO.class) // se convierte el JSON a objetos OrderDTO
                .filter(order -> {
                    // logica de filtrado manual, si el filtro es nulo, pasa. si tiene valor, se compara
                    boolean matchLocation = (deliveryLocation == null) ||
                            (order.deliveryLocation() != null && order.deliveryLocation().contains(deliveryLocation));

                    boolean matchStatus = (status == null) ||
                            (order.status() != null && order.status().equals(status));

                    return matchLocation && matchStatus;
                })
                .collectList() // se recolecta todo en una lista Java
                .block(); // se espera la respuesta de forma síncrona para GraphQL
    }

    // Obtener un Vehículo por ID
    public VehicleDTO getVehicleById(Long id) {
        if (id == null) return null;

        return webClientBuilder.build()
                .get()
                .uri(FLEET_SERVICE_URL + "/vehicles/" + id) // ruta /fleet/vehicles/{id}
                .retrieve()
                .bodyToMono(VehicleDTO.class)
                .onErrorResume(e -> Mono.empty()) // si falla o no existe, retorna null y no explota
                .block();
    }
}

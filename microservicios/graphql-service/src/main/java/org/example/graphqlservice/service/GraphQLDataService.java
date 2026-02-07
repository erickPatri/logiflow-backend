package org.example.graphqlservice.service;

import lombok.RequiredArgsConstructor;
import org.example.graphqlservice.model.OrderDTO;
import org.example.graphqlservice.model.VehicleDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GraphQLDataService {
    private final WebClient.Builder webClientBuilder;

    // Le decimos a Spring: "Busca este valor en application.properties"
    @Value("${service.url.orders}")
    private String orderServiceUrl;

    @Value("${service.url.fleet}")
    private String fleetServiceUrl;

    public List<OrderDTO> getOrders(String deliveryLocation, String status) {
        return webClientBuilder.build()
                .get()
                .uri(orderServiceUrl) // Usamos la variable inyectada
                .retrieve()
                .bodyToFlux(OrderDTO.class)
                .filter(order -> {
                    boolean matchLocation = (deliveryLocation == null) ||
                            (order.deliveryLocation() != null && order.deliveryLocation().contains(deliveryLocation));
                    boolean matchStatus = (status == null) ||
                            (order.status() != null && order.status().equals(status));
                    return matchLocation && matchStatus;
                })
                .collectList()
                .block();
    }

    public VehicleDTO getVehicleById(Long id) {
        if (id == null) return null;

        return webClientBuilder.build()
                .get()
                .uri(fleetServiceUrl + "/vehicles/" + id) // Usamos la variable inyectada
                .retrieve()
                .bodyToMono(VehicleDTO.class)
                .onErrorResume(e -> Mono.empty())
                .block();
    }
}
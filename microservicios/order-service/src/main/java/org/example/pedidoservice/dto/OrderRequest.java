package org.example.pedidoservice.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private Long clientId;
    private String description;
    private String pickupLocation;
    private String deliveryLocation;
    private Double latitude;
    private Double longitude;
}

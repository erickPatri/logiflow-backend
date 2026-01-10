package org.example.graphqlservice.model;

public record OrderFilterInput(
        String deliveryLocation,
        String status
) {
}

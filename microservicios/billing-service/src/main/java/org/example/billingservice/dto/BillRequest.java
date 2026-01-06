package org.example.billingservice.dto;

import lombok.Data;

@Data
public class BillRequest {
    private Long orderId;
    private Long clientId;
}

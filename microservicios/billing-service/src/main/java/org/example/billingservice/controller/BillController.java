package org.example.billingservice.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.billingservice.dto.BillRequest;
import org.example.billingservice.model.Bill;
import org.example.billingservice.service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    // http://localhost:8084/bills
    @PostMapping
    public ResponseEntity<Bill> createInvoice(@RequestBody BillRequest request) {
        return ResponseEntity.ok(
                billService.generateDraftBill(request.getOrderId(), request.getClientId())
        );
    }
}

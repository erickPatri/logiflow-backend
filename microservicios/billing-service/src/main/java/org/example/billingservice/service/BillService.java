package org.example.billingservice.service;

import lombok.AllArgsConstructor;
import org.example.billingservice.model.Bill;
import org.example.billingservice.model.BillState;
import org.example.billingservice.repository.BillRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class BillService {
    private final BillRepository billRepository;

    public Bill generateDraftBill(Long orderId, Long clientId) {
        BigDecimal baseRate = new BigDecimal("5.00");  // por ahora logica simple de tarifa, seran $5 de manera fija, quizas luego dinamica

        Bill bill = Bill.builder()
                .orderId(orderId)
                .clientId(clientId)
                .amount(baseRate)
                .state(BillState.BORRADOR)
                .build();

        return billRepository.save(bill);
    }
}

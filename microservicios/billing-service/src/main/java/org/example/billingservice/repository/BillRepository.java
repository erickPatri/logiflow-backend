package org.example.billingservice.repository;

import org.example.billingservice.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByClientId(Long clientId);   // para buscar facturas segun por cliente
}
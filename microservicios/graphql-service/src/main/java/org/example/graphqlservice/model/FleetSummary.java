package org.example.graphqlservice.model;

public record FleetSummary(
        Integer total,
        Integer available,
        Integer onRoute
) {}

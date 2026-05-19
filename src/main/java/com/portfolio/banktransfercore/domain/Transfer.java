package com.portfolio.banktransfercore.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transfer {
    private UUID id;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public Transfer(UUID id, String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount, LocalDateTime createdAt) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto de la transferencia debe ser mayor a cero");
        }
        if (sourceAccountNumber.equals(destinationAccountNumber)) {
            throw new IllegalArgumentException("La cuenta de origen y destino no pueden ser iguales");
        }
        this.id = id;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public String getDestinationAccountNumber() { return destinationAccountNumber; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
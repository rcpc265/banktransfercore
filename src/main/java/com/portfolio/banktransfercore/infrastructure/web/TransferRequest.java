package com.portfolio.banktransfercore.infrastructure.web;

import java.math.BigDecimal;

public record TransferRequest(
    String sourceNumber, String destinationNumber, BigDecimal amount, String currency) {}

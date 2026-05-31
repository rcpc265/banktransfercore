package com.portfolio.banktransfercore.infrastructure.web;

import java.math.BigDecimal;

public record FundsTransferRequest(
    String sourceNumber, String destinationNumber, BigDecimal amount, String currency) {}

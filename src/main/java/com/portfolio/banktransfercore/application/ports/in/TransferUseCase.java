package com.portfolio.banktransfercore.application.ports.in;

import java.math.BigDecimal;

public interface TransferUseCase {
    void execute(String sourceNumber, String destinationNumber, BigDecimal amount);
}
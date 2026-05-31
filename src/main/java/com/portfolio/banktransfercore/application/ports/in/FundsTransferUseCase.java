package com.portfolio.banktransfercore.application.ports.in;

import java.math.BigDecimal;

public interface FundsTransferUseCase {
  void execute(String sourceNumber, String destinationNumber, BigDecimal amount, String currency);
}

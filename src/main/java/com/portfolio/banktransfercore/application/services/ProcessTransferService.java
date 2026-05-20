package com.portfolio.banktransfercore.application.services;

import com.portfolio.banktransfercore.application.ports.in.TransferUseCase;
import com.portfolio.banktransfercore.application.ports.out.AccountRepositoryPort;
import com.portfolio.banktransfercore.domain.Account;
import java.math.BigDecimal;

public class ProcessTransferService implements TransferUseCase {

    private final AccountRepositoryPort accountRepositoryPort;
    public ProcessTransferService(AccountRepositoryPort accountRepositoryPort) {
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public void execute(String sourceNumber, String destinationNumber, BigDecimal amount) {
        Account sourceAccount = accountRepositoryPort.findByAccountNumber(sourceNumber)
                .orElseThrow(() -> new IllegalArgumentException("La cuenta de origen no existe"));

        Account destinationAccount = accountRepositoryPort.findByAccountNumber(destinationNumber)
                .orElseThrow(() -> new IllegalArgumentException("La cuenta de destino no existe"));

        sourceAccount.debit(amount);
        destinationAccount.credit(amount);

        accountRepositoryPort.save(sourceAccount);
        accountRepositoryPort.save(destinationAccount);
    }
}
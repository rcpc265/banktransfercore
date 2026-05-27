package com.portfolio.banktransfercore.application.services;

import com.portfolio.banktransfercore.application.ports.in.TransferUseCase;
import com.portfolio.banktransfercore.application.ports.out.AccountRepositoryPort;
import com.portfolio.banktransfercore.domain.account.Account;
import com.portfolio.banktransfercore.domain.shared.money.Money;
import com.portfolio.banktransfercore.domain.shared.money.SupportedCurrency;
import java.math.BigDecimal;

public class ProcessTransferService implements TransferUseCase {

    private final AccountRepositoryPort accountRepositoryPort;
    public ProcessTransferService(AccountRepositoryPort accountRepositoryPort) {
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public void execute(
            String sourceNumber,
            String destinationNumber,
            BigDecimal amount,
            String currency
    ) {
        Account sourceAccount = accountRepositoryPort.findByAccountNumber(sourceNumber)
                .orElseThrow(() -> new IllegalArgumentException("Source account does not exist"));

        Account destinationAccount = accountRepositoryPort.findByAccountNumber(destinationNumber)
                .orElseThrow(() -> new IllegalArgumentException("Destination account does not exist"));

        Money transferMoney = new Money(amount, SupportedCurrency.valueOf(currency));
        sourceAccount.debit(transferMoney);
        destinationAccount.credit(transferMoney);

        accountRepositoryPort.save(sourceAccount);
        accountRepositoryPort.save(destinationAccount);
    }
}
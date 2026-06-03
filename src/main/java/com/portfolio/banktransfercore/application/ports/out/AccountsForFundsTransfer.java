package com.portfolio.banktransfercore.application.ports.out;

import com.portfolio.banktransfercore.domain.account.Account;

public record AccountsForFundsTransfer(Account source, Account destination) {}

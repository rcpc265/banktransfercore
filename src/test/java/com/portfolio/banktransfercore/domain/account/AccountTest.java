package com.portfolio.banktransfercore.domain;

import com.portfolio.banktransfercore.domain.account.Account;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private final UUID validId = UUID.randomUUID();
    private final String validAccountNumber = "CTA-123";

    @Test
    void shouldCreateAccountSuccessfully() {
        Account account = new Account(validId, validAccountNumber, new BigDecimal("100.00"));

        assertNotNull(account);
        assertEquals(0, account.getBalance().compareTo(new BigDecimal("100.00")));
    }

    @Test
    void shouldThrowExceptionWhenCreatingAccountWithNullId() {
        assertThrows(NullPointerException.class, () ->
                new Account(null, validAccountNumber, BigDecimal.TEN));
    }

    @Test
    void shouldThrowExceptionWhenCreatingAccountWithInvalidNumber() {
        assertThrows(IllegalArgumentException.class, () ->
                new Account(validId, "INVALID-NUMBER", BigDecimal.TEN));
    }

    @Test
    void shouldThrowExceptionWhenCreatingAccountWithNegativeBalance() {
        assertThrows(IllegalArgumentException.class, () ->
                new Account(validId, validAccountNumber, new BigDecimal("-1.00")));
    }

    @Test
    void shouldDecreaseBalanceWhenDebiting() {
        Account account = new Account(validId, validAccountNumber, new BigDecimal("1000.00"));

        account.debit(new BigDecimal("200.00"));

        assertEquals(0, account.getBalance().compareTo(new BigDecimal("800.00")));
    }

    @Test
    void shouldDecreaseBalanceToZeroWhenDebitingFullAmount() {
        Account account = new Account(validId, validAccountNumber, new BigDecimal("100.00"));

        account.debit(new BigDecimal("100.00"));

        assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    void shouldThrowExceptionWhenDebitingInsufficientFunds() {
        Account account = new Account(validId, validAccountNumber, new BigDecimal("100.00"));

        assertThrows(IllegalStateException.class, () -> account.debit(new BigDecimal("200.00")));
    }

    @Test
    void shouldThrowExceptionWhenDebitingNegativeOrZero() {
        Account account = new Account(validId, validAccountNumber, new BigDecimal("100.00"));

        assertThrows(IllegalArgumentException.class, () -> account.debit(new BigDecimal("-50.00")));
        assertThrows(IllegalArgumentException.class, () -> account.debit(BigDecimal.ZERO));
    }

    @Test
    void shouldIncreaseBalanceWhenCrediting() {
        Account account = new Account(validId, validAccountNumber, new BigDecimal("100.00"));

        account.credit(new BigDecimal("50.00"));

        assertEquals(0, account.getBalance().compareTo(new BigDecimal("150.00")));
    }

    @Test
    void shouldThrowExceptionWhenCreditingNegativeOrZero() {
        Account account = new Account(validId, validAccountNumber, new BigDecimal("100.00"));

        assertThrows(IllegalArgumentException.class, () -> account.credit(new BigDecimal("-50.00")));
        assertThrows(IllegalArgumentException.class, () -> account.credit(BigDecimal.ZERO));
    }
}
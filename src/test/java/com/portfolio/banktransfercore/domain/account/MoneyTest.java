package com.portfolio.banktransfercore.domain.account;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class BalanceTest {
    private static final BigDecimal VALID_AMOUNT = new BigDecimal("100.00");

    @Test
    void shouldCreateBalanceWhenAmountIsValid() {
        assertEquals(VALID_AMOUNT, new Balance(VALID_AMOUNT).amount());
    }

    @Test
    void shouldAllowWhenAmountIsZero() {
        assertEquals(0, new Balance(BigDecimal.ZERO).amount().compareTo(BigDecimal.ZERO));
    }

    @Test
    void shouldFailWhenAmountIsNull() {
        var exception = assertThrows(NullPointerException.class, () -> new Balance(null));
        assertEquals("Balance amount cannot be null", exception.getMessage());
    }

    @Test
    void shouldRejectWhenAmountIsNegative() {
        var exception = assertThrows(IllegalArgumentException.class, () -> new Balance(new BigDecimal("-100.00")));
        assertEquals("Balance amount cannot be negative", exception.getMessage());
    }
}
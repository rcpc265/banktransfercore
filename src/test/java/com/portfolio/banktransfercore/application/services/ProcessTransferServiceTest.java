package com.portfolio.banktransfercore.application.services;

import com.portfolio.banktransfercore.application.ports.out.AccountRepositoryPort;
import com.portfolio.banktransfercore.domain.account.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ProcessTransferServiceTest {

    @Mock
    private AccountRepositoryPort accountRepositoryPort;

    @InjectMocks
    private ProcessTransferService transferService;

    private static final String ANY_SOURCE_NUMBER = "00219112345678901206";
    private static final String ANY_DESTINATION_NUMBER = "98765432101234567819";

    @Test
    @DisplayName("Executes a successful transfer between two existing accounts")
    void givenValidAccountsAndFunds_whenExecutingTransfer_thenBalancesAreUpdatedAndSaved() {
        // Given
        UUID anySourceId = UUID.randomUUID();
        UUID anyDestinationId = UUID.randomUUID();
        BigDecimal initialSourceBalance = new BigDecimal("500.00");
        BigDecimal initialDestinationBalance = new BigDecimal("100.00");
        BigDecimal transferAmount = new BigDecimal("200.00");

        Account sourceAccount = new Account(anySourceId, ANY_SOURCE_NUMBER, initialSourceBalance);
        Account destinationAccount = new Account(anyDestinationId, ANY_DESTINATION_NUMBER, initialDestinationBalance);

        given(accountRepositoryPort.findByAccountNumber(ANY_SOURCE_NUMBER))
                .willReturn(Optional.of(sourceAccount));
        given(accountRepositoryPort.findByAccountNumber(ANY_DESTINATION_NUMBER))
                .willReturn(Optional.of(destinationAccount));

        // When
        transferService.execute(ANY_SOURCE_NUMBER, ANY_DESTINATION_NUMBER, transferAmount);

        // Then
        BigDecimal expectedSourceBalance = new BigDecimal("300.00");
        BigDecimal expectedDestinationBalance = new BigDecimal("300.00");

        assertThat(sourceAccount.getBalance()).isEqualByComparingTo(expectedSourceBalance);
        assertThat(destinationAccount.getBalance()).isEqualByComparingTo(expectedDestinationBalance);

        then(accountRepositoryPort).should().save(sourceAccount);
        then(accountRepositoryPort).should().save(destinationAccount);
    }

    @Test
    @DisplayName("Throws exception when source account does not exist")
    void givenMissingSourceAccount_whenExecutingTransfer_thenThrowsException() {
        // Given
        BigDecimal anyAmount = new BigDecimal("100.00");

        given(accountRepositoryPort.findByAccountNumber(ANY_SOURCE_NUMBER))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> transferService.execute(ANY_SOURCE_NUMBER, ANY_DESTINATION_NUMBER, anyAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Source account does not exist");

        then(accountRepositoryPort).should(never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Throws exception when destination account does not exist")
    void givenMissingDestinationAccount_whenExecutingTransfer_thenThrowsException() {
        // Given
        UUID anySourceId = UUID.randomUUID();
        BigDecimal anyAmount = new BigDecimal("100.00");
        Account sourceAccount = new Account(anySourceId, ANY_SOURCE_NUMBER, new BigDecimal("500.00"));

        given(accountRepositoryPort.findByAccountNumber(ANY_SOURCE_NUMBER))
                .willReturn(Optional.of(sourceAccount));
        given(accountRepositoryPort.findByAccountNumber(ANY_DESTINATION_NUMBER))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> transferService.execute(ANY_SOURCE_NUMBER, ANY_DESTINATION_NUMBER, anyAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Destination account does not exist");

        then(accountRepositoryPort).should(never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Throws exception when source account has insufficient funds")
    void givenSourceAccountWithInsufficientFunds_whenExecutingTransfer_thenThrowsExceptionAndSavesNothing() {
        // Given
        UUID anySourceId = UUID.randomUUID();
        UUID anyDestinationId = UUID.randomUUID();
        BigDecimal lowSourceBalance = new BigDecimal("50.00");
        BigDecimal excessiveTransferAmount = new BigDecimal("100.00");

        Account sourceAccount = new Account(anySourceId, ANY_SOURCE_NUMBER, lowSourceBalance);
        Account destinationAccount = new Account(anyDestinationId, ANY_DESTINATION_NUMBER, new BigDecimal("200.00"));

        given(accountRepositoryPort.findByAccountNumber(ANY_SOURCE_NUMBER))
                .willReturn(Optional.of(sourceAccount));
        given(accountRepositoryPort.findByAccountNumber(ANY_DESTINATION_NUMBER))
                .willReturn(Optional.of(destinationAccount));

        // When & Then
        assertThatThrownBy(() -> transferService.execute(ANY_SOURCE_NUMBER, ANY_DESTINATION_NUMBER, excessiveTransferAmount))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Insufficient funds");

        then(accountRepositoryPort).should(never()).save(any(Account.class));
    }
}
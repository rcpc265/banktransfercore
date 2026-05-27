package com.portfolio.banktransfercore.domain.transfer;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransferTest {

  @Test
  @DisplayName("Does not allow creating a Transfer with an amount equal to zero")
  void givenZeroAmount_whenCreatingTransfer_thenThrowsException() {
    // Given
    UUID anyId = UUID.randomUUID();
    String anySourceAccount = "00219112345678901206";
    String anyDestinationAccount = "98765432101234567819";
    BigDecimal zeroAmount = new BigDecimal("0.00");
    LocalDateTime anyDate = LocalDateTime.now();

    // When & Then
    assertThatThrownBy(
            () -> new Transfer(anyId, anySourceAccount, anyDestinationAccount, zeroAmount, anyDate))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Transfer amount must be greater than zero");
  }

  @Test
  @DisplayName("Does not allow creating a Transfer with a negative amount")
  void givenNegativeAmount_whenCreatingTransfer_thenThrowsException() {
    // Given
    UUID anyId = UUID.randomUUID();
    String anySourceAccount = "00219112345678901206";
    String anyDestinationAccount = "98765432101234567819";
    BigDecimal negativeAmount = new BigDecimal("-100.00");
    LocalDateTime anyDate = LocalDateTime.now();

    // When & Then
    assertThatThrownBy(
            () ->
                new Transfer(
                    anyId, anySourceAccount, anyDestinationAccount, negativeAmount, anyDate))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Transfer amount must be greater than zero");
  }

  @Test
  @DisplayName("Does not allow creating a Transfer when source and destination are the same")
  void givenSameSourceAndDestination_whenCreatingTransfer_thenThrowsException() {
    // Given
    UUID anyId = UUID.randomUUID();
    String sameAccount = "00219112345678901206";
    BigDecimal validAmount = new BigDecimal("100.00");
    LocalDateTime anyDate = LocalDateTime.now();

    // When & Then
    assertThatThrownBy(() -> new Transfer(anyId, sameAccount, sameAccount, validAmount, anyDate))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Source and destination accounts cannot be the same");
  }

  @Test
  @DisplayName("Creates a valid Transfer when all arguments are correct")
  void givenValidArguments_whenCreatingTransfer_thenSuccessful() {
    // Given
    UUID expectedId = UUID.randomUUID();
    String expectedSourceAccount = "00219112345678901206";
    String expectedDestinationAccount = "98765432101234567819";
    BigDecimal expectedAmount = new BigDecimal("150.50");
    LocalDateTime expectedDate = LocalDateTime.now();

    // When
    Transfer transfer =
        new Transfer(
            expectedId,
            expectedSourceAccount,
            expectedDestinationAccount,
            expectedAmount,
            expectedDate);

    // Then - Verificación fluida y emparejada
    assertThat(transfer)
        .returns(expectedId, Transfer::getId)
        .returns(expectedSourceAccount, Transfer::getSourceAccountNumber)
        .returns(expectedDestinationAccount, Transfer::getDestinationAccountNumber)
        .returns(expectedAmount, Transfer::getAmount)
        .returns(expectedDate, Transfer::getCreatedAt);
  }
}

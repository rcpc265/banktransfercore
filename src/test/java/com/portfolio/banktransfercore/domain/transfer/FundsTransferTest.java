package com.portfolio.banktransfercore.domain.transfer;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FundsTransferTest {

  @Test
  @DisplayName("Does not allow creating a Transfer with an amount equal to zero")
  void givenZeroAmount_whenCreatingTransfer_thenThrowsException() {
    // Given
    var anyId = UUID.randomUUID();
    var anySourceAccount = "00219112345678901206";
    var anyDestinationAccount = "98765432101234567819";
    var zeroAmount = new BigDecimal("0.00");
    var anyDate = LocalDateTime.now();

    // When & Then
    assertThatThrownBy(
            () ->
                new FundsTransfer(
                    anyId, anySourceAccount, anyDestinationAccount, zeroAmount, anyDate))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Transfer amount must be greater than zero");
  }

  @Test
  @DisplayName("Does not allow creating a Transfer with a negative amount")
  void givenNegativeAmount_whenCreatingTransfer_thenThrowsException() {
    // Given
    var anyId = UUID.randomUUID();
    var anySourceAccount = "00219112345678901206";
    var anyDestinationAccount = "98765432101234567819";
    var negativeAmount = new BigDecimal("-100.00");
    var anyDate = LocalDateTime.now();

    // When & Then
    assertThatThrownBy(
            () ->
                new FundsTransfer(
                    anyId, anySourceAccount, anyDestinationAccount, negativeAmount, anyDate))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Transfer amount must be greater than zero");
  }

  @Test
  @DisplayName("Does not allow creating a Transfer when source and destination are the same")
  void givenSameSourceAndDestination_whenCreatingTransfer_thenThrowsException() {
    // Given
    var anyId = UUID.randomUUID();
    var sameAccount = "00219112345678901206";
    var validAmount = new BigDecimal("100.00");
    var anyDate = LocalDateTime.now();

    // When & Then
    assertThatThrownBy(
            () -> new FundsTransfer(anyId, sameAccount, sameAccount, validAmount, anyDate))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Source and destination accounts cannot be the same");
  }

  @Test
  @DisplayName("Creates a valid Transfer when all arguments are correct")
  void givenValidArguments_whenCreatingTransfer_thenSuccessful() {
    // Given
    var expectedId = UUID.randomUUID();
    var expectedSourceAccount = "00219112345678901206";
    var expectedDestinationAccount = "98765432101234567819";
    var expectedAmount = new BigDecimal("150.50");
    var expectedDate = LocalDateTime.now();

    // When
    var transfer =
        new FundsTransfer(
            expectedId,
            expectedSourceAccount,
            expectedDestinationAccount,
            expectedAmount,
            expectedDate);

    // Then - Verificación fluida y emparejada
    assertThat(transfer)
        .returns(expectedId, FundsTransfer::getId)
        .returns(expectedSourceAccount, FundsTransfer::getSourceAccountNumber)
        .returns(expectedDestinationAccount, FundsTransfer::getDestinationAccountNumber)
        .returns(expectedAmount, FundsTransfer::getAmount)
        .returns(expectedDate, FundsTransfer::getCreatedAt);
  }
}

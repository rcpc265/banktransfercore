package com.portfolio.banktransfercore.infrastructure.transactional;

import static org.mockito.BDDMockito.then;

import com.portfolio.banktransfercore.application.ports.in.FundsTransferUseCase;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionalFundsTransferDecoratorTest {

  private static final String ANY_SOURCE = "00219112345678901206";
  private static final String ANY_DESTINATION = "98765432101234567819";
  private static final BigDecimal ANY_AMOUNT = new BigDecimal("100.00");
  private static final String ANY_CURRENCY = "USD";

  @Mock private FundsTransferUseCase delegate;

  private TransactionalFundsTransferDecorator decorator;

  @BeforeEach
  void setUp() {
    decorator = new TransactionalFundsTransferDecorator(delegate);
  }

  @Test
  @DisplayName("Delegates execute call to wrapped service")
  void givenDecorator_whenExecute_thenDelegatesToService() {
    // Given
    // When
    decorator.execute(ANY_SOURCE, ANY_DESTINATION, ANY_AMOUNT, ANY_CURRENCY);

    // Then
    then(delegate).should().execute(ANY_SOURCE, ANY_DESTINATION, ANY_AMOUNT, ANY_CURRENCY);
  }
}

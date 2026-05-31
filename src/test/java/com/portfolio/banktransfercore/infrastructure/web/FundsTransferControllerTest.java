package com.portfolio.banktransfercore.infrastructure.web;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.banktransfercore.application.ports.in.FundsTransferUseCase;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FundsTransferController.class)
class FundsTransferControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private FundsTransferUseCase transferUseCase;

  @Test
  @DisplayName("When valid transfer request is POSTed, then returns 200 OK and executes use case")
  void givenValidRequest_whenPostingTransfer_thenReturns200Ok() throws Exception {
    // Given
    var sourceNumber = "00219112345678901206";
    var destinationNumber = "98765432101234567819";
    var amount = new BigDecimal("150.00");
    var currency = "USD";
    var request = new FundsTransferRequest(sourceNumber, destinationNumber, amount, currency);

    // When
    mockMvc
        .perform(
            post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    java.util.Objects.requireNonNull(objectMapper.writeValueAsString(request))))
        // Then
        .andExpect(status().isOk());

    then(transferUseCase).should().execute(sourceNumber, destinationNumber, amount, currency);
  }
}

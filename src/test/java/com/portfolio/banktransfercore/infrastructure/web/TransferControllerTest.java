package com.portfolio.banktransfercore.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portfolio.banktransfercore.application.ports.in.TransferUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransferUseCase transferUseCase;

    @Test
    @DisplayName("When valid transfer request is POSTed, then returns 200 OK and executes use case")
    void givenValidRequest_whenPostingTransfer_thenReturns200Ok() throws Exception {
        // Given: Una solicitud de transferencia válida con monto y moneda
        var sourceNumber = "00219112345678901206";
        var destinationNumber = "98765432101234567819";
        var amount = new BigDecimal("150.00");
        var currency = "USD";
        var request = new TransferRequest(sourceNumber, destinationNumber, amount, currency);

        // When: Se recibe el POST en el endpoint de transferencias
        mockMvc.perform(post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Then: El controlador delega al puerto de entrada y responde HTTP 200
                .andExpect(status().isOk());

        then(transferUseCase).should().execute(sourceNumber, destinationNumber, amount, currency);
    }
}
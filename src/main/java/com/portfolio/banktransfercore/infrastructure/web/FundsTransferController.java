package com.portfolio.banktransfercore.infrastructure.web;

import com.portfolio.banktransfercore.application.ports.in.FundsTransferUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
public class FundsTransferController {
  private final FundsTransferUseCase transferUseCase;

  public FundsTransferController(FundsTransferUseCase transferUseCase) {
    this.transferUseCase = transferUseCase;
  }

  @PostMapping
  public ResponseEntity<Void> transfer(@RequestBody FundsTransferRequest request) {
    transferUseCase.execute(
        request.sourceNumber(), request.destinationNumber(), request.amount(), request.currency());

    return ResponseEntity.ok().build();
  }
}

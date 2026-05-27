package com.portfolio.banktransfercore.infrastructure.web;

import com.portfolio.banktransfercore.application.ports.in.TransferUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    private final TransferUseCase transferUseCase;

    public TransferController(TransferUseCase transferUseCase) {
        this.transferUseCase = transferUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest request) {
        transferUseCase.execute(
                request.sourceNumber(),
                request.destinationNumber(),
                request.amount(),
                request.currency()
        );

        return ResponseEntity.ok().build();
    }
}
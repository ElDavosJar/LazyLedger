package com.lazyledger.ledger.api;

import com.lazyledger.ledger.api.dto.ApiResponse;
import com.lazyledger.ledger.api.dto.CreateLedgerRequest;
import com.lazyledger.ledger.api.dto.LedgerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ledgers")
public class LedgerController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LedgerController.class);
    private final LedgerServiceFacade ledgerServiceFacade;

    public LedgerController(LedgerServiceFacade ledgerServiceFacade) {
        this.ledgerServiceFacade = ledgerServiceFacade;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<LedgerDto>> createLedger(@RequestBody CreateLedgerRequest request) {
        UUID userId = UUID.fromString(request.userId());
        LedgerDto responseDto = ledgerServiceFacade.createLedger(request.name(), userId, request.groupId(), request.groupName());
        return ResponseEntity.ok(ApiResponse.success("Ledger created successfully", responseDto));
    }
}
package com.lazyledger.ledger.api;

import com.lazyledger.ledger.api.dto.ApiResponse;
import com.lazyledger.ledger.api.dto.CreateLedgerRequest;
import com.lazyledger.ledger.api.dto.LedgerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ledgers")
@Tag(name = "Ledger Management", description = "APIs for managing financial ledgers")
public class LedgerController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LedgerController.class);
    private final LedgerServiceFacade ledgerServiceFacade;

    public LedgerController(LedgerServiceFacade ledgerServiceFacade) {
        this.ledgerServiceFacade = ledgerServiceFacade;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new ledger", description = "Creates a new financial ledger for a user")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Ledger created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<LedgerDto>> createLedger(@RequestBody CreateLedgerRequest request) {
        UUID userId = UUID.fromString(request.userId());
        LedgerDto responseDto = ledgerServiceFacade.createLedger(request.name(), userId, request.groupId(), request.groupName());
        return ResponseEntity.ok(ApiResponse.success("Ledger created successfully", responseDto));
    }
}
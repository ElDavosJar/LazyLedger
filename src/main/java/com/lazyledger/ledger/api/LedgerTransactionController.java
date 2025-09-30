package com.lazyledger.ledger.api;

import com.lazyledger.ledger.api.dto.ApiResponse;
import com.lazyledger.ledger.api.dto.CreateTransactionRequest;
import com.lazyledger.ledger.api.dto.TransactionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ledgers/{ledgerId}/transactions")
@Tag(name = "Transaction Management", description = "APIs for managing ledger transactions")
public class LedgerTransactionController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LedgerTransactionController.class);
    private final LedgerTransactionServiceFacade transactionServiceFacade;

    public LedgerTransactionController(LedgerTransactionServiceFacade transactionServiceFacade) {
        this.transactionServiceFacade = transactionServiceFacade;
    }

    @PostMapping
    @Operation(summary = "Add a transaction to a ledger", description = "Creates a new transaction for the specified ledger")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction added successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ledger not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<TransactionDto>> addTransaction(
            @Parameter(description = "UUID of the ledger", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String ledgerId,
            @RequestBody CreateTransactionRequest request) {
        log.info("Adding transaction to ledger: {}", ledgerId);
        // Override the ledgerId from path parameter to ensure consistency
        CreateTransactionRequest updatedRequest = new CreateTransactionRequest(
            ledgerId,
            request.amount(),
            request.currency(),
            request.description(),
            request.category(),
            request.transactionDate()
        );
        TransactionDto responseDto = transactionServiceFacade.addTransaction(updatedRequest);
        return ResponseEntity.ok(ApiResponse.success("Transaction added successfully", responseDto));
    }

    @GetMapping("/{transactionId}")
    @Operation(summary = "Get a specific transaction", description = "Retrieves a transaction by its ID")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transaction not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<TransactionDto>> getTransaction(
            @Parameter(description = "UUID of the ledger", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String ledgerId,
            @Parameter(description = "UUID of the transaction", example = "123e4567-e89b-12d3-a456-426614174001")
            @PathVariable String transactionId) {
        log.info("Getting transaction: {} from ledger: {}", transactionId, ledgerId);
        TransactionDto responseDto = transactionServiceFacade.getTransactionById(transactionId);
        return ResponseEntity.ok(ApiResponse.success("Transaction retrieved successfully", responseDto));
    }

    @GetMapping
    @Operation(summary = "Get all transactions for a ledger", description = "Retrieves all transactions associated with a specific ledger")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid ledger ID",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ledger not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactionsByLedger(
            @Parameter(description = "UUID of the ledger", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String ledgerId) {
        log.info("Getting transactions for ledger: {}", ledgerId);
        List<TransactionDto> transactions = transactionServiceFacade.getTransactionsByLedgerId(ledgerId);
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactions));
    }

    @GetMapping("/range")
    @Operation(summary = "Get transactions by date range", description = "Retrieves transactions for a ledger within a specific date range")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input parameters",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Ledger not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactionsByLedgerAndDateRange(
            @Parameter(description = "UUID of the ledger", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String ledgerId,
            @Parameter(description = "Start date in ISO format", example = "2023-01-01")
            @RequestParam String startDate,
            @Parameter(description = "End date in ISO format", example = "2023-12-31")
            @RequestParam String endDate) {
        log.info("Getting transactions for ledger: {} between {} and {}", ledgerId, startDate, endDate);
        List<TransactionDto> transactions = transactionServiceFacade.getTransactionsByLedgerIdAndDateRange(ledgerId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactions));
    }

    @DeleteMapping("/{transactionId}")
    @Operation(summary = "Delete a transaction", description = "Deletes a specific transaction from a ledger")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction deleted successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transaction not found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(
            @Parameter(description = "UUID of the ledger", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String ledgerId,
            @Parameter(description = "UUID of the transaction to delete", example = "123e4567-e89b-12d3-a456-426614174001")
            @PathVariable String transactionId) {
        log.info("Deleting transaction: {} from ledger: {}", transactionId, ledgerId);
        transactionServiceFacade.deleteTransaction(transactionId);
        return ResponseEntity.ok(ApiResponse.success("Transaction deleted successfully", null));
    }
}
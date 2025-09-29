package com.lazyledger.ledger.api;

import com.lazyledger.ledger.api.dto.ApiResponse;
import com.lazyledger.ledger.api.dto.CreateTransactionRequest;
import com.lazyledger.ledger.api.dto.TransactionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ledgers/{ledgerId}/transactions")
public class LedgerTransactionController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LedgerTransactionController.class);
    private final LedgerTransactionServiceFacade transactionServiceFacade;

    public LedgerTransactionController(LedgerTransactionServiceFacade transactionServiceFacade) {
        this.transactionServiceFacade = transactionServiceFacade;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionDto>> addTransaction(@PathVariable String ledgerId, @RequestBody CreateTransactionRequest request) {
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
    public ResponseEntity<ApiResponse<TransactionDto>> getTransaction(@PathVariable String ledgerId, @PathVariable String transactionId) {
        log.info("Getting transaction: {} from ledger: {}", transactionId, ledgerId);
        TransactionDto responseDto = transactionServiceFacade.getTransactionById(transactionId);
        return ResponseEntity.ok(ApiResponse.success("Transaction retrieved successfully", responseDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactionsByLedger(@PathVariable String ledgerId) {
        log.info("Getting transactions for ledger: {}", ledgerId);
        List<TransactionDto> transactions = transactionServiceFacade.getTransactionsByLedgerId(ledgerId);
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactions));
    }

    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getTransactionsByLedgerAndDateRange(
            @PathVariable String ledgerId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        log.info("Getting transactions for ledger: {} between {} and {}", ledgerId, startDate, endDate);
        List<TransactionDto> transactions = transactionServiceFacade.getTransactionsByLedgerIdAndDateRange(ledgerId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactions));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<ApiResponse<Void>> deleteTransaction(@PathVariable String ledgerId, @PathVariable String transactionId) {
        log.info("Deleting transaction: {} from ledger: {}", transactionId, ledgerId);
        transactionServiceFacade.deleteTransaction(transactionId);
        return ResponseEntity.ok(ApiResponse.success("Transaction deleted successfully", null));
    }
}
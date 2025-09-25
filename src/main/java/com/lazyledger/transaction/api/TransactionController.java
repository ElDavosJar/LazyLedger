package com.lazyledger.transaction.api;

import com.lazyledger.transcription.extractor.TransactionDataDto;
import com.lazyledger.transaction.api.dto.ApiResponse;
import com.lazyledger.transaction.api.dto.CreateTransactionRequest;
import com.lazyledger.transaction.api.dto.TransactionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TransactionController.class);
    private final ApiTransactionServiceFacade apiTransactionServiceFacade;

    public TransactionController(ApiTransactionServiceFacade apiTransactionServiceFacade) {
        this.apiTransactionServiceFacade = apiTransactionServiceFacade;
    }



    @PostMapping(value = "/test-file", consumes = "*/*")
    public ResponseEntity<ApiResponse<TransactionDto>> testFile(@RequestBody byte[] fileBytes, @RequestHeader(value = "Content-Type", required = false) String contentType) {
        System.out.println("Received binary file, contentType: " + contentType + ", size: " + fileBytes.length);
        String mediaType = contentType != null && contentType.startsWith("text/") ? "text" : "audio";
        TransactionDto responseDto = apiTransactionServiceFacade.processFile(fileBytes, mediaType);
        if (responseDto != null) {
            return ResponseEntity.ok(ApiResponse.success("Transcription and transaction saved successfully", responseDto));
        } else {
            return ResponseEntity.ok(ApiResponse.error("No valid transaction data extracted."));
        }
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<TransactionDto>> createTransaction(@RequestBody CreateTransactionRequest request) {
        TransactionDataDto dataDto = TransactionMapper.toTransactionDataDto(request);
        TransactionDto responseDto = apiTransactionServiceFacade.save(dataDto);
        return ResponseEntity.ok(ApiResponse.success("Transaction created successfully", responseDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionDto>>> getAllTransactions() {
        List<TransactionDto> dtos = apiTransactionServiceFacade.findAll();
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", dtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDto>> getTransaction(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        TransactionDto dto = apiTransactionServiceFacade.findById(uuid);
        return ResponseEntity.ok(ApiResponse.success("Transaction retrieved successfully", dto));
    }
    

}

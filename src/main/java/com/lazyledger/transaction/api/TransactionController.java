package com.lazyledger.transaction.api;

import com.lazyledger.commons.TransactionId;
import com.lazyledger.commons.UserId;
import com.lazyledger.commons.enums.Category;
import com.lazyledger.trancriptionModule.AudioTranscriber;
import com.lazyledger.trancriptionModule.TransactionDataDto;
import com.lazyledger.transaction.api.dto.ApiResponse;
import com.lazyledger.transaction.api.dto.CreateTransactionRequest;
import com.lazyledger.transaction.api.dto.TransactionDto;
import com.lazyledger.transaction.domain.*;
import com.lazyledger.transaction.domain.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.reactive.function.client.WebClient;
import com.lazyledger.trancriptionModule.DataExtractor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.xml.crypto.Data;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TransactionController.class);

    private final TransactionRepository transactionRepository;
    private final WebClient webClient;

    @Value("${google.ai.api.key}")
    private String geminiApiKey;

    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.webClient = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com")
            .build();
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse<Void>> handleMultipartException(MultipartException e) {
        log.error("MultipartException occurred", e);
        return ResponseEntity.badRequest()
            .body(ApiResponse.error("Request must be multipart/form-data for file uploads"));
    }


    @PostMapping(value = "/test-file", consumes = "*/*")
    public ResponseEntity<ApiResponse<TransactionDto>> testFile(@RequestBody byte[] fileBytes, @RequestHeader(value = "Content-Type", required = false) String contentType) {
        System.out.println("Received binary file, contentType: " + contentType + ", size: " + fileBytes.length);
        String transcription = AudioTranscriber.transcribeAudioToText(fileBytes, contentType, geminiApiKey);
        TransactionDataDto extractedData = DataExtractor.extractTransactionData(transcription, geminiApiKey).stream().findFirst().orElse(null);
        if (extractedData != null) {
            CreateTransactionRequest createRequest = new CreateTransactionRequest(
                UUID.randomUUID().toString(), // userId
                extractedData.amount(), // amount as positive
                extractedData.currency(), // currency
                extractedData.description(), // description
                extractedData.category(), // category
                extractedData.transactionDate() // transactionDate
            );
            Transaction transaction = Transaction.of(
                TransactionId.of(UUID.randomUUID()),
                UserId.of(UUID.fromString(createRequest.userId())),
                Amount.of(createRequest.amount(), createRequest.currency()),
                createRequest.description() != null ? new Description(createRequest.description()) : null,
                Category.fromString(createRequest.category()),
                createRequest.transactionDate() != null ? new TransactionDate(LocalDate.parse(createRequest.transactionDate())) : new TransactionDate(LocalDate.now())
            );
            Transaction saved = transactionRepository.save(transaction);
            TransactionDto dto = toDto(saved);
            return ResponseEntity.ok(ApiResponse.success("Transcription and transaction saved successfully", dto));
        } else {
            return ResponseEntity.ok(ApiResponse.error("Transcription: " + transcription + "\nNo valid transaction data extracted."));
        }
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<TransactionDto>> createTransaction(@RequestBody CreateTransactionRequest request) {
        UUID userId = UUID.fromString(request.userId());
        BigDecimal amountValue = request.amount();
        String currency = request.currency();
        Amount amount = Amount.of(amountValue, currency);
        Description description = request.description() != null ? new Description(request.description()) : null;
        Category category = Category.fromString(request.category());
        LocalDate transactionDate;
        TransactionDate date;
        if(request.transactionDate()==null){
            transactionDate = LocalDate.now();
            date = new TransactionDate(transactionDate);
        }else{
            transactionDate = LocalDate.parse(request.transactionDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            date = new TransactionDate(transactionDate);
        }
        TransactionId id = TransactionId.of(UUID.randomUUID());
        Transaction transaction = Transaction.of(id, UserId.of(userId), amount, description, category, date);
        Transaction saved = transactionRepository.save(transaction);

        TransactionDto dto = toDto(saved);
        return ResponseEntity.ok(ApiResponse.success("Transaction created successfully", dto));
    }
   
    // Fallback regex parsing
    


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionDto>> getTransaction(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        Transaction transaction = transactionRepository.findById(uuid);
        if (transaction == null) {
            return ResponseEntity.status(404).body(ApiResponse.error("Transaction not found"));
        }
        TransactionDto dto = toDto(transaction);
        return ResponseEntity.ok(ApiResponse.success("Transaction retrieved successfully", dto));
    }

    private TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(
            transaction.getId().value().toString(),
            transaction.getUserId().value().toString(),
            transaction.getAmount().value(),
            transaction.getAmount().currency(),
            transaction.getDescription() != null ? transaction.getDescription().text() : null,
            transaction.getCategory().toString(),
            transaction.getTransactionDate().value().toString(),
            transaction.getCreatedAt().toString()
        );
    }


}

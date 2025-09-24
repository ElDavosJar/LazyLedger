package com.lazyledger.transaction.infrastructure.externalApps.adapters.telegram;

import com.lazyledger.trancriptionModule.TransactionDataDto;
import com.lazyledger.trancriptionModule.TranscriptionService;
import com.lazyledger.transaction.application.TransactionService;
import com.lazyledger.transaction.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageProcessorService {

    @Autowired
    private TranscriptionService transcriptionService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${telegram.bot.token}")
    private String botToken;

    public void processMessage(MessageDto dto) {
        List<TransactionDataDto> transactions;
        if ("text".equals(dto.getMediaType())) {
            transactions = transcriptionService.transcribeAndExtractTransactions(dto.getContent().getBytes(), "text");
        } else if (dto.getBinaryData() != null) {
            transactions = transcriptionService.transcribeAndExtractTransactions(dto.getBinaryData(), dto.getMediaType());
        } else {
            return; // No data to process
        }

        for (TransactionDataDto transaction : transactions) {
            Transaction saved = transactionService.save(transaction);
            sendFeedback(dto.getChatId(), saved, transaction);
        }
    }

    private void sendFeedback(String chatId, Transaction transaction, TransactionDataDto dto) {
        String type = transaction.getAmount().value().doubleValue() >= 0 ? "ingreso" : "gasto";
        String amountStr = transaction.getAmount().value().toString() + " " + transaction.getAmount().currency();
        String dateStr = transaction.getTransactionDate().value().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String transactionCode = "001"; // Default as requested

        String message = String.format(
            "Se ha guardado. Ha registrado un %s de %s en fecha %s. Código de transacción: %s",
            type, amountStr, dateStr, transactionCode
        );

        Map<String, Object> request = new HashMap<>();
        request.put("chat_id", chatId);
        request.put("text", message);

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        restTemplate.postForObject(url, request, String.class);
    }
}
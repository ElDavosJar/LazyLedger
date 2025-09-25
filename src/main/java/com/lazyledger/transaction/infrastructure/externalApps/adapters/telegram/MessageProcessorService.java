package com.lazyledger.transaction.infrastructure.externalApps.adapters.telegram;

import com.lazyledger.transaction.api.dto.TransactionDto;
import com.lazyledger.transaction.infrastructure.externalApps.ExternalApiServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageProcessorService {
    
    @Autowired
    private ExternalApiServiceFacade externalApiServiceFacade;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${telegram.bot.token}")
    private String botToken;

    public void processMessage(MessageDto dto) {
        List<TransactionDto> savedTransactions;
        if ("text".equals(dto.getMediaType())) {
            savedTransactions = externalApiServiceFacade.processFile(dto.getContent().getBytes(), "text");
        } else if (dto.getBinaryData() != null) {
            savedTransactions = externalApiServiceFacade.processFile(dto.getBinaryData(), dto.getMediaType());
        } else {
            return; // No data to process
        }

        for (TransactionDto transaction : savedTransactions) {
            sendFeedback(dto.getChatId(), transaction);
        }
    }

    private void sendFeedback(String chatId, TransactionDto transaction) {
        String type = transaction.amount().doubleValue() >= 0 ? "ingreso" : "gasto";
        String amountStr = transaction.amount() + " " + transaction.currency();
        String dateStr = transaction.transactionDate();
        String transactionCode = transaction.transactionNumber().toString();

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
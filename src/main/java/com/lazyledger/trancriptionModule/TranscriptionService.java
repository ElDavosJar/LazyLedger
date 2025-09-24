package com.lazyledger.trancriptionModule;

import com.lazyledger.commons.TransactionId;
import com.lazyledger.commons.UserId;
import com.lazyledger.commons.enums.Category;
import com.lazyledger.transaction.domain.*;
import com.lazyledger.transaction.domain.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
public class TranscriptionService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TranscriptionService.class);

    private final WebClient webClient;
    private final TransactionRepository transactionRepository;

    @Value("${google.ai.api.key}")
    private String geminiApiKey;

    public TranscriptionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.webClient = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com")
            .build();
    }

    public String transcribeAudioToText(byte[] audioBytes, String contentType) {
        try {
            String base64Audio = Base64.getEncoder().encodeToString(audioBytes);

            String prompt = "Transcribe this audio file to text. Return only the transcription text, nothing else.";

            Map<String, Object> requestBody = Map.of(
                "contents", java.util.List.of(Map.of(
                    "parts", java.util.List.of(
                        Map.of("text", prompt),
                        Map.of("inline_data", Map.of(
                            "mime_type", contentType,
                            "data", base64Audio
                        ))
                    )
                )),
                "generationConfig", Map.of(
                    "temperature", 0.1,
                    "maxOutputTokens", 2048
                )
            );

            Map response = webClient.post()
                .uri("/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

            if (response != null && response.containsKey("candidates")) {
                java.util.List candidates = (java.util.List) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map candidate = (Map) candidates.get(0);
                    Map content = (Map) candidate.get("content");
                    java.util.List parts = (java.util.List) content.get("parts");
                    if (!parts.isEmpty()) {
                        Map part = (Map) parts.get(0);
                        return (String) part.get("text");
                    }
                }
            }

            return "Error transcribing audio";

        } catch (Exception e) {
            return "Error transcribing audio: " + e.getMessage();
        }
    }

    public String extractTransactionsFromText(String transcriptionText) {
        try {
            String prompt = String.format("""
                    Eres un asistente experto en finanzas personales llamado "LazyLedger". Tu tarea es analizar rigurosamente el siguiente texto y extraer TODAS las transacciones, aplicando un signo negativo para los gastos y positivo para los ingresos.

                    Debes devolver el resultado como un array de objetos JSON. Cada objeto debe tener EXACTAMENTE los siguientes campos: "amount" (Number), "currency" (String), "description" (String), "category" (String), y "transactionDate" (String en formato YYYY-MM-DD).

                    Sigue estas reglas ESTRICTAMENTE y sin excepción:

                    ### REGLAS DE EXTRACCIÓN ###
                    1.  **REGLA DE ORO:** Solo crea un objeto JSON si el texto menciona explícitamente un **importe numérico**. Si no hay un número, ignora la frase por completo.

                    2.  **AMOUNT (Number):** Este es el campo más importante.
                        *   Debe ser un **NÚMERO**, no un string.
                        *   **FORMATO DECIMAL PRECISO:** El número DEBE tener siempre dos decimales para representar los centavos correctamente. Por ejemplo, "nueve centavos" es `.09`, no `.9`. "Trece dólares con nueve centavos" DEBE ser `13.09`.
                        *   **SIGNO:** Los **GASTOS** (compré, pagué, costó) deben ser **NEGATIVOS** (ej: -25.50). Los **INGRESOS** (me pagaron, recibí, salario) deben ser **POSITIVOS** (ej: 1200).

                    3.  **CURRENCY (String):** Usa el código de moneda ISO 4217 de 3 letras (ej: USD, EUR, MXN). Si se menciona un país o una moneda local (ej: "pesos"), usa el código más apropiado. Si no se menciona ninguna moneda, usa "USD" por defecto.

                    4.  **DESCRIPTION (String):** Una descripción concisa y clara de la transacción.

                    5.  **CATEGORY (String):** La categoría DEBE SER EXACTAMENTE UNA de las siguientes opciones. BAJO NINGUNA CIRCUNSTANCIA inventes, modifiques o uses una categoría que no esté en esta lista.
                        *   Si el `amount` es **positivo**, la categoría es siempre **"INCOME"**.
                        *   Si el `amount` es **negativo**, la categoría DEBE SER UNA de las siguientes:
                            - **FOOD**
                            - **RENT**
                            - **TRANSPORT**
                            - **UTILITIES**
                            - **ENTERTAINMENT**
                            - **HEALTH**
                            - **EDUCATION**
                        *   Si un gasto no encaja CLARAMENTE en ninguna de las anteriores, la única alternativa permitida es **"OTHER"**.

                    6.  **TRANSACTION_DATE (String):** Si se menciona una fecha explícita, conviértela a YYYY-MM-DD. Si NO se menciona ninguna fecha, omite el campo

                    7.  **SALIDA FINAL:** Si no encuentras transacciones válidas que cumplan todas estas reglas, devuelve un array JSON vacío: `[]`.

                    Texto del usuario:
                    "%s"

                    JSON de salida: este es el prompt para extraer la informacion con el formato concreto del texto de la transcripcion
                    """, transcriptionText);

            Map<String, Object> requestBody = Map.of(
                "contents", java.util.List.of(Map.of(
                    "parts", java.util.List.of(Map.of("text", prompt))
                )),
                "generationConfig", Map.of(
                    "temperature", 0.1,
                    "maxOutputTokens", 2048
                )
            );

            Map response = webClient.post()
                .uri("/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

            if (response != null && response.containsKey("candidates")) {
                java.util.List candidates = (java.util.List) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map candidate = (Map) candidates.get(0);
                    Map content = (Map) candidate.get("content");
                    java.util.List parts = (java.util.List) content.get("parts");
                    if (!parts.isEmpty()) {
                        Map part = (Map) parts.get(0);
                        return (String) part.get("text");
                    }
                }
            }

            return "[]";

        } catch (Exception e) {
            return "[]";
        }
    }

    public java.util.List<Transaction> transcribeAndCreateTransactions(byte[] audioBytes, String contentType, String userId) {
        log.info("Starting transcription and transaction creation for contentType: {}", contentType);
        String transcriptionText = transcribeAudioToText(audioBytes, contentType);
        log.info("Transcription text: {}", transcriptionText);
        String jsonResponse = extractTransactionsFromText(transcriptionText);
        log.debug("LLM JSON Response: {}", jsonResponse);
        java.util.List<TransactionInfo> infos = parseJsonResponse(jsonResponse);
        log.info("Parsed {} transaction infos", infos.size());
        java.util.List<Transaction> transactions = new java.util.ArrayList<>();
        for (TransactionInfo info : infos) {
            log.info("Creating transaction for amount: {}, currency: {}, description: {}", info.amount, info.currency, info.description);
            transactions.add(createAndSaveTransaction(info, userId));
        }
        log.info("Successfully created {} transactions", transactions.size());
        return transactions;
    }

    private java.util.List<TransactionInfo> parseJsonResponse(String jsonResponse) {
        try {
            // Strip markdown code blocks if present
            String cleanJson = jsonResponse.trim();
            if (cleanJson.startsWith("```json")) {
                cleanJson = cleanJson.substring(7);
            }
            if (cleanJson.endsWith("```")) {
                cleanJson = cleanJson.substring(0, cleanJson.length() - 3);
            }
            cleanJson = cleanJson.trim();

            // Parse JSON response as array
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            java.util.List<Map<String, Object>> jsonArray = mapper.readValue(cleanJson, java.util.List.class);

            java.util.List<TransactionInfo> infos = new java.util.ArrayList<>();
            for (Map<String, Object> jsonData : jsonArray) {
                BigDecimal amount = new BigDecimal(jsonData.getOrDefault("amount", "0.00").toString());
                String currency = (String) jsonData.getOrDefault("currency", "USD");
                String description = (String) jsonData.getOrDefault("description", "Transacción procesada");
                String category = (String) jsonData.getOrDefault("category", "OTHER");

                infos.add(new TransactionInfo(amount, currency, description, category));
            }
            return infos;

        } catch (Exception e) {
            // Fallback to regex parsing for single transaction
            TransactionInfo info = parseTransactionInfoRegex(jsonResponse);
            return java.util.List.of(info);
        }
    }

    // Fallback regex parsing
    private TransactionInfo parseTransactionInfoRegex(String text) {
        String amountPattern = "(\\d+[,.]\\d{2})";
        String currencyPattern = "(USD|EUR|COP|MXN)";
        String descriptionPattern = "(compra|gasto|pago|transferencia).*?(.+?)(?=\\s+(?:por\\s+)?\\d+[,.]\\d{2}|$)";

        java.util.regex.Pattern amountRegex = java.util.regex.Pattern.compile(amountPattern);
        java.util.regex.Pattern currencyRegex = java.util.regex.Pattern.compile(currencyPattern, java.util.regex.Pattern.CASE_INSENSITIVE);
        java.util.regex.Pattern descriptionRegex = java.util.regex.Pattern.compile(descriptionPattern, java.util.regex.Pattern.CASE_INSENSITIVE);

        java.util.regex.Matcher amountMatcher = amountRegex.matcher(text);
        java.util.regex.Matcher currencyMatcher = currencyRegex.matcher(text);
        java.util.regex.Matcher descriptionMatcher = descriptionRegex.matcher(text);

        String amountStr = amountMatcher.find() ? amountMatcher.group(1).replace(",", ".") : "0.00";
        String currencyStr = currencyMatcher.find() ? currencyMatcher.group(1).toUpperCase() : "COP";
        String descriptionStr = descriptionMatcher.find() ? descriptionMatcher.group(2) : "Transacción procesada";

        return new TransactionInfo(new BigDecimal(amountStr), currencyStr, descriptionStr, "OTHER");
    }

    private Transaction createAndSaveTransaction(TransactionInfo info, String userId) {
        Amount amount = Amount.of(info.amount, info.currency);
        Description description = new Description(info.description);
        Category category = Category.fromString(info.category);
        TransactionDate date = new TransactionDate(LocalDate.now());
        TransactionId id = TransactionId.of(UUID.randomUUID());
        UserId userIdObj = UserId.of(UUID.fromString(userId));

        Transaction transaction = Transaction.of(id, userIdObj, amount, description, category, date);
        return transactionRepository.save(transaction);
    }

    private record TransactionInfo(BigDecimal amount, String currency, String description, String category) {}
}
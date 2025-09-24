package com.lazyledger.trancriptionModule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

// Import the DTO class (adjust the package if needed)
public class DataExtractor {

    // Es buena práctica tener un ObjectMapper reutilizable
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final WebClient webClient = WebClient.builder()
        .baseUrl("https://generativelanguage.googleapis.com")
        .build();

    @SuppressWarnings("rawtypes")
    // Devuelve una lista para soportar múltiples transacciones en el futuro
    public static List<TransactionDataDto> extractTransactionData(String transcriptionText, String apiKey) {
        int defaultYear = LocalDate.now().getYear(); // Para manejar fechas sin año
        try {
            // El prompt ya está bien, pero he corregido la regla #6 para que coincida con la lógica del backend
            String prompt = String.format(
                    "Eres un asistente experto en finanzas personales llamado \"LazyLedger\". Tu tarea es analizar rigurosamente el siguiente texto y extraer TODAS las transacciones, aplicando un signo negativo para los gastos y positivo para los ingresos.\n\n" +
                    "Debes devolver el resultado como un array de objetos JSON. Cada objeto debe tener EXACTAMENTE los siguientes campos: \"amount\" (Number), \"currency\" (String), \"description\" (String), \"category\" (String), y \"transactionDate\" (String en formato YYYY-MM-DD).\n\n" +
                    "Sigue estas reglas ESTRICTAMENTE y sin excepción:\n\n" +
                    "### REGLAS DE EXTRACCIÓN ###\n" +
                    "1.  **REGLA DE ORO:** Solo crea un objeto JSON si el texto menciona explícitamente un **importe numérico**. Si no hay un número, ignora la frase por completo.\n\n" +
                    "2.  **AMOUNT (Number):** Este es el campo más importante.\n" +
                    "    *   Debe ser un **NÚMERO**, no un string.\n" +
                    "    *   **FORMATO DECIMAL PRECISO:** El número DEBE tener siempre dos decimales para representar los centavos correctamente. Por ejemplo, \"nueve centavos\" es `.09`, no `.9`. \"Trece dólares con nueve centavos\" DEBE ser `13.09`.\n" +
                    "    *   **SIGNO:** Los **GASTOS** (compré, pagué, costó) deben ser **NEGATIVOS** (ej: -25.50). Los **INGRESOS** (me pagaron, recibí, salario) deben ser **POSITIVOS** (ej: 1200).\n\n" +
                    "3.  **CURRENCY (String):** Usa el código de moneda ISO 4217 de 3 letras (ej: USD, EUR, MXN). Si se menciona un país o una moneda local (ej: \"pesos\"), usa el código más apropiado. Si no se menciona ninguna moneda, usa \"USD\" por defecto.\n\n" +
                    "4.  **DESCRIPTION (String):** Una descripción max 255 caracteres concisa y clara de la transacción.\n\n" +
                    "5.  **CATEGORY (String):** La categoría DEBE SER EXACTAMENTE UNA de las siguientes opciones. BAJO NINGUNA CIRCUNSTANCIA inventes, modifiques o uses una categoría que no esté en esta lista.\n" +
                    "    *   Si el `amount` es **positivo**, la categoría es siempre **\"INCOME\"**.\n" +
                    "    *   Si el `amount` es **negativo**, la categoría DEBE SER UNA de las siguientes:\n" +
                    "        - **FOOD**\n" +
                    "        - **RENT**\n" +
                    "        - **TRANSPORT**\n" +
                    "        - **UTILITIES**\n" +
                    "        - **ENTERTAINMENT**\n" +
                    "        - **HEALTH**\n" +
                    "        - **EDUCATION**\n" +
                    "    *   Si un gasto no encaja CLARAMENTE en ninguna de las anteriores, la única alternativa permitida es **\"OTHER\"**.\n\n" +
                    "6.  **TRANSACTION_DATE (String):** Si se menciona una fecha explícita, conviértela a YYYY-MM-DD si el año es mencionado establece con " + defaultYear + ". Si NO se menciona ninguna fecha, omite el campo.\n\n" +
                    "7.  **SALIDA FINAL:** Si no encuentras transacciones válidas que cumplan todas estas reglas, devuelve un array JSON vacío: `[]`.\n\n" +
                    "Texto del usuario:\n" +
                    "\"\"\"\n" +
                    "%s\n" +
                    "\"\"\"\n\n" +
                    "JSON de salida:\n", 
                    transcriptionText);

                    
            
            Map<String, Object> requestBody = Map.of(
                "contents", List.of(Map.of(
                    "parts", List.of(Map.of("text", prompt))
                )),
                "generationConfig", Map.of(
                    "response_mime_type", "application/json", // Forzar salida JSON
                    "temperature", 0.1,
                    "maxOutputTokens", 2048
                )
            );
            Map response = webClient.post()
                .uri("/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

            if (response != null && response.containsKey("candidates")) {
                List candidates = (List) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map candidate = (Map) candidates.get(0);
                    Map content = (Map) candidate.get("content");
                    List parts = (List) content.get("parts");
                    if (!parts.isEmpty()) {
                        Map firstPart = (Map) parts.get(0);
                        // 2. Obtener el texto de ese mapa.
                        String jsonText = (String) firstPart.get("text");

                        // Limpiar el string JSON de los bloques de markdown
                        String cleanJson = jsonText.trim()
                                .replace("```json", "")
                                .replace("```", "")
                                .trim();

                        // Parsear el JSON a una Lista de DTOs para ser más flexible
                        List<Map> transactionsList = objectMapper.readValue(cleanJson, new TypeReference<>() {});

                        if (!transactionsList.isEmpty()) {
                             // NOTA: Este código ahora solo procesa la primera transacción.
                             // Se podría modificar para que devuelva la lista completa.
                            @SuppressWarnings("unchecked")
                            Map data = (Map<String, Object>) transactionsList.get(0);
                            
                            
                            BigDecimal amount = new BigDecimal(data.getOrDefault("amount", "0.00").toString());
                            String currency = (String) data.getOrDefault("currency", "USD");
                            String description = (String) data.getOrDefault("description", "Transacción procesada");
                            String category = (String) data.getOrDefault("category", "OTHER");
                            
                            // --- MANEJO DE FECHA NULA ---
                            // Si la fecha es nula o no existe, usa la de hoy.
                            String transactionDateStr = (String) data.get("transactionDate");
                            if (transactionDateStr == null || transactionDateStr.isBlank()) {
                                transactionDateStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
                            }

                            var dto = new TransactionDataDto(amount, currency, description, category, transactionDateStr);
                            return List.of(dto); // Devolvemos una lista con un solo elemento
                        }
                    }
                }
            }

            return List.of(); // Devolver lista vacía en lugar de null

        } catch (Exception e) {
            // Es CRUCIAL registrar el error para poder depurarlo
            e.printStackTrace();
            return List.of(); // Devolver lista vacía en caso de error
        }
    }

    public static void main(String[] args) {
        String fakeApiKey = "AIzaSyC4grZUp-8en0MjFYXXiFZivmHHgL-9aMo";
        List<TransactionDataDto> transactions = extractTransactionData(
            "pagué 45.50 USD en comida.",
            fakeApiKey
        );
        transactions.forEach(t -> System.out.println(t));
          // Prueba rápida del extractor con un texto de ejemplo
    }
}
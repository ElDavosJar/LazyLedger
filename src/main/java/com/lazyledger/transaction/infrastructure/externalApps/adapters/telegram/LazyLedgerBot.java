package com.lazyledger.transaction.infrastructure.externalApps.adapters.telegram;

import com.lazyledger.transaction.application.TransactionPerUserUseCase;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import com.lazyledger.transaction.infrastructure.externalApps.drivers.ApiListener;

import java.util.List;
import java.util.UUID;

@Component
public class LazyLedgerBot extends TelegramLongPollingBot {

    @Autowired
    private ApiListener apiListener;

    @Autowired
    private MessageProcessorService messageProcessorService;

    @Autowired
    private TransactionPerUserUseCase transactionPerUserUseCase;

    @Value("${telegram.bot.username}")
    private String botUsername;

    // CONSTRUCTOR SIMPLE - EL TOKEN SE PASA DIRECTAMENTE DESDE LA LIBRERÍA
    public LazyLedgerBot(@Value("${telegram.bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            var message = update.getMessage();
            if (message.hasDocument() || message.hasAudio() || message.hasPhoto() || message.hasVoice()) {
                // Process file for transaction
                MessageDto dto = apiListener.getMessageFromUpdate(update);
                if (dto != null) {
                    messageProcessorService.processMessage(dto);
                }
            } else if (message.hasText()) {
                sendMainMenu(message.getChatId());
            }
        }
    }

    private void sendMainMenu(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText("¡Hola! Soy LazyLedger. ¿Qué deseas hacer?");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = List.of(
            List.of(
                InlineKeyboardButton.builder()
                    .text("Registrar transacción")
                    .callbackData("register_transaction")
                    .build()
            ),
            List.of(
                InlineKeyboardButton.builder()
                    .text("Consultar balance mensual")
                    .callbackData("balance_monthly")
                    .build(),
                InlineKeyboardButton.builder()
                    .text("Consultar balance semanal")
                    .callbackData("balance_weekly")
                    .build()
            )
        );
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleCallbackQuery(org.telegram.telegrambots.meta.api.objects.CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();

        // Default user for bot interactions
        UUID defaultUserId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        switch (data) {
            case "register_transaction":
                sendMessage(chatId, "Envía un audio, imagen o documento con la transacción a registrar.");
                break;
            case "balance_monthly":
                var monthlyBalance = transactionPerUserUseCase.getMonthlyBalance(defaultUserId);
                sendMessage(chatId, "Balance mensual: $" + monthlyBalance);
                break;
            case "balance_weekly":
                var weeklyBalance = transactionPerUserUseCase.getWeeklyBalance(defaultUserId);
                sendMessage(chatId, "Balance semanal: $" + weeklyBalance);
                break;
            default:
                sendMessage(chatId, "Opción no reconocida.");
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    // --- EL MÉTODO DE ARRANQUE MANUAL Y BRUTAL ---
    @PostConstruct
    public void register() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            System.out.println(">>> INTENTANDO CONECTAR... ENVÍA UN MENSAJE A TU BOT AHORA.");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
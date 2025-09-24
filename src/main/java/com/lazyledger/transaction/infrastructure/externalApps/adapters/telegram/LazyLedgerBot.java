package com.lazyledger.transaction.infrastructure.externalApps.adapters.telegram;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import com.lazyledger.transaction.infrastructure.externalApps.drivers.ApiListener;

@Component
public class LazyLedgerBot extends TelegramLongPollingBot {

    @Autowired
    private ApiListener apiListener;

    @Autowired
    private MessageProcessorService messageProcessorService;

    @Value("${telegram.bot.username}")
    private String botUsername;

    // CONSTRUCTOR SIMPLE - EL TOKEN SE PASA DIRECTAMENTE DESDE LA LIBRERÍA
    public LazyLedgerBot(@Value("${telegram.bot.token}") String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        MessageDto dto = apiListener.getMessageFromUpdate(update);
        if (dto != null) {
            messageProcessorService.processMessage(dto);
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
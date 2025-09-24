package com.lazyledger.transaction.infrastructure.externalApps.adapters.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.lazyledger.transaction.infrastructure.externalApps.drivers.ApiListener;

import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("rawtypes")
public class TelegramApiListener implements ApiListener {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public MessageDto getMessageFromUpdate(Update update) {
        if (update.hasMessage()) {
            String chatId = update.getMessage().getChatId().toString();
            String content = null;
            String msg = null;
            byte[] binaryData = null;
            String mediaType = null;

            try {
                if (update.getMessage().hasText()) {
                    content = update.getMessage().getText();
                    mediaType = "text";
                } else if (update.getMessage().hasDocument()) {
                    String fileId = update.getMessage().getDocument().getFileId();
                    binaryData = downloadFile(fileId);
                    content = update.getMessage().getDocument().getFileName();
                    msg = update.getMessage().getCaption();
                    String mimeType = update.getMessage().getDocument().getMimeType();
                    mediaType = (mimeType != null && mimeType.startsWith("image/")) ? "image" : "doc";
                } else if (update.getMessage().hasPhoto()) {
                    List<PhotoSize> photos = update.getMessage().getPhoto();
                    if (!photos.isEmpty()) {
                        PhotoSize photo = photos.get(photos.size() - 1); // Highest resolution
                        String fileId = photo.getFileId();
                        binaryData = downloadFile(fileId);
                        content = "Photo";
                        msg = update.getMessage().getCaption();
                        mediaType = "image";
                    }
                } else if (update.getMessage().hasAudio()) {
                    String fileId = update.getMessage().getAudio().getFileId();
                    binaryData = downloadFile(fileId);
                    content = update.getMessage().getAudio().getTitle() != null ? update.getMessage().getAudio().getTitle() : "Audio";
                    msg = update.getMessage().getCaption();
                    mediaType = "audio";
                } else if (update.getMessage().hasVoice()) {
                    String fileId = update.getMessage().getVoice().getFileId();
                    binaryData = downloadFile(fileId);
                    content = "Voice";
                    msg = update.getMessage().getCaption();
                    mediaType = "audio";
                } else if (update.getMessage().hasVideo()) {
                    String fileId = update.getMessage().getVideo().getFileId();
                    binaryData = downloadFile(fileId);
                    content = "Video";
                    msg = update.getMessage().getCaption();
                    mediaType = "video";
                } else {
                    content = "Unsupported message type";
                    mediaType = "unknown";
                }
            } catch (Exception e) {
                content = "Error processing message: " + e.getMessage();
                mediaType = "error";
            }

            return new MessageDto(chatId, content, msg, binaryData, mediaType);
        }
        return null;
    }

    private byte[] downloadFile(String fileId) {
        String url = "https://api.telegram.org/bot" + botToken + "/getFile?file_id=" + fileId;
        Map response = restTemplate.getForObject(url, Map.class);
        Map result = (Map) response.get("result");
        String filePath = (String) result.get("file_path");
        String downloadUrl = "https://api.telegram.org/file/bot" + botToken + "/" + filePath;
        return restTemplate.getForObject(downloadUrl, byte[].class);
    }
}
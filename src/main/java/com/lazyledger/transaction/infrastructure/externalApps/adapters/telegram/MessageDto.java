package com.lazyledger.transaction.infrastructure.externalApps.adapters.telegram;

public class MessageDto {

    private String chatId;
    private String content; // text content or file name/type
    private String msg; // caption, can be null
    private byte[] binaryData; // raw binary data for files, null for text
    private String mediaType; // "text", "audio", "image", "doc", etc.

    public MessageDto() {}

    public MessageDto(String chatId, String content, String msg, byte[] binaryData, String mediaType) {
        this.chatId = chatId;
        this.content = content;
        this.msg = msg;
        this.binaryData = binaryData;
        this.mediaType = mediaType;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "chatId='" + chatId + '\'' +
                ", content='" + content + '\'' +
                ", msg='" + msg + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", hasBinaryData=" + (binaryData != null) +
                '}';
    }
}
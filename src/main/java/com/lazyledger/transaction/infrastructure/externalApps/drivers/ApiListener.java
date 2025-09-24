package com.lazyledger.transaction.infrastructure.externalApps.drivers;

import org.telegram.telegrambots.meta.api.objects.Update;
import com.lazyledger.transaction.infrastructure.externalApps.adapters.telegram.MessageDto;

public interface ApiListener {

    MessageDto getMessageFromUpdate(Update update);

}
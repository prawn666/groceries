package ru.vlados.groceries.tg.client;

import com.pengrad.telegrambot.TelegramBot;

public interface TgClient {

    TelegramBot createConnection();

    TelegramBot getBot();

}

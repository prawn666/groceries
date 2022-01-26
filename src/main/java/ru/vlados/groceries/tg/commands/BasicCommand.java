package ru.vlados.groceries.tg.commands;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vlados.groceries.tg.client.TgClient;

@Slf4j
public abstract class BasicCommand implements Command {

    protected BotCommand botCommand;
    @Autowired
    protected TgClient tgClient;
    //todo делать как бины, сделать бин на регистрацию и при вызове конструктора регать

    public BasicCommand(String command, String description) {
        this.botCommand = new BotCommand(command, description);
    }

    protected void sendMessage(String text, long chatId) {
        TelegramBot bot = tgClient.getBot();
        SendMessage sendMessage = new SendMessage(chatId, text);
        bot.execute(sendMessage, new Callback<SendMessage, SendResponse>() {

            @Override
            public void onResponse(SendMessage sendMessage, SendResponse sendResponse) {
                log.info("Sent message {}", sendMessage.toWebhookResponse());
            }

            @Override
            public void onFailure(SendMessage sendMessage, IOException e) {
                log.error("error in send message", e);
            }
        });
    }

    @Override
    public BotCommand getBotCommand() {
        return this.botCommand;
    }
}

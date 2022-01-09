package ru.vlados.groceries.tg.client;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.DeleteWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import java.io.IOException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.vlados.groceries.configuration.TgProps;
import ru.vlados.groceries.tg.controller.TgUpdatesController;

@Data
@Slf4j
@Service
@Profile("local")
public class LongPollTgClient implements TgClient {

    private final TgProps tgProps;
    private final TgUpdatesController controller;
    private TelegramBot bot;

    public LongPollTgClient(@Autowired TgProps tgProps, @Lazy TgUpdatesController controller) {
        this.tgProps = tgProps;
        this.controller = controller;
        this.bot = new TelegramBot(tgProps.getToken());
    }

    @Override
    public TelegramBot createConnection() {
        setUpdateListener(e -> {
            if (HttpStatus.CONFLICT.value() == e.response().errorCode()) {
                deleteWebhook(bot);
                setUpdateListener(e2 -> log.error("Error getting updates after delete webhook", e2));
            }
            log.error("Error getting updates", e);
        });
        return bot;
    }

    private void setUpdateListener(ExceptionHandler exceptionHandler) {
        bot.setUpdatesListener(updates -> {
            updates.forEach(controller::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, exceptionHandler);
    }

    private void deleteWebhook(TelegramBot bot) {
        DeleteWebhook deleteWebhook = new DeleteWebhook();
        bot.execute(deleteWebhook, new Callback<DeleteWebhook, BaseResponse>() {
            @Override
            public void onResponse(DeleteWebhook deleteWebhook, BaseResponse baseResponse) {
                log.info("Deleted webhook {}, response {}",
                    deleteWebhook.toWebhookResponse(), baseResponse.toString());
            }

            @Override
            public void onFailure(DeleteWebhook deleteWebhook, IOException e) {
                log.error("Did not delete webhook {}", deleteWebhook.toWebhookResponse(),
                    e);
            }
        });
    }
}

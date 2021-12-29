package ru.vlados.groceries.tg.client;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.DeleteWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import java.io.IOException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.vlados.groceries.configuration.TgProps;
import ru.vlados.groceries.tg.controller.TgUpdatesController;

@Data
@Slf4j
@Service
@Profile("local")
@RequiredArgsConstructor
public class LongPollTgClient implements TgClient {

    private final TgProps tgProps;
    private final TgUpdatesController controller;
    private TelegramBot bot;

    @Override
    public TelegramBot createConnection() {
        bot = new TelegramBot(tgProps.getToken());
        bot.setUpdatesListener(updates -> {
            updates.forEach(controller::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (HttpStatus.CONFLICT.value() == e.response().errorCode()) {
                deleteWebhook(bot);
            }
            log.error("Error getting updates", e);
        });
        return bot;
    }

    private void deleteWebhook(TelegramBot bot) {
        DeleteWebhook deleteWebhook = new DeleteWebhook();
        bot.execute(deleteWebhook, new Callback<DeleteWebhook, BaseResponse>() {
            @Override
            public void onResponse(DeleteWebhook deleteWebhook, BaseResponse baseResponse) {
                log.info("Deleted webhook {}, response {}",
                    deleteWebhook.toWebhookResponse(), baseResponse.toString());
                createConnection();
            }

            @Override
            public void onFailure(DeleteWebhook deleteWebhook, IOException e) {
                log.error("Did not delete webhook {}", deleteWebhook.toWebhookResponse(),
                    e);
            }
        });
    }

//    public  <T extends BaseRequest<T, R>, R extends BaseResponse> void kek(Command command, T request) {
//        bot.execute(request, new Callback<T, R>() {
//            @Override
//            public void onResponse(T t, R r) {
//                command.execute();
//            }
//
//            @Override
//            public void onFailure(T t, IOException e) {
//
//            }
//        });
//    }
}

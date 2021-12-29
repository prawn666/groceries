package ru.vlados.groceries.tg.client;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SetWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import java.io.File;
import java.io.IOException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.vlados.groceries.configuration.TgProps;

@Data
@Slf4j
@Service
@Profile("prod")
@RequiredArgsConstructor
public class WebhookTgClient implements TgClient {

    private final TgProps tgProps;
    private TelegramBot bot;

    public TelegramBot createConnection() {
        bot = new TelegramBot(tgProps.getToken());
        SetWebhook webhook = new SetWebhook();
        webhook.url(tgProps.getWebhookUrl());
        webhook.certificate(new File("src/main/resources/ssl-certificate.crt"));
        bot.execute(webhook, new Callback<SetWebhook, BaseResponse>() {
            @Override
            public void onResponse(SetWebhook setWebhook, BaseResponse baseResponse) {
                log.info("Successfully set webhook {}, description {}", webhook.toWebhookResponse(),
                    baseResponse.description());
            }

            @Override
            public void onFailure(SetWebhook setWebhook, IOException e) {
                log.error("Error in creating webhook {}", webhook.toWebhookResponse(), e);
            }
        });
        return bot;
    }
}

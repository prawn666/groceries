package ru.vlados.groceries.tg.service;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.vlados.groceries.tg.client.TgClient;
import ru.vlados.groceries.tg.commands.BasicCommand;

//@Order(1)
@Service
@RequiredArgsConstructor
public class SetCommandsService {

    private final TgService tgService;
    private final TgClient tgClient;

    @PostConstruct
    private void setClient() {
        sendSetMyCommands(tgService.getBotCommandMap().values()
            .stream()
            .map(BasicCommand::getBotCommand)
            .toArray(BotCommand[]::new));
        tgClient.createConnection();
    }

    private void sendSetMyCommands(BotCommand[] commands) {
        SetMyCommands setMyCommands = new SetMyCommands(commands);
        tgClient.getBot().execute(setMyCommands);
    }

}

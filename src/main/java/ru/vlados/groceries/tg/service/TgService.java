package ru.vlados.groceries.tg.service;

import com.pengrad.telegrambot.model.Update;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import ru.vlados.groceries.tg.commands.BasicCommand;

@Service
public class TgService {

    @Getter
    private final Map<String, BasicCommand> botCommandMap = new HashMap<>();

    //todo add register action для лички шоб туда писать (регистер по коду с чата, ограничить время жизни кода) или же кидать chatid и проверять является ли чат мембером

    public Flux<?> execute(Update update) {
        return Flux.just(update)
            .map(this::validate)
            .map(this::mapToTextArr)
            .doOnNext(command -> botCommandMap.getOrDefault(command[0], botCommandMap.get("/notFound"))
                .execute(update, command));
    }

    private Update validate(Update update) {
        if (!StringUtils.hasText(update.message().text())) {
            //todo норм эксепшен
            throw new RuntimeException("empty message");
        }
        return update;
    }

    public String[] mapToTextArr(Update update) {
        String[] splitText = update.message().text().split(" ");
        if (splitText.length == 0) {
            throw new RuntimeException("empty message");
        }
        splitText[0] = splitText[0].split("@")[0];
        return splitText;
    }

    public void addCommand(BasicCommand command) {
        botCommandMap.put(command.getBotCommand().command(), command);
    }

}

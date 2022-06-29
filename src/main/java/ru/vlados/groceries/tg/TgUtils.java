package ru.vlados.groceries.tg;

import com.pengrad.telegrambot.model.Update;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TgUtils {

    public static Long getUserId(Update update) {
        return update.message().from().id();
    }
}

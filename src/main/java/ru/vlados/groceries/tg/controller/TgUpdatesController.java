package ru.vlados.groceries.tg.controller;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.vlados.groceries.tg.service.TgService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TgUpdatesController {

    private final TgService tgService;

    @PostMapping("/webhook")
    public void processUpdate(Update update) {
        //todo хранить контекст того где ты типо хранить в редисе контекст и удалять его как закончится (и перезаписывать)
        Flux.just(update)
            .doOnNext(upd -> log.info("Incoming update {}", upd))
            .flatMap(tgService::execute)
            .subscribe();
    }

}

package ru.urfu.logics;

import org.jetbrains.annotations.NotNull;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.localobjects.Request;

/**
 * Логическое ядро эхо-бота.
 * Отправляет назад несколько изменённое сообщение пользователя.
 * Обрабатывает команды /help и /start, отвечая на них справкой.
 */
public final class EchoBotCore implements LogicCore {
    private final static String START_COMMAND = "/start";
    private final static String HELP_COMMAND = "/help";

    /**
     * Обрабатывает всю информацию, полученную с ботов.
     * @param request сообщение, которое нужно обработать
     */
    @Override
    public void processMessage(@NotNull Request request) {
        if (request.message().text() == null) {
            return;
        }

        switch (request.message().text()) {
            case START_COMMAND, HELP_COMMAND -> helpCommandHandler(request);
            default -> defaultHandler(request);
        }
    }

    /**
     * Обрабатывает сообщения, не распознанные как заявленные команды.
     * @param request запрос.
     */
    private void defaultHandler(@NotNull Request request) {
        final LocalMessage answer = new LocalMessageBuilder()
                .text("Ты написал: " + request.message().text())
                .build();
        request.bot().sendMessage(answer, request.id());
    }

    /**
     * Выдаёт справку.
     * @param request запрос.
     */
    private void helpCommandHandler(@NotNull Request request) {
        final String HELP_MESSAGE = """
                Привет, я эхо бот! Сейчас я расскажу как ты можешь со мной взаимодействовать.
                Пассивная способность: Я пишу твое сообщение тебе обратно но добавляю фразу 'Ты написал:' в начало \
                твоего сообщения!

                /help - Показать доступные команды.
                /start - Начинает диалог с начала. (нет)
                Приятного использования!""";

        final LocalMessage answer = new LocalMessageBuilder()
                .text(HELP_MESSAGE)
                .build();
        request.bot().sendMessage(answer, request.id());
    }
}

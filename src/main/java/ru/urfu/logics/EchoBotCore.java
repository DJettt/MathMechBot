package ru.urfu.logics;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.BotProcessMessageRequest;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;

/**
 * <p>Логическое ядро эхо-бота.</p>
 *
 * <p>Отправляет назад несколько изменённое сообщение пользователя.
 * Обрабатывает команды /help и /start, отвечая на них справкой.</p>
 */
public final class EchoBotCore implements LogicCore {
    final static String START_COMMAND = "/start";
    final static String HELP_COMMAND = "/help";

    @Override
    public void processMessage(@NotNull BotProcessMessageRequest request) {
        if (request.message().text() == null) {
            return;
        }

        switch (request.message().text()) {
            case START_COMMAND, HELP_COMMAND -> helpCommandHandler(request);
            default -> defaultHandler(request);
        }
    }

    /**
     * <p>Обрабатывает сообщения, не распознанные как заявленные команды.</p>
     *
     * @param request запрос.
     */
    private void defaultHandler(@NotNull BotProcessMessageRequest request) {
        final LocalMessage answer = new LocalMessageBuilder()
                .text("Ты написал: " + request.message().text())
                .build();
        request.bot().sendMessage(answer, request.id());
    }

    /**
     * <p>Выдаёт справку.</p>
     *
     * @param request запрос.
     */
    private void helpCommandHandler(@NotNull BotProcessMessageRequest request) {
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

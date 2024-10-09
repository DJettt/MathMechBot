package ru.urfu.logics;

import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;

/**
 * Логическое ядро эхо-бота.
 * Отправляет назад несколько изменённое сообщение пользователя.
 * Обрабатывает команды /help и /start, отвечая на них справкой.
 */
public final class EchoBotCore implements LogicCore {
    final static String START_COMMAND = "/start";
    final static String HELP_COMMAND = "/help";

    /**
     * Обрабатывает всю информацию, полученную с ботов.
     * @param msg сообщение, которое нужно обработать
     */
    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        if (msg.text() == null) {
            return;
        }

        switch (msg.text()) {
            case START_COMMAND, HELP_COMMAND -> helpCommandHandler(msg, chatId, bot);
            default -> defaultHandler(msg, chatId, bot);
        }
    }

    /**
     * Обрабатывает сообщения, не распознанные как заявленные команды.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void defaultHandler(LocalMessage inputMessage, long chatId, Bot bot) {
        final LocalMessage answer = new LocalMessageBuilder()
                .text("Ты написал: " + inputMessage.text())
                .build();
        bot.sendMessage(answer, chatId);
    }

    /**
     * Выдаёт справку.
     * @param inputMessage входящее сообщение с командой /help
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void helpCommandHandler(LocalMessage inputMessage, Long chatId, Bot bot) {
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
        bot.sendMessage(answer, chatId);
    }
}

package ru.urfu.logics.mathmechbot;


import java.util.ArrayList;
import java.util.List;
import ru.urfu.bots.Bot;
import ru.urfu.enums.RegistrationState;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.State;



/**
 * Состояние ожидания ответа на запрос года обучения во время регистрации.
 */
public class RegistrationYearState extends MathMechBotState {
    private final static LocalMessage ON_ENTER_MESSAGE = new LocalMessageBuilder()
            .text("На каком курсе Вы обучаетесь?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("1 курс", "1"),
                    new LocalButton("2 курс", "2"),
                    new LocalButton("3 курс", "3"),
                    new LocalButton("4 курс", "4"),
                    new LocalButton("5 курс", "5"),
                    new LocalButton("6 курс", "6"),
                    BACK_BUTTON
            )))
            .build();

    /**
     * Конструктор состояния.
     *
     * @param context контекст (в том же смысле, что и в паттерне "State").
     */
    public RegistrationYearState(MathMechBotCore context) {
        super(context);
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        switch (msg.text()) {
            case BACK_COMMAND -> backCommandHandler(msg, chatId, bot);
            case null -> {
                bot.sendMessage(TRY_AGAIN, chatId);
                bot.sendMessage(ON_ENTER_MESSAGE, chatId);
            }
            default -> textHandler(msg, chatId, bot);
        }
    }

    @Override
    public void onEnter(LocalMessage msg, long chatId, Bot bot) {
        bot.sendMessage(ON_ENTER_MESSAGE, chatId);
    }

    /**
     * Возвращаем пользователя на шаг назад, т.е. на запрос ФИО
     *
     * @param message полученное сообщение
     * @param chatId  идентификатор чата
     * @param bot     бот, принявший сообщение
     */
    private void backCommandHandler(LocalMessage message, long chatId, Bot bot) {
        context.users.changeUserState(chatId, RegistrationState.NAME);
        final State newState = new RegistrationFullNameState(context);
        newState.onEnter(message, chatId, bot);
        context.changeState(newState);
    }

    /**
     * Проверяем различные текстовые сообщения.
     * Если текстовое сообщение является числом от 1 до 6 (проходит валидацию),
     * пользователь перемещается на следующее состояние, то есть запрос специальности.
     * Если год обучения - 1, отправляем в запрос специальности первокурсника (так как они отличаются от других курсов).
     * Если год обучения иной, отправляем в запрос специальности поздних курсов.
     * В противном случае просим пользователя повторить ввод.
     *
     * @param message полученное сообщение
     * @param chatId  идентификатор чата
     * @param bot     бот, принявший сообщение
     */
    public void textHandler(LocalMessage message, long chatId, Bot bot) {
        assert message.text() != null;

        try {
            final int maxYear = 6;
            final int year = Integer.parseInt(message.text().trim());
            if (year == 1) {
                context.userEntries.changeUserEntryYear(chatId, year);
                context.users.changeUserState(chatId, RegistrationState.SPECIALTY1);

                final State newState = new RegistrationFirstYearSpecialtiesState(context);
                newState.onEnter(message, chatId, bot);
                context.changeState(newState);
            } else if (year > 1 && year <= maxYear) {
                context.userEntries.changeUserEntryYear(chatId, year);
                context.users.changeUserState(chatId, RegistrationState.SPECIALTY2);

                final State newState = new RegistrationLaterYearSpecialitiesState(context);
                newState.onEnter(message, chatId, bot);
                context.changeState(newState);
            } else {
                bot.sendMessage(TRY_AGAIN, chatId);
                bot.sendMessage(ON_ENTER_MESSAGE, chatId);
            }
        } catch (NumberFormatException e) {
            bot.sendMessage(TRY_AGAIN, chatId);
            bot.sendMessage(ON_ENTER_MESSAGE, chatId);
        }
    }
}

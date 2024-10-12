package ru.urfu.logics.mathmechbot.states.registration;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;


/**
 * Состояние ожидания ответа на запрос года обучения во время регистрации.
 */
public enum RegistrationYearState implements MathMechBotState {
    INSTANCE;

    private final static Pattern VALID_YEAR_STRING_PATTERN = Pattern.compile("^[1-6]$");
    private final static LocalMessage ON_ENTER_MESSAGE = new LocalMessageBuilder()
            .text("На каком курсе Вы обучаетесь?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("1 курс", "1"),
                    new LocalButton("2 курс", "2"),
                    new LocalButton("3 курс", "3"),
                    new LocalButton("4 курс", "4"),
                    new LocalButton("5 курс", "5"),
                    new LocalButton("6 курс", "6"),
                    Constants.BACK_BUTTON
            )))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull LocalMessage msg,
                               long chatId, @NotNull Bot bot) {
        switch (msg.text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(context, chatId, bot);
            case null -> {
                bot.sendMessage(Constants.TRY_AGAIN, chatId);
                bot.sendMessage(ON_ENTER_MESSAGE, chatId);
            }
            default -> textHandler(context, msg, chatId, bot);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, long userId) {
        return ON_ENTER_MESSAGE;
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть на запрос ФИО.
     *
     * @param context логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param bot    бот, принявший сообщение
     */
    private void backCommandHandler(MathMechBotCore context, long chatId, Bot bot) {
        context.storage.users.changeUserState(chatId, MathMechBotUserState.REGISTRATION_NAME);
        bot.sendMessage(RegistrationFullNameState.INSTANCE.enterMessage(context, chatId), chatId);
    }

    /**
     * Проверяем различные текстовые сообщения.
     * Если текстовое сообщение является числом от 1 до 6 (проходит валидацию),
     * пользователь перемещается на следующее состояние, то есть запрос специальности.
     * Если год обучения - 1, отправляем в запрос специальности первокурсника (так как они отличаются от других курсов).
     * Если год обучения иной, отправляем в запрос специальности поздних курсов.
     * В противном случае просим пользователя повторить ввод.
     *
     * @param context логического ядро (контекст для состояния).
     * @param message полученное сообщение
     * @param chatId  идентификатор чата
     * @param bot     бот, принявший сообщение
     */
    public void textHandler(MathMechBotCore context, LocalMessage message, long chatId, Bot bot) {
        assert message.text() != null;

        int year;
        try {
            year = Integer.parseInt(message.text().trim());
        } catch (NumberFormatException e) {
            bot.sendMessage(Constants.TRY_AGAIN, chatId);
            bot.sendMessage(ON_ENTER_MESSAGE, chatId);
            return;
        }

        if (message.text().equals("1")) {
            context.storage.userEntries.changeUserEntryYear(chatId, year);
            context.storage.users.changeUserState(chatId, MathMechBotUserState.REGISTRATION_SPECIALTY1);

            final LocalMessage msg = RegistrationFirstYearSpecialtiesState.INSTANCE.enterMessage(context, chatId);
            bot.sendMessage(msg, chatId);
        } else if (VALID_YEAR_STRING_PATTERN.matcher(message.text()).matches()) {
            context.storage.userEntries.changeUserEntryYear(chatId, year);
            context.storage.users.changeUserState(chatId, MathMechBotUserState.REGISTRATION_SPECIALTY2);

            final LocalMessage msg = RegistrationLaterYearSpecialitiesState.INSTANCE.enterMessage(context, chatId);
            bot.sendMessage(msg, chatId);
        } else {
            bot.sendMessage(Constants.TRY_AGAIN, chatId);
            bot.sendMessage(ON_ENTER_MESSAGE, chatId);
        }
    }
}

package ru.urfu.mathmechbot;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;

/**
 * <p>Тесты регистрации.</p>
 *
 * <p>@SuppressWarnings("MagicNumber") -- потому что индексы.</p>
 * <p>@SuppressWarnings("MagicNumber") -- чтобы не жаловался на текст кнопок.</p>
 */
@DisplayName("Регистрация")
@SuppressWarnings({"MagicNumber", "MultipleStringLiterals"})
public final class RegistrationTest {
    private final static String ACCEPT_COMMAND = "/yes";
    private final static String DECLINE_COMMAND = "/no";
    private final static String BACK_COMMAND = "/back";
    private final static String REGISTER_COMMAND = "/register";
    private final static String INFO_COMMAND = "/info";

    private final LocalButton backButton = new LocalButton("Назад", BACK_COMMAND);
    private final List<LocalButton> yesNoBack = List.of(
            new LocalButton("Да", ACCEPT_COMMAND),
            new LocalButton("Нет", DECLINE_COMMAND),
            backButton
    );

    private final LocalMessage askForRegistration =
            new LocalMessage("Сперва нужно зарегистрироваться.");

    private final LocalMessage askFullName = new LocalMessageBuilder()
            .text("""
                    Введите свое ФИО в формате:
                    Иванов Артём Иванович
                    Без дополнительных пробелов и с буквой ё, если нужно.""")
            .buttons(List.of(backButton))
            .build();

    private final LocalMessage askYear = new LocalMessageBuilder()
            .text("На каком курсе Вы обучаетесь?")
            .buttons(List.of(
                    new LocalButton("1 курс", "1"),
                    new LocalButton("2 курс", "2"),
                    new LocalButton("3 курс", "3"),
                    new LocalButton("4 курс", "4"),
                    new LocalButton("5 курс", "5"),
                    new LocalButton("6 курс", "6"),
                    backButton
            ))
            .build();

    private final List<LocalButton> askFirstYearSpecialtyButtons = List.of(
            new LocalButton("КНМО", "КНМО"),
            new LocalButton("ММП", "ММП"),
            new LocalButton("КБ", "КБ"),
            new LocalButton("ФТ", "ФТ"),
            backButton
    );
    private final LocalMessage askFirstYearSpecialty = new LocalMessageBuilder()
            .text("""
                    На каком направлении?
                    Если Вы не видите свое направление, то, возможно, Вы выбрали не тот курс.""")
            .buttons(askFirstYearSpecialtyButtons)
            .build();

    private final List<LocalButton> askLaterYearSpecialtyButtons = List.of(
            new LocalButton("КН", "КН"),
            new LocalButton("МО", "МО"),
            new LocalButton("МХ", "МХ"),
            new LocalButton("МТ", "МТ"),
            new LocalButton("ПМ", "ПМ"),
            new LocalButton("КБ", "КБ"),
            new LocalButton("ФТ", "ФТ"),
            backButton
    );
    private final LocalMessage askLaterYearSpecialty = new LocalMessageBuilder()
            .text("""
                    На каком направлении?
                    Если Вы не видите свое направление, то, возможно, Вы выбрали не тот курс.""")
            .buttons(askLaterYearSpecialtyButtons)
            .build();

    private final LocalMessage askGroupNumber = new LocalMessageBuilder()
            .text("Какая у Вас группа?")
            .buttons(List.of(
                    new LocalButton("1 группа", "1"),
                    new LocalButton("2 группа", "2"),
                    new LocalButton("3 группа", "3"),
                    new LocalButton("4 группа", "4"),
                    new LocalButton("5 группа", "5"),
                    backButton
            ))
            .build();

    private final LocalMessage askMen = new LocalMessageBuilder()
            .text("Введите свою академическую группу в формате:\nМЕН-123456")
            .buttons(List.of(backButton))
            .build();

    private TestUtils utils;
    private DummyBot bot;

    /**
     * <p>Создаём объект логики, ложного бота и утилиты для каждого теста.</p>
     */
    @BeforeEach
    void setupTest() {
        final MMBCore logic = new MMBCore(new MathMechArrayStorage());
        bot = new DummyBot();
        utils = new TestUtils(logic, bot);
    }

    /**
     * <p>Тестируем случай, где пользователь-первокурсник последовательно
     * вводит корректные данные, а в конце подтверждает регистрацию.</p>
     */
    @Test
    @DisplayName("Тест всей регистрации первокурсника с подтверждением в конце.")
    void wholeRegistrationFirstYearAcceptTest() {
        utils.sendMessageToLogic(new LocalMessage(REGISTER_COMMAND));
        Assertions.assertEquals(askFullName, bot.getOutcomingMessageList().getFirst());

        utils.sendMessageToLogic(new LocalMessage("Дим-Димыч  Дима Артёмович"));
        Assertions.assertEquals(askYear, bot.getOutcomingMessageList().get(1));

        utils.sendMessageToLogic(new LocalMessage("1"));
        Assertions.assertEquals(askFirstYearSpecialty, bot.getOutcomingMessageList().get(2));

        utils.sendMessageToLogic(new LocalMessage("КНМО"));
        Assertions.assertEquals(askGroupNumber, bot.getOutcomingMessageList().get(3));

        utils.sendMessageToLogic(new LocalMessage("3"));
        Assertions.assertEquals(askMen, bot.getOutcomingMessageList().get(4));

        utils.sendMessageToLogic(new LocalMessage("МЕН-123456 "));
        Assertions.assertEquals(new LocalMessageBuilder()
                        .text("""
                                Всё верно?

                                ФИО: Дим-Димыч Дима Артёмович
                                Группа: КНМО-103 (МЕН-123456)""")
                        .buttons(yesNoBack)
                        .build(),
                bot.getOutcomingMessageList().get(5));

        utils.sendMessageToLogic(new LocalMessage(ACCEPT_COMMAND));
        utils.sendMessageToLogic(new LocalMessage(INFO_COMMAND));

        // Пропускаю 6-е и 7-е сообщение, так как они не содержат ничего примечательного.

        Assertions.assertEquals(new LocalMessage("""
                        Данные о Вас:

                        ФИО: Дим-Димыч Дима Артёмович
                        Группа: КНМО-103 (МЕН-123456)"""),
                bot.getOutcomingMessageList().get(8));
    }

    /**
     * <p>Тестируем случай, где пользователь-третьекурсник последовательно
     * вводит корректные данные, в конце подтверждает регистрацию.</p>
     */
    @Test
    @DisplayName("Тест всей регистрации старшекурсника с подтверждением в конце.")
    void wholeRegistrationLaterYearAcceptTest() {
        utils.sendMessageToLogic(new LocalMessage(REGISTER_COMMAND));
        utils.sendMessageToLogic(new LocalMessage("Артёмов  Артём  Артёмович "));
        utils.sendMessageToLogic(new LocalMessage("3"));

        Assertions.assertEquals(askLaterYearSpecialty,
                bot.getOutcomingMessageList().get(2));

        utils.sendMessageToLogic(new LocalMessage("МО"));
        utils.sendMessageToLogic(new LocalMessage("1"));
        utils.sendMessageToLogic(new LocalMessage(" МЕН-999999"));

        Assertions.assertEquals(new LocalMessageBuilder()
                        .text("""
                                Всё верно?

                                ФИО: Артёмов Артём Артёмович
                                Группа: МО-301 (МЕН-999999)""")
                        .buttons(yesNoBack)
                        .build(),
                bot.getOutcomingMessageList().get(5));

        utils.sendMessageToLogic(new LocalMessage(ACCEPT_COMMAND));
        utils.sendMessageToLogic(new LocalMessage(INFO_COMMAND));

        Assertions.assertEquals(new LocalMessage("""
                        Данные о Вас:

                        ФИО: Артёмов Артём Артёмович
                        Группа: МО-301 (МЕН-999999)"""),
                bot.getOutcomingMessageList().get(8));
    }

    /**
     * <p>Тестируем случай, где пользователь-второкурсник последовательно
     * вводит корректные данные, но затем начинает нажимать кнопку "Назад"
     * до выхода из регистрации. В конце проверяем, что на команду /info
     * пользователь получает просьбу о регистрации (то есть никакие данные
     * сохранены не были).</p>
     */
    @Test
    @DisplayName("Тест всей регистрации, а затем пошаговый возврат.")
    void wholeRegistrationThenAlwaysBackTest() {
        utils.sendMessageToLogic(new LocalMessage(REGISTER_COMMAND));
        utils.sendMessageToLogic(new LocalMessage("Николаев Николай Кун"));
        utils.sendMessageToLogic(new LocalMessage("2"));
        utils.sendMessageToLogic(new LocalMessage("ФТ"));
        utils.sendMessageToLogic(new LocalMessage("5"));
        utils.sendMessageToLogic(new LocalMessage("МЕН-623754"));

        Assertions.assertEquals(new LocalMessageBuilder()
                        .text("""
                                Всё верно?

                                ФИО: Николаев Николай Кун
                                Группа: ФТ-205 (МЕН-623754)""")
                        .buttons(yesNoBack)
                        .build(),
                bot.getOutcomingMessageList().get(5));

        utils.sendMessageToLogic(new LocalMessage(BACK_COMMAND));
        Assertions.assertEquals(askMen, bot.getOutcomingMessageList().get(6));

        utils.sendMessageToLogic(new LocalMessage(BACK_COMMAND));
        Assertions.assertEquals(askGroupNumber, bot.getOutcomingMessageList().get(7));

        utils.sendMessageToLogic(new LocalMessage(BACK_COMMAND));
        Assertions.assertEquals(askLaterYearSpecialty,
                bot.getOutcomingMessageList().get(8));

        utils.sendMessageToLogic(new LocalMessage(BACK_COMMAND));
        Assertions.assertEquals(askYear, bot.getOutcomingMessageList().get(9));

        utils.sendMessageToLogic(new LocalMessage(BACK_COMMAND));
        Assertions.assertEquals(askFullName, bot.getOutcomingMessageList().get(10));

        utils.sendMessageToLogic(new LocalMessage(BACK_COMMAND));
        utils.sendMessageToLogic(new LocalMessage(INFO_COMMAND));
        Assertions.assertEquals(askForRegistration,
                bot.getOutcomingMessageList().get(12));
    }
}

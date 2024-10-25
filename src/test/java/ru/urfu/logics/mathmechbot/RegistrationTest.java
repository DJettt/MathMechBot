package ru.urfu.logics.mathmechbot;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.DummyBot;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;

/**
 * <p>Тесты регистрации.</p>
 *
 * <p>@SuppressWarnings("MagicNumber") -- потому что индексы.</p>
 * <p>@SuppressWarnings("MagicNumber") -- чтобы не жаловался на текст кнопок.</p>
 */
@DisplayName("Регистрация")
@SuppressWarnings({"MagicNumber", "MultipleStringLiterals"})
public final class RegistrationTest {
    private final static Long TEST_ID = 0L;
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
            .buttons(List.of(
                    new LocalButton("Отменить регистрацию", BACK_COMMAND)))
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
            .text("На каком направлении?")
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
            .text("На каком направлении?")
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

    private MathMechBotCore logic;
    private DummyBot bot;

    /**
     * <p>Создаём объект логики, ложного бота и утилиты для каждого теста.</p>
     */
    @BeforeEach
    void setupTest() {
        logic = new MathMechBotCore(new MathMechStorage());
        bot = new DummyBot();
    }

    /**
     * <p>Тестируем случай, где пользователь-первокурсник последовательно
     * вводит корректные данные, а в конце подтверждает регистрацию.</p>
     */
    @Test
    @DisplayName("Первокурсник с подтверждением в конце")
    void firstYearAcceptTest() {
        logic.processMessage(TEST_ID, new LocalMessage(REGISTER_COMMAND), bot);
        Assertions.assertEquals(askFullName, bot.getOutcomingMessageList().getFirst());

        logic.processMessage(TEST_ID, new LocalMessage("Дим-Димыч  Дима Артёмович"), bot);
        Assertions.assertEquals(askYear, bot.getOutcomingMessageList().get(1));

        logic.processMessage(TEST_ID, new LocalMessage("1"), bot);
        Assertions.assertEquals(askFirstYearSpecialty, bot.getOutcomingMessageList().get(2));

        logic.processMessage(TEST_ID, new LocalMessage("КНМО"), bot);
        Assertions.assertEquals(askGroupNumber, bot.getOutcomingMessageList().get(3));

        logic.processMessage(TEST_ID, new LocalMessage("3"), bot);
        Assertions.assertEquals(askMen, bot.getOutcomingMessageList().get(4));

        logic.processMessage(TEST_ID, new LocalMessage("МЕН-123456 "), bot);
        Assertions.assertEquals(new LocalMessageBuilder()
                        .text("""
                                Всё верно?

                                ФИО: Дим-Димыч Дима Артёмович
                                Группа: КНМО-103 (МЕН-123456)""")
                        .buttons(yesNoBack)
                        .build(),
                bot.getOutcomingMessageList().get(5));

        logic.processMessage(TEST_ID, new LocalMessage(ACCEPT_COMMAND), bot);
        logic.processMessage(TEST_ID, new LocalMessage(INFO_COMMAND), bot);

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
    @DisplayName("Старшекурсник с подтверждением в конце")
    void laterYearAcceptTest() {
        logic.processMessage(TEST_ID, new LocalMessage(REGISTER_COMMAND), bot);
        logic.processMessage(TEST_ID, new LocalMessage("Артёмов  Артём  Артёмович "), bot);
        logic.processMessage(TEST_ID, new LocalMessage("3"), bot);

        Assertions.assertEquals(askLaterYearSpecialty,
                bot.getOutcomingMessageList().get(2));

        logic.processMessage(TEST_ID, new LocalMessage("МО"), bot);
        logic.processMessage(TEST_ID, new LocalMessage("1"), bot);

        logic.processMessage(TEST_ID, new LocalMessage(" МЕН-999999"), bot);
        Assertions.assertEquals(new LocalMessageBuilder()
                        .text("""
                                Всё верно?

                                ФИО: Артёмов Артём Артёмович
                                Группа: МО-301 (МЕН-999999)""")
                        .buttons(yesNoBack)
                        .build(),
                bot.getOutcomingMessageList().get(5));

        logic.processMessage(TEST_ID, new LocalMessage(ACCEPT_COMMAND), bot);
        logic.processMessage(TEST_ID, new LocalMessage(INFO_COMMAND), bot);

        Assertions.assertEquals(new LocalMessage("""
                        Данные о Вас:

                        ФИО: Артёмов Артём Артёмович
                        Группа: МО-301 (МЕН-999999)"""),
                bot.getOutcomingMessageList().get(8));
    }

    /**
     * <p>Тестируем случай, где пользователь-четверокурсник последовательно
     * вводит корректные данные, но в конце отказывается от регистрации.
     * В конце проверяем, что на команду /info пользователь получает просьбу
     * о регистрации (то есть никакие данные сохранены не были).</p>
     */
    @Test
    @DisplayName("Отказ в конце")
    void declineTest() {
        logic.processMessage(TEST_ID, new LocalMessage(REGISTER_COMMAND), bot);
        logic.processMessage(TEST_ID, new LocalMessage("Джун Даянч Даянчевич"), bot);
        logic.processMessage(TEST_ID, new LocalMessage("4"), bot);
        logic.processMessage(TEST_ID, new LocalMessage("КБ"), bot);
        logic.processMessage(TEST_ID, new LocalMessage("4"), bot);
        logic.processMessage(TEST_ID, new LocalMessage("МЕН-240104"), bot);

        Assertions.assertEquals(new LocalMessageBuilder()
                        .text("""
                                Всё верно?

                                ФИО: Джун Даянч Даянчевич
                                Группа: КБ-404 (МЕН-240104)""")
                        .buttons(yesNoBack)
                        .build(),
                bot.getOutcomingMessageList().get(5));

        logic.processMessage(TEST_ID, new LocalMessage(DECLINE_COMMAND), bot);
        logic.processMessage(TEST_ID, new LocalMessage(INFO_COMMAND), bot);
        Assertions.assertEquals(askForRegistration,
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
    @DisplayName("Пошаговый возврат до выхода")
    void backTest() {
            logic.processMessage(TEST_ID, new LocalMessage(REGISTER_COMMAND), bot);
            logic.processMessage(TEST_ID, new LocalMessage("Николаев Николай Кун"), bot);
            logic.processMessage(TEST_ID, new LocalMessage("2"), bot);
            logic.processMessage(TEST_ID, new LocalMessage("ФТ"), bot);
            logic.processMessage(TEST_ID, new LocalMessage("5"), bot);
            logic.processMessage(TEST_ID, new LocalMessage("МЕН-623754"), bot);

        Assertions.assertEquals(new LocalMessageBuilder()
                        .text("""
                                Всё верно?

                                ФИО: Николаев Николай Кун
                                Группа: ФТ-205 (МЕН-623754)""")
                        .buttons(yesNoBack)
                        .build(),
                bot.getOutcomingMessageList().get(5));

        logic.processMessage(TEST_ID, new LocalMessage(BACK_COMMAND), bot);
        Assertions.assertEquals(askMen, bot.getOutcomingMessageList().get(6));

        logic.processMessage(TEST_ID, new LocalMessage(BACK_COMMAND), bot);
        Assertions.assertEquals(askGroupNumber, bot.getOutcomingMessageList().get(7));

        logic.processMessage(TEST_ID, new LocalMessage(BACK_COMMAND), bot);
        Assertions.assertEquals(askLaterYearSpecialty,
                bot.getOutcomingMessageList().get(8));

        logic.processMessage(TEST_ID, new LocalMessage(BACK_COMMAND), bot);
        Assertions.assertEquals(askYear, bot.getOutcomingMessageList().get(9));

        logic.processMessage(TEST_ID, new LocalMessage(BACK_COMMAND), bot);
        Assertions.assertEquals(askFullName, bot.getOutcomingMessageList().get(10));

        logic.processMessage(TEST_ID, new LocalMessage(BACK_COMMAND), bot);

        logic.processMessage(TEST_ID, new LocalMessage(INFO_COMMAND), bot);
        Assertions.assertEquals(askForRegistration,
                bot.getOutcomingMessageList().get(12));
    }
}

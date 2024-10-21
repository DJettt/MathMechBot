package ru.urfu.logics.mathmechbot;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.DummyBot;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;

/**
 * <p>Тесты удаления данных.</p>
 *
 * <p>@SuppressWarnings("MagicNumber"), потому
 * что это не магические цифры, а индексы.</p>
 */
@DisplayName("Удаление данных")
@SuppressWarnings("MagicNumber")
final class DeletionTest {
    private final static String ACCEPT_COMMAND = "/yes";
    private final static String DECLINE_COMMAND = "/no";
    private final static String BACK_COMMAND = "/back";
    private final static String INFO_COMMAND = "/info";

    private final LocalButton backButton = new LocalButton("Назад", BACK_COMMAND);
    private final List<LocalButton> yesNoBack = List.of(
            new LocalButton("Да", ACCEPT_COMMAND),
            new LocalButton("Нет", DECLINE_COMMAND),
            backButton
    );


    private final LocalMessage askForRegistration = new LocalMessage("Сперва нужно зарегистрироваться.");
    private final LocalMessage tryAgain = new LocalMessage("Попробуйте снова.");

    private final LocalMessage askConfirmation = new LocalMessageBuilder()
            .text("""
                    Точно удаляем?

                    ФИО: Иванов Иван Иванович
                    Группа: ММП-102 (МЕН-123456)""")
            .buttons(yesNoBack)
            .build();
    private final LocalMessage userInfo = new LocalMessage("""
            Данные о Вас:

            ФИО: Иванов Иван Иванович
            Группа: ММП-102 (МЕН-123456)""");

    private TestUtils utils;
    private MathMechBotCore logic;
    private DummyBot bot;

    /**
     * Создаём объект логики и ложного бота для каждого теста, регистрируем человека и отправляем /delete.
     */
    @BeforeEach
    void setupTest() {
        logic = new MathMechBotCore(new MathMechStorage());
        bot = new DummyBot();
        utils = new TestUtils(logic, bot);

        utils.registerUser(0L, "Иванов Иван Иванович", 1, "ММП", 2, "МЕН-123456");
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage("/delete")));
    }

    /**
     * <p>Проверяем, что у зарегистрированного пользователя при просьбе
     * и подтверждении удалить данные, данные удаляются.</p>
     *
     * <ol>
     *     <li>Регистрируемся.</li>
     *     <li>Запускаем команду <code>/delete</code>.</li>
     *     <li>Проверяем, что бот отправил запрос на подтверждение.</li>
     *     <li>Нажимаем кнопку "Да".</li>
     *     <li>Проверяем, что бот подтвердил удаление данных.</li>
     *     <li>Проверяем, что пользователь вернулся в дефолтное состояние.</li>
     *     <li>Проверяем, что данные действительно удалились.</li>
     *     <li>Проверяем, что пользователю отправилась справка.</li>
     * </ol>
     */
    @Test
    @DisplayName("Кнопка 'Да'")
    void testRegisteredUserSaysYes() {
        Assertions.assertEquals(askConfirmation, bot.getOutcomingMessageList().getFirst());

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(ACCEPT_COMMAND)));
        Assertions.assertEquals(new LocalMessage("Удаляем..."), bot.getOutcomingMessageList().get(1));

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(INFO_COMMAND)));
        Assertions.assertEquals(askForRegistration, bot.getOutcomingMessageList().get(3));
    }

    /**
     * <p>Проверяем случай, когда зарегистрированный пользователь
     * запускает команду /delete, но не подтверждает удаление.</p>
     *
     * <ol>
     *     <li>Регистрируемся.</li>
     *     <li>Запускаем команду <code>/delete</code>.</li>
     *     <li>Нажимаем кнопку "Нет".</li>
     *     <li>Проверяем, что бот подтвердил не удаление данных.</li>
     *     <li>Проверяем, что пользователь вернулся в дефолтное состояние.</li>
     *     <li>Проверяем, что данные не удалились.</li>
     *     <li>Проверяем, что пользователю отправилась справка.</li>
     * </ol>
     */
    @Test
    @DisplayName("Кнопка 'Нет'")
    void testRegisteredUserSaysNo() {
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(DECLINE_COMMAND)));
        Assertions.assertEquals(new LocalMessage("Отмена..."), bot.getOutcomingMessageList().get(1));

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(INFO_COMMAND)));
        Assertions.assertEquals(userInfo, bot.getOutcomingMessageList().get(3));
    }

    /**
     * <p>Проверяем случай, когда зарегистрированный пользователь
     * запускает команду /delete, а затем нажимает кнопку "Назад".</p>
     *
     * <ol>
     *     <li>Регистрируемся.</li>
     *     <li>Запускаем команду <code>/delete</code>.</li>
     *     <li>Нажимаем кнопку "Назад".</li>
     *     <li>Проверяем, что бот вернулся в основное состояние.</li>
     *     <li>Проверяем, что данные не удалились.</li>
     *     <li>Проверяем, что пользователю отправилась справка.</li>
     * </ol>
     */
    @Test
    @DisplayName("Кнопка 'Назад'")
    void testRegisteredUserSaysBack() {
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(BACK_COMMAND)));
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(INFO_COMMAND)));
        Assertions.assertEquals(userInfo, bot.getOutcomingMessageList().get(2));
    }

    /**
     * <p>Проверяем случай, когда зарегистрированный пользователь
     * запускает команду /delete, но на запрос о подтверждении
     * удаления пользователь отправляет непредвиденный текст.</p>
     *
     * <ol>
     *     <li>Регистрируемся.</li>
     *     <li>Запускаем команду <code>/delete</code>.</li>
     *     <li>Нажимаем кнопку отправляем сообщение, не содержащее ожидаемые ответы.</li>
     *     <li>Проверяем, что состояние пользователя не изменилось.</li>
     *     <li>Проверяем, что данные не удалились.</li>
     *     <li>Проверяем, что бот запросил повторный ввод.</li>
     * </ol>
     *
     * @param text неожиданные данные
     */
    @DisplayName("Не кнопка")
    @NullAndEmptySource
    @ValueSource(strings = {"SomeString"})
    @ParameterizedTest(name = "\"{0}\" - не корректный ввод")
    void testRegisteredUserSaysSomethingElse(String text) {
        // Используем билдер, чтобы иметь возможность передать null.
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder().text(text).build()));
        Assertions.assertEquals(tryAgain, bot.getOutcomingMessageList().get(1));
        Assertions.assertEquals(askConfirmation, bot.getOutcomingMessageList().get(2));

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(DECLINE_COMMAND)));
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(INFO_COMMAND)));
        Assertions.assertEquals(userInfo, bot.getOutcomingMessageList().get(5));
    }
}

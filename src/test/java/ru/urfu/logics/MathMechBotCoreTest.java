package ru.urfu.logics;

import org.junit.jupiter.api.*;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.MathMechBotCore;

import java.util.ArrayList;
import java.util.List;


/**
 * Тесты для класса EchoBotCore
 */
public final class MathMechBotCoreTest {
    private final static String ACCEPT_COMMAND = "/yes";
    private final static String DECLINE_COMMAND = "/no";
    private final static String BACK_COMMAND = "/back";
    private final static LocalMessage ACCEPT_MESSAGE = new LocalMessageBuilder().text(ACCEPT_COMMAND).build();
    private final static LocalMessage DECLINE_MESSAGE = new LocalMessageBuilder().text(DECLINE_COMMAND).build();
    private final static LocalMessage BACK_MESSAGE = new LocalMessageBuilder().text(BACK_COMMAND).build();
    private final static LocalMessage INFO_MESSAGE = new LocalMessageBuilder().text("/info").build();
    private final static LocalMessage DELETE_MESSAGE = new LocalMessageBuilder().text("/delete").build();
    private final static LocalMessage ASK_FOR_REGISTRATION_MESSAGE = new LocalMessageBuilder()
            .text("Сперва нужно зарегистрироваться.")
            .build();
    private final static LocalMessage TRY_AGAIN = new LocalMessageBuilder()
            .text("Попробуйте снова.")
            .build();
    private final static LocalMessage HELP = new LocalMessageBuilder()
            .text("""
                    /start - начало общения с ботом
                    /help - выводит команды, которые принимает бот
                    /register - регистрация
                    /info - информация о Вас
                    /delete - удалить информацию о Вас""")
            .build();
    private final static List<LocalButton> YES_NO_BACK = new ArrayList<>(List.of(
            new LocalButton("Да", ACCEPT_COMMAND),
            new LocalButton("Неа", DECLINE_COMMAND),
            new LocalButton("Назад", BACK_COMMAND)
    ));

    private DummyBot bot;
    private MathMechBotCore logic;

    /**
     * Создаём объект логики и ложного бота для каждого теста.
     */
    @BeforeEach
    public void setupTest() {
        bot = new DummyBot();
        logic = new MathMechBotCore();
    }

    /**
     * Регистрирует человека со следующими данными
     *
     * @param fullName  ФИО или ФИ
     * @param year      год обучения.
     * @param specialty аббревиатура направления.
     * @param group     номер группы.
     * @param men       группа в формате МЕН.
     * @return результат команды <code>/info</code> для него.
     */
    private LocalMessage registerUser(String fullName, int year, String specialty, int group, String men) {
        final ArrayList<LocalMessage> messages = new ArrayList<>(List.of(
                new LocalMessageBuilder().text("/register").build(),
                new LocalMessageBuilder().text(fullName).build(),
                new LocalMessageBuilder().text(String.valueOf(year)).build(),
                new LocalMessageBuilder().text(specialty).build(),
                new LocalMessageBuilder().text(String.valueOf(group)).build(),
                new LocalMessageBuilder().text(men).build(),
                ACCEPT_MESSAGE,
                INFO_MESSAGE
        ));

        for (final LocalMessage message : messages) {
            logic.processMessage(message, 0L, bot);
        }
        return bot.getOutcomingMessageList().getLast();
    }

    /**
     * Тесты для команды удаления.
     */
    @Nested
    class DeleteCommandTest {
        /**
         * Проверяем, что на попытку незарегистрированного пользователя
         * удалить свои данные, ядро отвечает просьбой о регистрации.
         */
        @Test
        @DisplayName("Незарегистрированный пользователь пытается удалить свои данные")
        void testUnregisteredUser() {
            logic.processMessage(DELETE_MESSAGE, 0L, bot);
            Assertions.assertEquals(ASK_FOR_REGISTRATION_MESSAGE, bot.getOutcomingMessageList().getLast());
        }


        /**
         * Проверяем, что у зарегистрированного пользователя при просьбе
         * и подтверждении удалить данные, данные удаляются.
         * <ol>
         *     <li>Регистрируемся.</li>
         *     <li>Запускаем команду <code>/delete</code>.</li>
         *     <li>Проверяем, что бот отправил запрос на подтверждение.</li>
         *     <li>Нажимаем кнопку "Да".</li>
         *     <li>Проверям, что бот подтвердил удаление данных.</li>
         * </ol>
         */
        @Test
        @DisplayName("Подтверждаем удаление данных")
        void testRegisteredUserSaysYes() {
            registerUser("Иванов Иван Иванович", 1, "ММП", 2, "МЕН-123456");

            logic.processMessage(DELETE_MESSAGE, 0L, bot);
            Assertions.assertEquals(
                    new LocalMessageBuilder().text("""
                                    Точно удаляем?\n
                                    ФИО: Иванов Иван Иванович
                                    Группа: ММП-102 (МЕН-123456)""")
                            .buttons(YES_NO_BACK)
                            .build(),
                    bot.getOutcomingMessageList().getLast());

            bot.getOutcomingMessageList().clear();

            logic.processMessage(ACCEPT_MESSAGE, 0L, bot);
            Assertions.assertEquals(
                    new LocalMessageBuilder().text("Удаляем...").build(),
                    bot.getOutcomingMessageList().getFirst());
            Assertions.assertEquals(HELP, bot.getOutcomingMessageList().getLast());

            logic.processMessage(INFO_MESSAGE, 0L, bot);
            Assertions.assertEquals(ASK_FOR_REGISTRATION_MESSAGE, bot.getOutcomingMessageList().getLast());
        }

        /**
         * Проверяем случай, когда зарегистрированный пользователь
         * запускает команду /delete, но не подтверждает удаление.
         * <ol>
         *     <li>Регистрируемся.</li>
         *     <li>Запускаем команду <code>/delete</code>.</li>
         *     <li>Нажимаем кнопку "Неа".</li>
         *     <li>Проверям, что бот подтвердил не удаление данных.</li>
         *     <li>Проверяем, что данные не удалились.</li>
         * </ol>
         */
        @Test
        @DisplayName("Не подтверждаем удаление данных")
        void testRegisteredUserSaysNo() {
            final LocalMessage userInfo = registerUser(
                    "Иванов Иван Иванович", 1, "ММП", 2, "МЕН-123456");

            logic.processMessage(
                    new LocalMessageBuilder().text("/register").build(),
                    0L, bot);
            logic.processMessage(DELETE_MESSAGE, 0L, bot);
            bot.getOutcomingMessageList().clear();

            logic.processMessage(DECLINE_MESSAGE, 0L, bot);
            Assertions.assertEquals(
                    new LocalMessageBuilder().text("Отмена...").build(),
                    bot.getOutcomingMessageList().getFirst());
            Assertions.assertEquals(HELP, bot.getOutcomingMessageList().getLast());

            logic.processMessage(INFO_MESSAGE, 0L, bot);
            Assertions.assertEquals(userInfo, bot.getOutcomingMessageList().getLast());
        }

        /**
         * Проверяем случай, когда зарегистрированный пользователь
         * запускает команду /delete, а затем нажимает кнопку "Назад".
         * <ol>
         *     <li>Регистрируемся.</li>
         *     <li>Запускаем команду <code>/delete</code>.</li>
         *     <li>Нажимаем кнопку "Назад".</li>
         *     <li>Проверям, что бот вернулся в основное состояние.</li>
         *     <li>Проверяем, что данные не удалились.</li>
         * </ol>
         */
        @Test
        @DisplayName("Нажата кнопка 'Назад' во время подтверждения удаления данных")
        void testRegisteredUserSaysBack() {
            final LocalMessage userInfo = registerUser(
                    "Иванов Иван Иванович", 1, "ММП", 2, "МЕН-123456");

            logic.processMessage(
                    new LocalMessageBuilder().text("/register").build(),
                    0L, bot);
            logic.processMessage(DELETE_MESSAGE, 0L, bot);
            bot.getOutcomingMessageList().clear();

            logic.processMessage(BACK_MESSAGE, 0L, bot);
            Assertions.assertEquals(HELP, bot.getOutcomingMessageList().getLast());

            logic.processMessage(INFO_MESSAGE, 0L, bot);
            Assertions.assertEquals(userInfo, bot.getOutcomingMessageList().getLast());
        }

        /**
         * Проверяем случай, когда зарегистрированный пользователь
         * запускает команду /delete, но на запрос о подтверждении
         * удаления пользователь отправляет непредвиденный текст.
         * <ol>
         *     <li>Регистрируемся.</li>
         *     <li>Запускаем команду <code>/delete</code>.</li>
         *     <li>Отправляем сообщение с произвольным текстом "SomethingElse".</li>
         *     <li>Проверям, что бот переспросил.</li>
         * </ol>
         */
        @Test
        @DisplayName("Вместо подтверждения пришёл неожиданный текст")
        void testRegisteredUserSaysSomethingElse() {
            registerUser("Иванов Иван Иванович", 1, "ММП", 2, "МЕН-123456");

            logic.processMessage(DELETE_MESSAGE, 0L, bot);
            logic.processMessage(
                    new LocalMessageBuilder().text("SomethingElse").build(),
                    0L, bot);
            Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().getLast());
        }
    }
}

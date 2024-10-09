package ru.urfu.logics;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.MathMechBotCore;

import java.util.ArrayList;
import java.util.List;


/**
 * Тесты для класса MathMechBotCore
 */
public final class MathMechBotCoreTest {
    private final static String ACCEPT_COMMAND = "/yes";
    private final static String DECLINE_COMMAND = "/no";
    private final static String BACK_COMMAND = "/back";
    private final static LocalButton BACK_BUTTON = new LocalButton("Назад", BACK_COMMAND);
    private final static LocalMessage ACCEPT_MESSAGE = new LocalMessageBuilder().text(ACCEPT_COMMAND).build();
    private final static LocalMessage DECLINE_MESSAGE = new LocalMessageBuilder().text(DECLINE_COMMAND).build();
    private final static LocalMessage BACK_MESSAGE = new LocalMessageBuilder().text(BACK_COMMAND).build();
    private final static LocalMessage INFO_MESSAGE = new LocalMessageBuilder().text("/info").build();
    private final static LocalMessage DELETE_MESSAGE = new LocalMessageBuilder().text("/delete").build();
    private final static LocalMessage REGISTER_MESSAGE = new LocalMessageBuilder().text("/register").build();

    private final static LocalMessage ASK_FOR_REGISTRATION_MESSAGE = new LocalMessageBuilder()
            .text("Сперва нужно зарегистрироваться.").build();
    private final static LocalMessage TRY_AGAIN = new LocalMessageBuilder()
            .text("Попробуйте снова.").build();

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
            BACK_BUTTON
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
                REGISTER_MESSAGE,
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
     * Тесты для команды регистрации
     */
    @Nested
    @DisplayName("Тестирование команды /register и её состояний")
    class RegisterCommandTest {
        private final static LocalMessage ASK_FULL_NAME = new LocalMessageBuilder()
                .text("""
                        Введите свое ФИО в формате:
                        Иванов Артём Иванович
                        Без дополнительных пробелов и с буквой ё, если нужно.""")
                .build();
        private final static LocalMessage ASK_YEAR = new LocalMessageBuilder()
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
         * Тесты для команды регистрации в состоянии запроса ФИО
         */
        @Nested
        @DisplayName("Состояние: запрос ФИО")
        class FullNameState {
            /**
             * Проверяем, что бот принимает корректные ФИО или ФИ.
             * <ol>
             *     <li>Отправляем команду <code>/register</code>.</li>
             *     <li>Проверяем, что бот спросил ФИО.</li>
             *     <li>Отправляем корректное ФИО или ФИ.</li>
             *     <li>Проверяем, что бот принял их.</li>
             * </ol>
             *
             * @param fullName корректные ФИО или ФИ.
             */
            @ParameterizedTest(name = "{0} - корректное ФИО")
            @ValueSource(strings = {"Иванов Иван", "Иванов Иван Иванович", "Ии Ии Ии", "Ии Ии"})
            @DisplayName("Различные корректные ФИО или ФИ")
            void testCorrectFullNames(String fullName) {
                logic.processMessage(REGISTER_MESSAGE, 0L, bot);
                Assertions.assertEquals(ASK_FULL_NAME, bot.getOutcomingMessageList().getLast());

                logic.processMessage(new LocalMessageBuilder().text(fullName).build(), 0L, bot);
                Assertions.assertEquals(ASK_YEAR, bot.getOutcomingMessageList().getLast());
            }

            /**
             * Проверяем, что бот не принимает некорректные ФИО или ФИ.
             * <ol>
             *     <li>Отправляем команду <code>/register</code>.</li>
             *     <li>Отправляем некорректное ФИО или ФИ.</li>
             *     <li>Проверяем, что бот запросил повторить ввод.</li>
             * </ol>
             *
             * @param fullName некорректные ФИО или ФИ.
             */
            @ParameterizedTest(name = "{0} - некорректное ФИО")
            @ValueSource(strings = {"Иванов", "Иванов И", "И Иванов", "Иванов Иванов И", "ИВанов Иванов"})
            @DisplayName("Различные некорректные ФИО или ФИ")
            void testIncorrectFullNames(String fullName) {
                logic.processMessage(REGISTER_MESSAGE, 0L, bot);
                logic.processMessage(new LocalMessageBuilder().text(fullName).build(), 0L, bot);
                Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().getLast());
            }

            /**
             * Проверяем, что бот корректно возвращает на шаг назад на этапе запроса ФИО.
             * <ol>
             *     <li>Отправляем команду <code>/register</code>.</li>
             *     <li>Нажимаем кнопку "Назад"</li>
             *     <li>Проверяем, что бот в вернулся в основное состояние (выдал справку).</li>
             *     <li>Проверяем, что пользователь всё так же не зарегистрирован.</li>
             * </ol>
             */
            @Test
            @DisplayName("Нажата кнопка 'Назад'")
            void testBackCommand() {
                logic.processMessage(REGISTER_MESSAGE, 0L, bot);
                logic.processMessage(BACK_MESSAGE, 0L, bot);
                Assertions.assertEquals(HELP, bot.getOutcomingMessageList().getLast());

                logic.processMessage(INFO_MESSAGE, 0L, bot);
                Assertions.assertEquals(ASK_FOR_REGISTRATION_MESSAGE, bot.getOutcomingMessageList().getLast());
            }
        }
    }

    /**
     * Тесты для команды удаления
     */
    @Nested
    @DisplayName("Тестирование команды /delete и её состояний")
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
        @DisplayName("Нажата кнопка 'Да' во время подтверждения удаления данных")
        void testRegisteredUserSaysYes() {
            registerUser("Иванов Иван Иванович", 1, "ММП", 2, "МЕН-123456");

            logic.processMessage(DELETE_MESSAGE, 0L, bot);
            Assertions.assertEquals(
                    new LocalMessageBuilder().text("""
                                    Точно удаляем?
                                                                        
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
        @DisplayName("Нажата кнопка 'Неа' во время подтверждения удаления данных")
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

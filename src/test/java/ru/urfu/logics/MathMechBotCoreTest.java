package ru.urfu.logics;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.MathMechBotCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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

        private final static List<LocalButton> ASK_FIRST_YEAR_SPECIALTY_BUTTONS = new ArrayList<>(
                List.of(
                        new LocalButton("КНМО", "КНМО"),
                        new LocalButton("ММП", "ММП"),
                        new LocalButton("КБ", "КБ"),
                        new LocalButton("ФТ", "ФТ"),
                        BACK_BUTTON
                ));
        private final static LocalMessage ASK_FIRST_YEAR_SPECIALTY = new LocalMessageBuilder()
                .text("На каком направлении?")
                .buttons(ASK_FIRST_YEAR_SPECIALTY_BUTTONS)
                .build();

        private final static List<LocalButton> ASK_LATER_YEAR_SPECIALTY_BUTTONS = new ArrayList<>(
                List.of(
                        new LocalButton("КН", "КН"),
                        new LocalButton("МО", "МО"),
                        new LocalButton("МХ", "МХ"),
                        new LocalButton("МТ", "МТ"),
                        new LocalButton("ПМ", "ПМ"),
                        new LocalButton("КБ", "КБ"),
                        new LocalButton("ФТ", "ФТ"),
                        BACK_BUTTON
                ));
        private final static LocalMessage ASK_LATER_YEAR_SPECIALTY = new LocalMessageBuilder()
                .text("На каком направлении?")
                .buttons(ASK_LATER_YEAR_SPECIALTY_BUTTONS)
                .build();

        private final static LocalMessage ASK_GROUP_NUMBER = new LocalMessageBuilder()
                .text("На каком курсе Вы обучаетесь?")
                .buttons(new ArrayList<>(List.of(
                        new LocalButton("1 группа", "1"),
                        new LocalButton("2 группа", "2"),
                        new LocalButton("3 группа", "3"),
                        new LocalButton("4 группа", "4"),
                        new LocalButton("5 группа", "5"),
                        BACK_BUTTON
                )))
                .build();

        /**
         * Тесты для команды регистрации в состоянии запроса ФИО.
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
            @ParameterizedTest(name = "\"{0}\" - корректное ФИО")
            @ValueSource(strings = {"Иванов Иван", "Иванов Иван Иванович", "Ии Ии Ии", "Ии Ии"})
            @DisplayName("Различные корректные ФИО или ФИ")
            void testCorrectData(String fullName) {
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
            @ParameterizedTest(name = "\"{0}\" - некорректное ФИО")
            @EmptySource
            @NullSource
            @ValueSource(strings = {"Иванов", "Иванов И", "И Иванов", "Иванов Иванов И", "ИВанов Иванов"})
            @DisplayName("Различные некорректные ФИО или ФИ")
            void testIncorrectData(String fullName) {
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

        /**
         * Тесты для команды регистрации в состоянии запроса года обучения.
         */
        @Nested
        @DisplayName("Состояние: запрос года обучения")
        class YearState {
            /**
             * До тестов запускаем команду <code>/register</code>
             * и отправляем корректное ФИО, так как это уже протестировано.
             * Все отправленные ботом сообщения стираем, так как они не интересны.
             */
            @BeforeEach
            void setupTest() {
                logic.processMessage(REGISTER_MESSAGE, 0L, bot);
                logic.processMessage(new LocalMessageBuilder().text("Иванов Иван Иванович").build(), 0L, bot);
                bot.getOutcomingMessageList().clear();
            }

            /**
             * Проверяем, что бот принимает годы обучения.
             * <ol>
             *     <li>Отправляем корректное корректный год обучения.</li>
             *     <li>Проверяем, что бот принял их.</li>
             * </ol>
             *
             * @param year корректный год обучения.
             */
            @ParameterizedTest(name = "\"{0}\" - корректный год обучения")
            @ValueSource(strings = {"1", "2", "3", "4", "5", "6"})
            @DisplayName("Все возможные корректные годы обучения")
            void testCorrectData(String year) {
                logic.processMessage(new LocalMessageBuilder().text(year).build(), 0L, bot);
                if (Objects.equals(year, "1")) {
                    Assertions.assertEquals(ASK_FIRST_YEAR_SPECIALTY, bot.getOutcomingMessageList().getLast());
                } else {
                    Assertions.assertEquals(ASK_LATER_YEAR_SPECIALTY, bot.getOutcomingMessageList().getLast());
                }
            }

            /**
             * Проверяем, что бот не принимает иных.
             * <ol>
             *     <li>Отправляем некорректные данные.</li>
             *     <li>Проверяем, что бот не принял их.</li>
             * </ol>
             *
             * @param text некорректное сообщение.
             */
            @ParameterizedTest(name = "\"{0}\" не является годом обучения")
            @EmptySource
            @NullSource
            @ValueSource(strings = {"0", "7", "10", "a", "((("})
            @DisplayName("Некорректный ввод")
            void testIncorrectData(String text) {
                logic.processMessage(new LocalMessageBuilder().text(text).build(), 0L, bot);
                Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
                Assertions.assertEquals(ASK_YEAR, bot.getOutcomingMessageList().getLast());
            }

            /**
             * Проверяем, что бот корректно возвращает на шаг назад, то есть в состояние запроса ФИО.
             * <ol>
             *     <li>Нажимаем кнопку "Назад"</li>
             *     <li>Проверяем, что бот в вернулся в состояние запроса ФИО (запросил ФИО снова).</li>
             *     <li>Проверяем, что пользователь всё так же не зарегистрирован.</li>
             * </ol>
             */
            @Test
            @DisplayName("Нажата кнопка 'Назад'")
            void testBackCommand() {
                logic.processMessage(BACK_MESSAGE, 0L, bot);
                Assertions.assertEquals(ASK_FULL_NAME, bot.getOutcomingMessageList().getLast());
            }
        }

        /**
         * Тесты для команды регистрации в состоянии запроса направления подготовки для первокурсников.
         */
        @Nested
        @DisplayName("Состояние: запрос направления подготовки")
        class Specialty {
            /**
             * До тестов регистрируем двух студентов, первокурсника и второкурсника.
             */
            @BeforeEach
            void setupTest() {
                logic.processMessage(REGISTER_MESSAGE, 0L, bot);
                logic.processMessage(new LocalMessageBuilder().text("Иванов Иван Иванович").build(), 0L, bot);
                logic.processMessage(new LocalMessageBuilder().text("1").build(), 0L, bot);

                logic.processMessage(REGISTER_MESSAGE, 1L, bot);
                logic.processMessage(new LocalMessageBuilder().text("Ильин Илья Ильич").build(), 1L, bot);
                logic.processMessage(new LocalMessageBuilder().text("2").build(), 1L, bot);

                bot.getOutcomingMessageList().clear();
            }

            /**
             * Проверяем, что бот принимает общие направления и для первокурсника, и для второкурсника.
             * <ol>
             *     <li>Отправляем от первокурсника корректную аббревиатуру направления подготовки.</li>
             *     <li>Проверяем, что бот принял их.</li>
             *     <li>Аналогично для второкурсника</li>
             * </ol>
             *
             * @param specialtyAbbreviation корректная аббревиатура направления подготовки, общего для всех курсов.
             */
            @ParameterizedTest(name = "\"{0}\" - корректное направление подготовки для обоих курсов")
            @ValueSource(strings = {"КБ", "ФТ"})
            @DisplayName("Все возможные корректные направления подготовки, общие для всех курсов")
            void testCommonData(String specialtyAbbreviation) {
                for (long id = 0; id < 2; ++id) {
                    logic.processMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), id, bot);
                    Assertions.assertEquals(ASK_GROUP_NUMBER, bot.getOutcomingMessageList().getFirst());
                    bot.getOutcomingMessageList().clear();
                }
            }

            /**
             * Проверяем, что бот принимает направления подготовки первого курса только у первокурсников.
             * <ol>
             *     <li>Отправляем корректную аббревиатуру направления подготовки первого курса.</li>
             *     <li>Проверяем, что бот принял её от первокурсника.</li>
             *     <li>Аналогично проверяем, что бот не принял её от второкурсника.</li>
             * </ol>
             *
             * @param specialtyAbbreviation корректная аббревиатура направления подготовки первого курса.
             */
            @ParameterizedTest(name = "\"{0}\" - корректное направление подготовки только для первого курса")
            @ValueSource(strings = {"КНМО", "ММП"})
            @DisplayName("Все возможные корректные направления подготовки первого курса")
            void testFirstYearOnlyData(String specialtyAbbreviation) {
                // Первокурсник
                logic.processMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 0L, bot);
                Assertions.assertEquals(ASK_GROUP_NUMBER, bot.getOutcomingMessageList().getFirst());
                bot.getOutcomingMessageList().clear();

                // Второкурсник
                logic.processMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 1L, bot);
                Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
                Assertions.assertEquals(ASK_LATER_YEAR_SPECIALTY, bot.getOutcomingMessageList().get(1));
            }

            /**
             * Проверяем, что бот принимает направления подготовки первого курса только у первокурсников.
             * <ol>
             *     <li>Отправляем корректную аббревиатуру направления подготовки первого курса.</li>
             *     <li>Проверяем, что бот не принял её от первокурсника.</li>
             *     <li>Аналогично проверяем, что бот принял её от второкурсника.</li>
             * </ol>
             *
             * @param specialtyAbbreviation корректная аббревиатура направления подготовки не первых курсов.
             */
            @ParameterizedTest(name = "\"{0}\" - корректное направление подготовки только для поздних курса")
            @ValueSource(strings = {"КН", "МО", "МХ", "ПМ"})
            @DisplayName("Все возможные корректные направления подготовки поздних курса")
            void testLaterYearsOnlyData(String specialtyAbbreviation) {
                // Первокурсник
                logic.processMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 0L, bot);
                Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
                Assertions.assertEquals(ASK_FIRST_YEAR_SPECIALTY, bot.getOutcomingMessageList().get(1));
                bot.getOutcomingMessageList().clear();

                // Второкурсник
                logic.processMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 1L, bot);
                Assertions.assertEquals(ASK_GROUP_NUMBER, bot.getOutcomingMessageList().getFirst());
            }

            /**
             * Проверяем, что бот принимает направления подготовки первого курса только у первокурсников.
             * <ol>
             *     <li>Отправляем корректную аббревиатуру направления подготовки первого курса.</li>
             *     <li>Проверяем, что бот не принял её от первокурсника.</li>
             *     <li>Аналогично проверяем, что бот принял её от второкурсника.</li>
             * </ol>
             *
             * @param specialtyAbbreviation корректная аббревиатура направления подготовки не первых курсов.
             */
            @ParameterizedTest(name = "\"{0}\" не является разрешённым направление подготовки")
            @EmptySource
            @NullSource
            @ValueSource(strings = {"кн", "string", "0"})
            @DisplayName("Некорректный ввод")
            void testIncorrectData(String specialtyAbbreviation) {
                // Первокурсник
                logic.processMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 0L, bot);
                Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
                Assertions.assertEquals(ASK_FIRST_YEAR_SPECIALTY, bot.getOutcomingMessageList().get(1));
                bot.getOutcomingMessageList().clear();

                // Второкурсник
                logic.processMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 1L, bot);
                Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
                Assertions.assertEquals(ASK_LATER_YEAR_SPECIALTY, bot.getOutcomingMessageList().get(1));
            }

            /**
             * Проверяем, что бот корректно возвращает на шаг назад, то есть в состояние запроса года обучения.
             * <ol>
             *     <li>Нажимаем кнопку "Назад"</li>
             *     <li>Проверяем, что бот в вернулся в состояние запроса года обучения (снова спросил его).</li>
             * </ol>
             */
            @Test
            @DisplayName("Нажата кнопка 'Назад'")
            void testBackCommand() {
                logic.processMessage(BACK_MESSAGE, 0L, bot);
                Assertions.assertEquals(ASK_YEAR, bot.getOutcomingMessageList().getFirst());
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

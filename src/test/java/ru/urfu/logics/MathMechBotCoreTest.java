package ru.urfu.logics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.models.User;
import ru.urfu.logics.mathmechbot.models.UserBuilder;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.models.UserEntryBuilder;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;
import ru.urfu.logics.mathmechbot.storages.UserArrayStorage;
import ru.urfu.logics.mathmechbot.storages.UserEntryArrayStorage;


/**
 * Тесты для класса MathMechBotCore
 */
final class MathMechBotCoreTest {
    final static String ACCEPT_COMMAND = "/yes";
    final static String DECLINE_COMMAND = "/no";
    final static String BACK_COMMAND = "/back";
    final static LocalButton BACK_BUTTON = new LocalButton("Назад", BACK_COMMAND);
    final static LocalMessage ACCEPT_MESSAGE = new LocalMessageBuilder().text(ACCEPT_COMMAND).build();
    final static LocalMessage DECLINE_MESSAGE = new LocalMessageBuilder().text(DECLINE_COMMAND).build();
    final static LocalMessage BACK_MESSAGE = new LocalMessageBuilder().text(BACK_COMMAND).build();
    final static LocalMessage INFO_MESSAGE = new LocalMessageBuilder().text("/info").build();
    final static LocalMessage DELETE_MESSAGE = new LocalMessageBuilder().text("/delete").build();
    final static LocalMessage REGISTER_MESSAGE = new LocalMessageBuilder().text("/register").build();

    final static LocalMessage ASK_FOR_REGISTRATION_MESSAGE = new LocalMessageBuilder()
            .text("Сперва нужно зарегистрироваться.").build();
    final static LocalMessage TRY_AGAIN = new LocalMessageBuilder()
            .text("Попробуйте снова.").build();

    final static LocalMessage HELP = new LocalMessageBuilder()
            .text("""
                    /start - начало общения с ботом
                    /help - выводит команды, которые принимает бот
                    /register - регистрация
                    /info - информация о Вас
                    /delete - удалить информацию о Вас""")
            .build();

    final static List<LocalButton> YES_NO_BACK = new ArrayList<>(List.of(
            new LocalButton("Да", ACCEPT_COMMAND),
            new LocalButton("Неа", DECLINE_COMMAND),
            BACK_BUTTON
    ));

    private MathMechStorage storage;
    private MathMechBotCore logic;
    private DummyBot bot;

    /**
     * Создаём объект логики и ложного бота для каждого теста.
     */
    @BeforeEach
    void setupTest() {
        storage = new MathMechStorage(new UserArrayStorage(), new UserEntryArrayStorage());
        logic = new MathMechBotCore(storage);
        bot = new DummyBot();
    }

    /**
     * Регистрирует человека со следующими данными.
     * Нужна для быстрых тестов команд, где требуется зарегистрированный пользователь.
     *
     * @param fullName  ФИО или ФИ
     * @param year      год обучения.
     * @param specialty аббревиатура направления.
     * @param group     номер группы.
     * @param men       группа в формате МЕН.
     */
    private void registerUser(long id, String fullName, int year, String specialty, int group, String men) {
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
            logic.processMessage(message, id, bot);
        }
        bot.getOutcomingMessageList().clear();
    }

    @Test
    @DisplayName("Тестирование команды /info")
    void testInfoExist() {
        logic.processMessage(INFO_MESSAGE, 0L, bot);
        Assertions.assertEquals(ASK_FOR_REGISTRATION_MESSAGE, bot.getOutcomingMessageList().getFirst());

        registerUser(0L, "Ильин Илья Ильич", 2, "КН", 3, "МЕН-654321");
        logic.processMessage(INFO_MESSAGE, 0L, bot);
        Assertions.assertEquals(new LocalMessageBuilder()
                        .text("""
                                Данные о Вас:

                                ФИО: Ильин Илья Ильич
                                Группа: КН-203 (МЕН-654321)""")
                        .build(),
                bot.getOutcomingMessageList().getFirst());
    }


    /**
     * Тесты для команды регистрации
     */
    @Nested
    @DisplayName("Тестирование команды /register и её состояний")
    class RegisterCommandTest {
        final static LocalMessage ASK_FULL_NAME = new LocalMessageBuilder()
                .text("""
                        Введите свое ФИО в формате:
                        Иванов Артём Иванович
                        Без дополнительных пробелов и с буквой ё, если нужно.""")
                .build();

        final static LocalMessage ASK_YEAR = new LocalMessageBuilder()
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

        final static List<LocalButton> ASK_FIRST_YEAR_SPECIALTY_BUTTONS = new ArrayList<>(
                List.of(
                        new LocalButton("КНМО", "КНМО"),
                        new LocalButton("ММП", "ММП"),
                        new LocalButton("КБ", "КБ"),
                        new LocalButton("ФТ", "ФТ"),
                        BACK_BUTTON
                ));
        final static LocalMessage ASK_FIRST_YEAR_SPECIALTY = new LocalMessageBuilder()
                .text("На каком направлении?")
                .buttons(ASK_FIRST_YEAR_SPECIALTY_BUTTONS)
                .build();

        final static List<LocalButton> ASK_LATER_YEAR_SPECIALTY_BUTTONS = new ArrayList<>(
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
        final static LocalMessage ASK_LATER_YEAR_SPECIALTY = new LocalMessageBuilder()
                .text("На каком направлении?")
                .buttons(ASK_LATER_YEAR_SPECIALTY_BUTTONS)
                .build();

        final static LocalMessage ASK_GROUP_NUMBER = new LocalMessageBuilder()
                .text("Какая у Вас группа?")
                .buttons(new ArrayList<>(List.of(
                        new LocalButton("1 группа", "1"),
                        new LocalButton("2 группа", "2"),
                        new LocalButton("3 группа", "3"),
                        new LocalButton("4 группа", "4"),
                        new LocalButton("5 группа", "5"),
                        BACK_BUTTON
                )))
                .build();

        final static LocalMessage ASK_MEN = new LocalMessageBuilder()
                .text("Введите свою академическую группу в формате:\nМЕН-123456")
                .buttons(new ArrayList<>(List.of(BACK_BUTTON)))
                .build();

        /**
         * Тесты для команды регистрации в состоянии запроса ФИО.
         */
        @Nested
        @DisplayName("Состояние: запрос ФИО")
        class FullNameState {
            /**
             * <p>Аргументы для testCorrectData.</p>
             *
             * @return аргументы.
             */
            static Stream<Arguments> testCorrectData() {
                return Stream.of(
                        Arguments.of("Иванов Иван", "Иванов", "Иван", null),
                        Arguments.of("   Иванов   Иван   ", "Иванов", "Иван", null),
                        Arguments.of("Артемов Артемий Артёмович", "Артемов", "Артемий", "Артёмович"),
                        Arguments.of("    Ильин   Илья     Ильич  ", "Ильин", "Илья", "Ильич"),
                        Arguments.of("Ии Ии Ии", "Ии", "Ии", "Ии"),
                        Arguments.of("Ии Ии", "Ии", "Ии", null)
                );
            }

            /**
             * <p>Проверяем, что бот принимает корректные ФИО или ФИ.</p>
             *
             * <ol>
             *     <li>Отправляем команду <code>/register</code>.</li>
             *     <li>Проверяем, что бот спросил ФИО.</li>
             *     <li>Отправляем корректное ФИО или ФИ.</li>
             *     <li>Проверяем, что бот сохранил данные.</li>
             *     <li>Проверяем, что состояние пользователя изменилось на запрос года обучения.</li>
             *     <li>Проверяем, что бот отравил запрос года обучения.</li>
             * </ol>
             *
             * @param incomingMessageText сообщение с корректным ФИО или ФИ.
             * @param surname             фамилия, содержащая в сообщении.
             * @param name                имя, содержащее в сообщении.
             * @param patronym            отчество, содержащее в сообщении.
             */
            @ParameterizedTest(name = "\"{0}\" - сообщение, содержащее корректное ФИО")
            @MethodSource
            @DisplayName("Различные корректные ФИО или ФИ")
            void testCorrectData(String incomingMessageText, String surname, String name, String patronym) {
                logic.processMessage(REGISTER_MESSAGE, 0L, bot);
                Assertions.assertEquals(ASK_FULL_NAME, bot.getOutcomingMessageList().getFirst());

                logic.processMessage(new LocalMessageBuilder().text(incomingMessageText).build(), 0L, bot);

                Assertions.assertEquals(
                        new UserEntryBuilder(0L, surname, name, 0L).patronym(patronym).build(),
                        storage.userEntries.get(0L).get()
                );
                Assertions.assertEquals(
                        new UserBuilder(0L, MathMechBotUserState.REGISTRATION_YEAR).build(),
                        storage.users.get(0L).get()
                );
                Assertions.assertEquals(ASK_YEAR, bot.getOutcomingMessageList().get(1));
            }

            /**
             * <p>Проверяем, что бот не принимает некорректные ФИО или ФИ.</p>
             *
             * <ol>
             *     <li>Отправляем команду <code>/register</code>.</li>
             *     <li>Отправляем некорректное ФИО или ФИ.</li>
             *     <li>Проверяем, что бот ничего не сохранил.</li>
             *     <li>Проверяем, что состояние пользователя не изменилось.</li>
             *     <li>Проверяем, что бот запросил повторить ввод.</li>
             * </ol>
             *
             * @param fullName некорректные ФИО или ФИ.
             */
            @ParameterizedTest(name = "\"{0}\" - сообщение, содержащее некорректное ФИО")
            @NullAndEmptySource
            @ValueSource(strings = {
                    "И", "И И", "И И", "Иванов", "Иванов И", "И Иванов",
                    "И Иванов Иванов", "Иванов Иванов И", "Иванов И Иванов",
                    "ИВанов Иванов", "Иванов ИВанов", "Иванов Иванов ИВанов",
            })
            @DisplayName("Различные некорректные ФИО или ФИ")
            void testIncorrectData(String fullName) {
                logic.processMessage(REGISTER_MESSAGE, 0L, bot);
                logic.processMessage(new LocalMessageBuilder().text(fullName).build(), 0L, bot);

                Assertions.assertTrue(storage.userEntries.get(0L).isEmpty());
                Assertions.assertEquals(
                        new UserBuilder(0L, MathMechBotUserState.REGISTRATION_NAME).build(),
                        storage.users.get(0L).get()
                );
                Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().getLast());
            }

            /**
             * <p>Проверяем, что бот корректно возвращает на шаг назад на этапе запроса ФИО.</p>
             *
             * <ol>
             *     <li>Отправляем команду <code>/register</code>.</li>
             *     <li>Нажимаем кнопку "Назад"</li>
             *     <li>Проверяем, что бот не сохранил никаких данных.</li>
             *     <li>Проверяем, что состояние пользователя изменилось на дефолтное.</li>
             *     <li>Проверяем, что бот отравил запрос года обучения.</li>
             * </ol>
             */
            @Test
            @DisplayName("Нажата кнопка 'Назад'")
            void testBackCommand() {
                logic.processMessage(REGISTER_MESSAGE, 0L, bot);
                logic.processMessage(BACK_MESSAGE, 0L, bot);

                Assertions.assertTrue(storage.userEntries.get(0L).isEmpty());
                Assertions.assertEquals(
                        new UserBuilder(0L, MathMechBotUserState.DEFAULT).build(),
                        storage.users.get(0L).get()
                );
                Assertions.assertEquals(HELP, bot.getOutcomingMessageList().get(1));
            }
        }

        /**
         * Тесты для команды регистрации в состоянии запроса года обучения.
         */
        @Nested
        @DisplayName("Состояние: запрос года обучения")
        class YearState {
            User currentUser;
            UserEntry currentUserEntry;

            /**
             * До тестов запускаем команду <code>/register</code>
             * и отправляем корректное ФИО, так как это уже протестировано.
             * Все отправленные ботом сообщения стираем, так как они не интересны.
             */
            @BeforeEach
            void setupTest() {
                logic.processMessage(REGISTER_MESSAGE, 0L, bot);
                logic.processMessage(new LocalMessageBuilder().text("Иванов Артём Иванович").build(), 0L, bot);

                currentUser = storage.users.get(0L).get();
                currentUserEntry = storage.userEntries.get(0L).get();

                bot.getOutcomingMessageList().clear();
            }

            /**
             * <p>Проверяем, что бот принимает годы обучения.</p>
             *
             * <ol>
             *     <li>Отправляем корректное корректный год обучения.</li>
             *     <li>Проверяем, что пользователь перешёл в нужное состояние:
             *          <ul>
             *              <li>Если год первый, то в состояние выбора направления первого курса.</li>
             *              <li>Если иной, то в состояние выбора направления старших курсов.</li>
             *          </ul>
             *     </li>
             *     <li>Проверяем, что бот отправил нужное сообщение.</li>
             *     <li>Проверяем, что пользовательские данные теперь содержат год обучения.</li>
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
                    Assertions.assertEquals(
                            new UserBuilder(0L, MathMechBotUserState.REGISTRATION_SPECIALTY1).build(),
                            storage.users.get(0L).get());
                    Assertions.assertEquals(ASK_FIRST_YEAR_SPECIALTY, bot.getOutcomingMessageList().getLast());
                } else {
                    Assertions.assertEquals(
                            new UserBuilder(0L, MathMechBotUserState.REGISTRATION_SPECIALTY2).build(),
                            storage.users.get(0L).get());
                    Assertions.assertEquals(ASK_LATER_YEAR_SPECIALTY, bot.getOutcomingMessageList().getLast());
                }
                Assertions.assertEquals(
                        new UserEntryBuilder(currentUserEntry).year(Integer.parseInt(year)).build(),
                        storage.userEntries.get(0L).get());
            }

            /**
             * <p>Проверяем, что бот не принимает иных.</p>
             *
             * <ol>
             *     <li>Отправляем некорректные данные.</li>
             *     <li>Проверяем, что состояние пользователя не изменилось.</li>
             *     <li>Проверяем, что запись пользователя не изменилась.</li>
             *     <li>Проверяем, что запросил повторный ввод.</li>
             * </ol>
             *
             * @param text некорректное сообщение.
             */
            @ParameterizedTest(name = "\"{0}\" не является годом обучения")
            @NullAndEmptySource
            @ValueSource(strings = {"0", "01", " 1", "2 ", " 3 ", "7", "10", "a", "((("})
            @DisplayName("Некорректный ввод")
            void testIncorrectData(String text) {
                logic.processMessage(new LocalMessageBuilder().text(text).build(), 0L, bot);

                Assertions.assertEquals(currentUser, storage.users.get(0L).get());
                Assertions.assertEquals(currentUserEntry, storage.userEntries.get(0L).get());

                Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
                Assertions.assertEquals(ASK_YEAR, bot.getOutcomingMessageList().getLast());
            }

            /**
             * <p>Проверяем, что бот корректно возвращает на шаг назад, то есть в состояние запроса ФИО.</p>
             *
             * <ol>
             *     <li>Нажимаем кнопку "Назад".</li>
             *     <li>Проверяем, что бот в вернулся в состояние запроса ФИО (запросил ФИО снова).</li>
             * </ol>
             */
            @Test
            @DisplayName("Нажата кнопка 'Назад'")
            void testBackCommand() {
                logic.processMessage(BACK_MESSAGE, 0L, bot);
                Assertions.assertEquals(
                        new UserBuilder(0L, MathMechBotUserState.REGISTRATION_NAME).build(),
                        storage.users.get(0L).get());
            }
        }

        /**
         * Тесты для команды регистрации в состояниях запроса направления подготовки.
         */
        @Nested
        @DisplayName("Состояние: запрос направления подготовки")
        class Specialty {
            User currentFirstYearUser;
            UserEntry currentFirstYearUserEntry;
            User currentSecondYearUser;
            UserEntry currentSecondYearUserEntry;

            /**
             * До тестов регистрируем двух студентов, первокурсника и второкурсника.
             */
            @BeforeEach
            void setupTest() {
                logic.processMessage(REGISTER_MESSAGE, 0L, bot);
                logic.processMessage(new LocalMessageBuilder().text("Иванов Иван Иванович").build(), 0L, bot);
                logic.processMessage(new LocalMessageBuilder().text("1").build(), 0L, bot);
                currentFirstYearUser = storage.users.get(0L).get();
                currentFirstYearUserEntry = storage.userEntries.get(0L).get();

                logic.processMessage(REGISTER_MESSAGE, 1L, bot);
                logic.processMessage(new LocalMessageBuilder().text("Ильин Илья Ильич").build(), 1L, bot);
                logic.processMessage(new LocalMessageBuilder().text("2").build(), 1L, bot);
                currentSecondYearUser = storage.users.get(1L).get();
                currentSecondYearUserEntry = storage.userEntries.get(1L).get();

                bot.getOutcomingMessageList().clear();
            }

            /**
             * <p>Проверяем, что бот принимает общие направления и для первокурсника, и для второкурсника.</p>
             *
             * <ol>
             *     <li>Отправляем от первокурсника корректную аббревиатуру направления подготовки.</li>
             *     <li>Проверяем, что состояние студента изменилось на запрос группы.</li>
             *     <li>Проверяем, что запись студента содержит указанное направление.</li>
             *     <li>Проверяем, что бот отправил сообщение с запросом группы.</li>
             *     <li>Аналогично для второкурсника.</li>
             * </ol>
             *
             * @param specialtyAbbreviation корректная аббревиатура направления подготовки, общего для всех курсов.
             */
            @ParameterizedTest(name = "\"{0}\" - корректное направление подготовки для обоих курсов")
            @ValueSource(strings = {"КБ", "ФТ"})
            @DisplayName("Все возможные корректные направления подготовки, общие для всех курсов")
            void testCommonData(String specialtyAbbreviation) {
                // Первокурсник
                logic.processMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 0L, bot);
                Assertions.assertEquals(
                        new UserBuilder(0L, MathMechBotUserState.REGISTRATION_GROUP).build(),
                        storage.users.get(0L).get());
                Assertions.assertEquals(
                        new UserEntryBuilder(currentFirstYearUserEntry).specialty(specialtyAbbreviation).build(),
                        storage.userEntries.get(0L).get());
                Assertions.assertEquals(ASK_GROUP_NUMBER, bot.getOutcomingMessageList().getFirst());

                // Второкурсник
                logic.processMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 1L, bot);
                Assertions.assertEquals(
                        new UserBuilder(1L, MathMechBotUserState.REGISTRATION_GROUP).build(),
                        storage.users.get(1L).get());
                Assertions.assertEquals(
                        new UserEntryBuilder(currentSecondYearUserEntry).specialty(specialtyAbbreviation).build(),
                        storage.userEntries.get(1L).get());
                Assertions.assertEquals(ASK_GROUP_NUMBER, bot.getOutcomingMessageList().get(1));
            }

            /**
             * <p>Проверяем, что бот принимает направления подготовки первого курса только у первокурсников.</p>
             *
             * <ol>
             *     <li>Отправляем от старшекурсника корректную аббревиатуру направления подготовки первого курса.</li>
             *     <li>Проверяем, что состояние пользователя изменилось.</li>
             *     <li>Проверяем, что введённые данные сохранились.</li>
             *     <li>Проверяем, что бот отправил запрос номера группы.</li>
             *
             *     <li>Отправляем ту же аббревиатуру от второкурсника.</li>
             *     <li>Проверяем, что состояние пользователя не изменилось.</li>
             *     <li>Проверяем, что введённые данные не изменились.</li>
             *     <li>Проверяем, что бот запрос повторный ввод.</li>
             * </ol>
             *
             * @param specialtyAbbreviation корректная аббревиатура направления подготовки только первого курса.
             */
            @ParameterizedTest(name = "\"{0}\" - корректное направление подготовки только для первого курса")
            @ValueSource(strings = {"КНМО", "ММП"})
            @DisplayName("Все возможные корректные направления подготовки первого курса")
            void testFirstYearOnlyData(String specialtyAbbreviation) {
                // Первокурсник
                logic.processMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 0L, bot);
                Assertions.assertEquals(
                        new UserBuilder(0L, MathMechBotUserState.REGISTRATION_GROUP).build(),
                        storage.users.get(0L).get());
                Assertions.assertEquals(
                        new UserEntryBuilder(currentFirstYearUserEntry).specialty(specialtyAbbreviation).build(),
                        storage.userEntries.get(0L).get());
                Assertions.assertEquals(ASK_GROUP_NUMBER, bot.getOutcomingMessageList().getFirst());
                bot.getOutcomingMessageList().clear();

                // Второкурсник
                logic.processMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 1L, bot);
                Assertions.assertEquals(currentSecondYearUser, storage.users.get(1L).get());
                Assertions.assertEquals(currentSecondYearUserEntry, storage.userEntries.get(1L).get());
                Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
                Assertions.assertEquals(ASK_LATER_YEAR_SPECIALTY, bot.getOutcomingMessageList().get(1));
            }

            /**
             * <p>Проверяем, что бот принимает направления подготовки старших курсов только у старшекурсников.</p>
             *
             * <ol>
             *     <li>Отправляем от первокурсника корректную аббревиатуру направления подготовки старших курсов.</li>
             *     <li>Проверяем, что состояние пользователя не изменилось.</li>
             *     <li>Проверяем, что введённые данные не изменились.</li>
             *     <li>Проверяем, что бот запрос повторный ввод.</li>
             *
             *     <li>Отправляем ту же аббревиатуру от второкурсника.</li>
             *     <li>Проверяем, что состояние пользователя изменилось.</li>
             *     <li>Проверяем, что введённые данные сохранились.</li>
             *     <li>Проверяем, что бот отправил запрос номера группы.</li>
             * </ol>
             *
             * @param specialtyAbbreviation корректная аббревиатура направления подготовки только старших курсов.
             */
            @ParameterizedTest(name = "\"{0}\" - корректное направление подготовки только для поздних курса")
            @ValueSource(strings = {"КН", "МО", "МХ", "ПМ"})
            @DisplayName("Все возможные корректные направления подготовки поздних курса")
            void testLaterYearsOnlyData(String specialtyAbbreviation) {
                // Первокурсник
                logic.processMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 0L, bot);
                Assertions.assertEquals(currentFirstYearUser, storage.users.get(0L).get());
                Assertions.assertEquals(currentFirstYearUserEntry, storage.userEntries.get(0L).get());
                Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
                Assertions.assertEquals(ASK_FIRST_YEAR_SPECIALTY, bot.getOutcomingMessageList().get(1));
                bot.getOutcomingMessageList().clear();

                // Второкурсник
                logic.processMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 1L, bot);
                Assertions.assertEquals(
                        new UserBuilder(1L, MathMechBotUserState.REGISTRATION_GROUP).build(),
                        storage.users.get(1L).get());
                Assertions.assertEquals(
                        new UserEntryBuilder(currentSecondYearUserEntry).specialty(specialtyAbbreviation).build(),
                        storage.userEntries.get(1L).get());
                Assertions.assertEquals(ASK_GROUP_NUMBER, bot.getOutcomingMessageList().getFirst());
                bot.getOutcomingMessageList().clear();
            }

            /**
             * <p>Проверяем, что бот не принимает ни от кого неверные строки.</p>
             *
             * <ol>
             *     <li>Отправляем сообщение, не содержащее разрешённого направления подготовки.</li>
             *     <li>Проверяем, что состояние первокурсника не изменилось.</li>
             *     <li>Проверяем, что запись первокурсника не изменилось.</li>
             *     <li>Аналогично проверяем для второкурсника.</li>
             * </ol>
             *
             * @param incorrectMessageText не разрешённое направление подготовки.
             */
            @ParameterizedTest(name = "\"{0}\" не является разрешённым направление подготовки")
            @EmptySource
            @NullSource
            @ValueSource(strings = {" КН", "КН ", " КН ", "кн", "string", "0"})
            @DisplayName("Некорректный ввод")
            void testIncorrectData(String incorrectMessageText) {
                // Первокурсник
                logic.processMessage(new LocalMessageBuilder().text(incorrectMessageText).build(), 0L, bot);
                Assertions.assertEquals(currentFirstYearUser, storage.users.get(0L).get());
                Assertions.assertEquals(currentFirstYearUserEntry, storage.userEntries.get(0L).get());
                Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
                Assertions.assertEquals(ASK_FIRST_YEAR_SPECIALTY, bot.getOutcomingMessageList().get(1));
                bot.getOutcomingMessageList().clear();

                // Второкурсник
                logic.processMessage(new LocalMessageBuilder().text(incorrectMessageText).build(), 1L, bot);
                Assertions.assertEquals(currentSecondYearUser, storage.users.get(1L).get());
                Assertions.assertEquals(currentSecondYearUserEntry, storage.userEntries.get(1L).get());
                Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
                Assertions.assertEquals(ASK_LATER_YEAR_SPECIALTY, bot.getOutcomingMessageList().get(1));
            }

            /**
             * <p>Проверяем, что бот корректно возвращает на шаг назад, то есть в состояние запроса года обучения.</p>
             *
             * <ol>
             *     <li>Нажимаем кнопку "Назад"</li>
             *     <li>Проверяем, что для обоих пользователей состояние изменилось на запрос года обучения.</li>
             *     <li>Проверяем, что бот вновь спросил год обучения.</li>
             * </ol>
             */
            @Test
            @DisplayName("Нажата кнопка 'Назад'")
            void testBackCommand() {
                logic.processMessage(BACK_MESSAGE, 0L, bot);
                logic.processMessage(BACK_MESSAGE, 1L, bot);

                Assertions.assertEquals(
                        new UserBuilder(0L, MathMechBotUserState.REGISTRATION_YEAR).build(),
                        storage.users.get(0L).get());
                Assertions.assertEquals(
                        new UserBuilder(1L, MathMechBotUserState.REGISTRATION_YEAR).build(),
                        storage.users.get(1L).get());

                Assertions.assertEquals(ASK_YEAR, bot.getOutcomingMessageList().getFirst());
                Assertions.assertEquals(ASK_YEAR, bot.getOutcomingMessageList().get(1));
            }
        }

        /**
         * Тесты для команды регистрации в состоянии запроса номера группы.
         */
        @Nested
        @DisplayName("Состояние: запрос номера группы")
        class Group {
            User currentUser;
            UserEntry currentUserEntry;

            @BeforeEach
            void setupTest() {
                logic.processMessage(REGISTER_MESSAGE, 0L, bot);
                logic.processMessage(new LocalMessageBuilder().text("Максимов Андрей").build(), 0L, bot);
                logic.processMessage(new LocalMessageBuilder().text("2").build(), 0L, bot);
                logic.processMessage(new LocalMessageBuilder().text("ФТ").build(), 0L, bot);
                currentUser = storage.users.get(0L).get();
                currentUserEntry = storage.userEntries.get(0L).get();
                bot.getOutcomingMessageList().clear();
            }

            /**
             * <p>Проверяем, что бот принимает корректные номера групп.</p>
             *
             * <ol>
             *     <li>Отправляем корректный номер группы.</li>
             *     <li>Проверяем, что бот отправил нужное сообщение.</li>
             *     <li>Проверяем, что пользователь перешёл в нужное состояние.</li>
             *     <li>Проверяем, что пользовательские данные теперь содержат номер группы.</li>
             * </ol>
             *
             * @param group корректный номер группы.
             */
            @DisplayName("Все возможные корректные номера группы")
            @ParameterizedTest(name = "\"{0}\" - корректный номер группы")
            @ValueSource(strings = {"1", "2", "3", "4", "5", "6"})
            void testCorrectData(String group) {
                logic.processMessage(new LocalMessageBuilder().text(group).build(), 0L, bot);

                Assertions.assertEquals(
                        new UserBuilder(0L, MathMechBotUserState.REGISTRATION_MEN).build(),
                        storage.users.get(0L).get());
                Assertions.assertEquals(
                        new UserEntryBuilder(currentUserEntry).group(Integer.parseInt(group)).build(),
                        storage.userEntries.get(0L).get());
                Assertions.assertEquals(ASK_MEN, bot.getOutcomingMessageList().getLast());
            }

            /**
             * <p>Проверяем, что бот не принимает некорректные сообщения.</p>
             *
             * <ol>
             *     <li>Отправляем некорректные данные.</li>
             *     <li>Проверяем, что состояние пользователя не изменилось.</li>
             *     <li>Проверяем, что запись пользователя не изменилась.</li>
             *     <li>Проверяем, что запросил повторный ввод.</li>
             * </ol>
             *
             * @param text некорректное сообщение.
             */
            @DisplayName("Некорректный ввод")
            @ParameterizedTest(name = "\"{0}\" не является номером группы")
            @NullAndEmptySource
            @ValueSource(strings = {"0", "01", " 1", "2 ", " 3 ", "7", "10", "a", "((("})
            void testIncorrectData(String text) {
                logic.processMessage(new LocalMessageBuilder().text(text).build(), 0L, bot);

                Assertions.assertEquals(currentUser, storage.users.get(0L).get());
                Assertions.assertEquals(currentUserEntry, storage.userEntries.get(0L).get());
                Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
            }

            /**
             * <p>Проверяем, что бот корректно возвращает на шаг назад,
             * то есть в состояние запроса года обучения.</p>
             *
             * <ol>
             *     <li>Нажимаем кнопку "Назад".</li>
             *     <li>Проверяем, что бот в вернулся в состояние запроса года обучения.</li>
             * </ol>
             */
            @Test
            @DisplayName("Нажата кнопка 'Назад'")
            void testBackCommand() {
                logic.processMessage(BACK_MESSAGE, 0L, bot);
                Assertions.assertEquals(
                        new UserBuilder(0L, MathMechBotUserState.REGISTRATION_YEAR).build(),
                        storage.users.get(0L).get());
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
        @DisplayName("Нажата кнопка 'Да' во время подтверждения удаления данных")
        void testRegisteredUserSaysYes() {
            registerUser(0L, "Иванов Иван Иванович", 1, "ММП", 2, "МЕН-123456");

            logic.processMessage(DELETE_MESSAGE, 0L, bot);
            Assertions.assertEquals(
                    new LocalMessageBuilder().text("""
                                    Точно удаляем?

                                    ФИО: Иванов Иван Иванович
                                    Группа: ММП-102 (МЕН-123456)""")
                            .buttons(YES_NO_BACK)
                            .build(),
                    bot.getOutcomingMessageList().getFirst());

            logic.processMessage(ACCEPT_MESSAGE, 0L, bot);
            Assertions.assertEquals(
                    new LocalMessageBuilder().text("Удаляем...").build(),
                    bot.getOutcomingMessageList().get(1));

            Assertions.assertTrue(storage.userEntries.get(0L).isEmpty());
            Assertions.assertEquals(
                    new UserBuilder(0L, MathMechBotUserState.DEFAULT).build(),
                    storage.users.get(0L).get());
            Assertions.assertEquals(HELP, bot.getOutcomingMessageList().get(2));
        }

        /**
         * <p>Проверяем случай, когда зарегистрированный пользователь
         * запускает команду /delete, но не подтверждает удаление.</p>
         *
         * <ol>
         *     <li>Регистрируемся.</li>
         *     <li>Запускаем команду <code>/delete</code>.</li>
         *     <li>Нажимаем кнопку "Неа".</li>
         *     <li>Проверяем, что бот подтвердил не удаление данных.</li>
         *     <li>Проверяем, что пользователь вернулся в дефолтное состояние.</li>
         *     <li>Проверяем, что данные не удалились.</li>
         *     <li>Проверяем, что пользователю отправилась справка.</li>
         * </ol>
         */
        @Test
        @DisplayName("Нажата кнопка 'Неа' во время подтверждения удаления данных")
        void testRegisteredUserSaysNo() {
            registerUser(0L, "Иванов Иван Иванович", 1, "ММП", 2, "МЕН-123456");
            final UserEntry userEntryBeforeDelete = storage.userEntries.get(0L).get();

            logic.processMessage(DELETE_MESSAGE, 0L, bot);
            logic.processMessage(DECLINE_MESSAGE, 0L, bot);
            Assertions.assertEquals(
                    new LocalMessageBuilder().text("Отмена...").build(),
                    bot.getOutcomingMessageList().get(1));

            Assertions.assertEquals(
                    new UserBuilder(0L, MathMechBotUserState.DEFAULT).build(),
                    storage.users.get(0L).get());
            Assertions.assertEquals(
                    userEntryBeforeDelete,
                    storage.userEntries.get(0L).get());
            Assertions.assertEquals(HELP, bot.getOutcomingMessageList().get(2));
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
        @DisplayName("Нажата кнопка 'Назад' во время подтверждения удаления данных")
        void testRegisteredUserSaysBack() {
            registerUser(0L, "Иванов Иван Иванович", 1, "ММП", 2, "МЕН-123456");
            final UserEntry userEntryBeforeDelete = storage.userEntries.get(0L).get();

            logic.processMessage(DELETE_MESSAGE, 0L, bot);
            logic.processMessage(BACK_MESSAGE, 0L, bot);

            Assertions.assertEquals(
                    new UserBuilder(0L, MathMechBotUserState.DEFAULT).build(),
                    storage.users.get(0L).get());
            Assertions.assertEquals(
                    userEntryBeforeDelete,
                    storage.userEntries.get(0L).get());
            Assertions.assertEquals(HELP, bot.getOutcomingMessageList().get(1));
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
         */
        @Test
        @DisplayName("Вместо подтверждения пришёл неожиданный текст")
        void testRegisteredUserSaysSomethingElse() {
            registerUser(0L, "Иванов Иван Иванович", 1, "ММП", 2, "МЕН-123456");
            final UserEntry userEntryBeforeDelete = storage.userEntries.get(0L).get();

            logic.processMessage(DELETE_MESSAGE, 0L, bot);
            logic.processMessage(new LocalMessageBuilder().text("Some string").build(), 0L, bot);

            Assertions.assertEquals(
                    new UserBuilder(0L, MathMechBotUserState.DELETION_CONFIRMATION).build(),
                    storage.users.get(0L).get());
            Assertions.assertEquals(
                    userEntryBeforeDelete,
                    storage.userEntries.get(0L).get());
            Assertions.assertEquals(TRY_AGAIN, bot.getOutcomingMessageList().get(1));
            Assertions.assertEquals(
                    new LocalMessageBuilder().text("""
                                    Точно удаляем?

                                    ФИО: Иванов Иван Иванович
                                    Группа: ММП-102 (МЕН-123456)""")
                            .buttons(YES_NO_BACK)
                            .build(),
                    bot.getOutcomingMessageList().get(2));
        }
    }
}

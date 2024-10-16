package ru.urfu.logics.mathmechbot.registration;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.DummyBot;
import ru.urfu.logics.mathmechbot.TestConstants;
import ru.urfu.logics.mathmechbot.TestUtils;
import ru.urfu.mathmechbot.MathMechBotCore;
import ru.urfu.mathmechbot.models.MathMechBotUserState;
import ru.urfu.mathmechbot.models.UserBuilder;
import ru.urfu.mathmechbot.models.UserEntryBuilder;
import ru.urfu.mathmechbot.storages.MathMechStorage;
import ru.urfu.mathmechbot.storages.UserArrayStorage;
import ru.urfu.mathmechbot.storages.UserEntryArrayStorage;

/**
 * Тесты для команды регистрации в состоянии запроса ФИО.
 */
@DisplayName("[/register] Состояние: ожидание ФИО")
final class FullNameTest {
    private TestUtils utils;
    private MathMechStorage storage;
    private MathMechBotCore logic;
    private DummyBot bot;

    /**
     * Создаём объект логики, ложного бота и утилиты для каждого теста.
     */
    @BeforeEach
    void setupTest() {
        storage = new MathMechStorage(new UserArrayStorage(), new UserEntryArrayStorage());
        logic = new MathMechBotCore(storage);
        bot = new DummyBot();
        utils = new TestUtils(logic, bot);
    }

    /**
     * <p>Аргументы для testCorrectData.</p>
     *
     * @return аргументы.
     */
    static Stream<Arguments> testCorrectData() {
        return Stream.of(
                Arguments.of("Иванов Иван", "Иванов", "Иван", null),
                Arguments.of("   Дим   Димыч   ", "Дим", "Димыч", null),
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
     * @param surname             фамилия, содержащаяся в сообщении.
     * @param name                имя, содержащееся в сообщении.
     * @param patronym            отчество, содержащееся в сообщении.
     */
    @DisplayName("Корректный ввод")
    @MethodSource
    @ParameterizedTest(name = "\"{0}\" - сообщение, содержащее корректное ФИО")
    void testCorrectData(String incomingMessageText, String surname, String name, String patronym) {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.REGISTER_MESSAGE));
        Assertions.assertEquals(RegistrationConstants.ASK_FULL_NAME, bot.getOutcomingMessageList().getFirst());

        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(incomingMessageText).build()));

        Assertions.assertEquals(
                new UserEntryBuilder(0L, surname, name, 0L).patronym(patronym).build(),
                storage.getUserEntries().get(0L).orElseThrow()
        );
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.REGISTRATION_YEAR).build(),
                storage.getUsers().get(0L).orElseThrow()
        );
        Assertions.assertEquals(RegistrationConstants.ASK_YEAR, bot.getOutcomingMessageList().get(1));
    }

    /**
     * <p>Проверяем, что бот не принимает некорректные данные.</p>
     *
     * <ol>
     *     <li>Отправляем команду <code>/register</code>.</li>
     *     <li>Отправляем некорректное ФИО или ФИ.</li>
     *     <li>Проверяем, что бот ничего не сохранил.</li>
     *     <li>Проверяем, что состояние пользователя не изменилось.</li>
     *     <li>Проверяем, что бот запросил повторить ввод.</li>
     * </ol>
     *
     * @param text некорректные данные.
     */
    @DisplayName("Некорректный ввод")
    @NullAndEmptySource
    @ValueSource(strings = {
            "И", "И И", "И И", "Иванова", "Иванова И", "И Иванов",
            "И Иванов Иванов", "Иванова Иванов И", "Иванова И Иванов",
            "ИВанова Иванов", "Иванова ИВанов", "Иванова Иванов ИВанов",
    })
    @ParameterizedTest(name = "\"{0}\" не содержит корректное ФИО")
    void testIncorrectData(String text) {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.REGISTER_MESSAGE));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(text).build()));

        Assertions.assertTrue(storage.getUserEntries().get(0L).isEmpty());
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.REGISTRATION_NAME).build(),
                storage.getUsers().get(0L).orElseThrow()
        );
        Assertions.assertEquals(TestConstants.TRY_AGAIN, bot.getOutcomingMessageList().getLast());
    }

    /**
     * <p>Проверяем, что бот корректно возвращает на шаг назад на этапе запроса ФИО.</p>
     *
     * <ol>
     *     <li>Отправляем команду <code>/register</code>.</li>
     *     <li>Нажимаем кнопку "Назад"</li>
     *     <li>Проверяем, что бот отравил запрос года обучения.</li>
     *     <li>Проверяем, что бот не сохранил никаких данных.</li>
     *     <li>Проверяем, что состояние пользователя изменилось на дефолтное.</li>
     * </ol>
     */
    @Test
    @DisplayName("Кнопка 'Назад'")
    void testBackCommand() {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.REGISTER_MESSAGE));
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.BACK_MESSAGE));

        Assertions.assertEquals(TestConstants.HELP, bot.getOutcomingMessageList().get(1));
        Assertions.assertTrue(storage.getUserEntries().get(0L).isEmpty());
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.DEFAULT).build(),
                storage.getUsers().get(0L).orElseThrow()
        );
    }
}

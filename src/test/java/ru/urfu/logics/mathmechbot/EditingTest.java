package ru.urfu.logics.mathmechbot;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.urfu.logics.DummyBot;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.storages.MathMechStorage;

/**
 * Тесты для команды изменения информации о себе - /edit.
 */
@DisplayName("[/edit] Редактирование информации")
final class EditingTest {
    private TestUtils utils;
    private DummyBot bot;

    private final static String ACCEPT_COMMAND = "/yes";
    private final static String DECLINE_COMMAND = "/no";
    private final static String BACK_COMMAND = "/back";
    private final static String INFO_COMMAND = "/info";
    private final static String EDIT_COMMAND = "/edit";
    private final static String EDITING_FULL_NAME_COMMAND = "full_name";
    private final static String EDITING_YEAR_COMMAND = "year";
    private final static String EDITING_SPECIALITY_COMMAND = "speciality";
    private final static String EDITING_GROUP_COMMAND = "number_of_group";
    private final static String EDITING_MEN_COMMAND = "men";

    private final LocalButton backButton = new LocalButton("Назад", BACK_COMMAND);
    private final LocalMessage editMessage = new LocalMessage(EDIT_COMMAND);
    private final LocalMessage acceptMessage = new LocalMessage(ACCEPT_COMMAND);
    private final LocalMessage declineMessage = new LocalMessage(DECLINE_COMMAND);

    @SuppressWarnings("RegexpSingleline")
    private final static String EDITING_FULL_TEST_CHECK_INFO = """
            Данные о Вас:
            
            ФИО: Иванов Иван Иванович
            Группа: КН-201 (МЕН-200201)""";

    private final LocalMessage help = new LocalMessageBuilder()
            .text("""
                    /start - начало общения с ботом
                    /help - выводит команды, которые принимает бот
                    /register - регистрация
                    /info - информация о Вас
                    /edit - изменить информацию
                    /delete - удалить информацию о Вас""")
            .build();

    private final List<LocalButton> editingChooseButtons = List.of(
            new LocalButton("ФИО", EDITING_FULL_NAME_COMMAND),
            new LocalButton("Курс", EDITING_YEAR_COMMAND),
            new LocalButton("Направление", EDITING_SPECIALITY_COMMAND),
            new LocalButton("Группа", EDITING_GROUP_COMMAND),
            new LocalButton("МЕН", EDITING_MEN_COMMAND),
            backButton);

    private final List<LocalButton> editingYearButtons = List.of(
            new LocalButton("1 курс", "1"),
            new LocalButton("2 курс", "2"),
            new LocalButton("3 курс", "3"),
            new LocalButton("4 курс", "4"),
            new LocalButton("5 курс", "5"),
            new LocalButton("6 курс", "6"),
            backButton);

    private final List<LocalButton> editingSpecialityButtons = List.of(
            new LocalButton("КН", "КН"),
            new LocalButton("МО", "МО"),
            new LocalButton("МХ", "МХ"),
            new LocalButton("МТ", "МТ"),
            new LocalButton("ПМ", "ПМ"),
            new LocalButton("КБ", "КБ"),
            new LocalButton("ФТ", "ФТ"),
            backButton);

    private final List<LocalButton> editingGroupButtons = List.of(
            new LocalButton("1 группа", "1"),
            new LocalButton("2 группа", "2"),
            new LocalButton("3 группа", "3"),
            new LocalButton("4 группа", "4"),
            new LocalButton("5 группа", "5"),
            backButton);

    private final List<LocalButton> editingAdditionalButtons = List.of(
            new LocalButton("Да", ACCEPT_COMMAND),
            new LocalButton("Нет", DECLINE_COMMAND)
    );

    private final LocalMessage editingChooseMessage = new LocalMessageBuilder()
            .text("Что Вы хотите изменить?")
            .buttons(editingChooseButtons)
            .build();

    private final LocalMessage editingFullNameMessage = new LocalMessageBuilder()
            .text("""
                        Введите свое ФИО в формате:
                        Иванов Артём Иванович
                        Без дополнительных пробелов и с буквой ё, если нужно.""")
            .buttons(List.of(backButton))
            .build();

    private final LocalMessage editingYearMessage = new LocalMessageBuilder()
            .text("На каком курсе Вы обучаетесь?")
            .buttons(editingYearButtons)
            .build();

    private final LocalMessage editingSpecialityMessage = new LocalMessageBuilder()
            .text("""
                    На каком направлении?
                    Если Вы не видите свое направление, то, возможно, Вы выбрали не тот курс.""")
            .buttons(editingSpecialityButtons)
            .build();

    private final LocalMessage editingGroupMessage = new LocalMessageBuilder()
            .text("Какая у Вас группа?")
            .buttons(editingGroupButtons)
            .build();

    private final LocalMessage editingMenMessage = new LocalMessageBuilder()
                .text("Введите свою академическую группу в формате:\nМЕН-123456")
                .buttons(List.of(backButton))
            .build();

    private final LocalMessage editingAdditionalMessage = new LocalMessageBuilder()
            .text("На этом всё?")
            .buttons(editingAdditionalButtons)
            .build();

    /**
     * Создаём объект логики, ложного бота и утилиты для каждого теста, регистрируемся.
     */
    @BeforeEach
    @SuppressWarnings("MagicNumber")
    void setupTest() {
        final MMBCore logic = new MMBCore(new MathMechStorage());
        bot = new DummyBot();
        utils = new TestUtils(logic, bot);

        utils.registerUser(0L, "Денисов Денис Денисович",
                4, "КБ", 2, "МЕН-162534");

        bot.getOutcomingMessageList().clear();
    }

    /**
     * <p>Проверяем, что команда '/edit' меняет состояние ядра и выводит нужное сообщение.</p>
     *
     * <ol>
     *     <li>Проверяем, что бот отправил нужное сообщение.</li>
     *     <li>Проверяем, что пользователь перешёл в нужное состояние.</li>
     *     <li>Проверяем повторный запрос на изменение.</li>
     *     <li>Проверяем корректное состояние при обработке запроса на изменение ФИО.</li>
     *     <li>Проверяем команду '/back'. Новое состояние и вывод ядра.</li>
     *     <li>Проверяем корректный вывод и состояние при запросе на изменение группы.</li>
     *     <li>Проверяем сообщение на дополнительное изменение.</li>
     *     <li>Проверяем Корректный выход из состояния изменения.</li>
     * </ol>
     */
    @Test
    @SuppressWarnings("MagicNumber")
    @DisplayName("Команда '/edit' ")
    void testEdit() {
        utils.sendMessageToLogic(editMessage);
        Assertions.assertEquals(editingChooseMessage,
                bot.getOutcomingMessageList().getFirst());

        utils.sendMessageToLogic(new LocalMessage(EDIT_COMMAND));
        Assertions.assertEquals(
                new LocalMessage("Попробуйте снова."),
                bot.getOutcomingMessageList().get(1));
    }

    /**
     * Проверка изменения ФИО.
     */
    @Test
    @SuppressWarnings("MagicNumber")
    @DisplayName("Изменение ФИО")
    void testFullName() {
        utils.sendMessageToLogic(editMessage);

        utils.sendMessageToLogic(new LocalMessage(EDITING_FULL_NAME_COMMAND));
        Assertions.assertEquals(editingFullNameMessage,
                bot.getOutcomingMessageList().get(1));

        utils.sendMessageToLogic(new LocalMessage("Иванов Иван Сергеевич"));
        Assertions.assertEquals(editingAdditionalMessage,
                bot.getOutcomingMessageList().get(3));

        utils.sendMessageToLogic(declineMessage);
    }

    /**
     * Проверка работы кнопки "назад".
     */
    @Test
    @SuppressWarnings("MagicNumber")
    @DisplayName("Кнопка 'Назад'")
    void testBackButton() {
        utils.sendMessageToLogic(editMessage);
        utils.sendMessageToLogic(new LocalMessage(BACK_COMMAND));
        Assertions.assertEquals(help, bot.getOutcomingMessageList().get(1));
    }

    /**
     * Проверка изменения всей информации.
     */
    @Test
    @SuppressWarnings("MagicNumber")
    @DisplayName("Полное изменение")
    void testFullCheck() {
        final String testSurname = "Иванов";
        final String testName = "Иван";
        final String testPatronym = "Иванович";
        final String testYear = "2";
        final String testSpeciality = "КН";
        final String testGroup = "1";
        final String testMen = "МЕН-200201";

        //Вводим /edit
        utils.sendMessageToLogic(editMessage);
        Assertions.assertEquals(editingChooseMessage,
                bot.getOutcomingMessageList().getFirst());

        //Вводим изменить ФИО
        utils.sendMessageToLogic(new LocalMessage(EDITING_FULL_NAME_COMMAND));
        Assertions.assertEquals(editingFullNameMessage, bot.getOutcomingMessageList().get(1));

        //Вводим ФИО
        utils.sendMessageToLogic(new LocalMessage(
                String.join(" ", testSurname, testName, testPatronym)));
        Assertions.assertEquals(editingAdditionalMessage, bot.getOutcomingMessageList().get(3));

        //Выбираем изменить что-то еще
        utils.sendMessageToLogic(declineMessage);
        Assertions.assertEquals(editingChooseMessage, bot.getOutcomingMessageList().get(4));

        //Выбираем изменить курс
        utils.sendMessageToLogic(new LocalMessage(EDITING_YEAR_COMMAND));
        Assertions.assertEquals(editingYearMessage, bot.getOutcomingMessageList().get(5));

        //Выбираем курс
        utils.sendMessageToLogic(new LocalMessage(testYear));
        Assertions.assertEquals(editingAdditionalMessage, bot.getOutcomingMessageList().get(7));

        //Выбираем изменить что-то еще
        utils.sendMessageToLogic(declineMessage);
        Assertions.assertEquals(
                editingChooseMessage,
                bot.getOutcomingMessageList().get(8));

        //Выбираем изменить направление
        utils.sendMessageToLogic(new LocalMessage(EDITING_SPECIALITY_COMMAND));
        Assertions.assertEquals(
                editingSpecialityMessage,
                bot.getOutcomingMessageList().get(9));

        //Выбираем направление
        utils.sendMessageToLogic(new LocalMessage(testSpeciality));
        Assertions.assertEquals(
                editingAdditionalMessage,
                bot.getOutcomingMessageList().get(11));

        //Выбираем изменить что-то еще
        utils.sendMessageToLogic(declineMessage);
        Assertions.assertEquals(
                editingChooseMessage,
                bot.getOutcomingMessageList().get(12));

        //Выбираем изменить группу
        utils.sendMessageToLogic(new LocalMessage(EDITING_GROUP_COMMAND));
        Assertions.assertEquals(
                editingGroupMessage,
                bot.getOutcomingMessageList().get(13));

        //Выбираем группу
        utils.sendMessageToLogic(new LocalMessage(testGroup));
        Assertions.assertEquals(
                editingAdditionalMessage,
                bot.getOutcomingMessageList().get(15));

        //Выбираем изменить что-то еще
        utils.sendMessageToLogic(declineMessage);
        Assertions.assertEquals(
                editingChooseMessage,
                bot.getOutcomingMessageList().get(16));

        //Выбираем изменить МЕН
        utils.sendMessageToLogic(new LocalMessage(EDITING_MEN_COMMAND));
        Assertions.assertEquals(editingMenMessage, bot.getOutcomingMessageList().get(17));

        //Вводим МЕН
        utils.sendMessageToLogic(new LocalMessage(testMen));
        Assertions.assertEquals(
                editingAdditionalMessage,
                bot.getOutcomingMessageList().get(19));

        //Выбираем ничего не менять
        utils.sendMessageToLogic(acceptMessage);
        Assertions.assertEquals(help, bot.getOutcomingMessageList().get(21));

        //Вызываем /info
        utils.sendMessageToLogic(new LocalMessage(INFO_COMMAND));
        Assertions.assertEquals(
                new LocalMessage(EDITING_FULL_TEST_CHECK_INFO),
                bot.getOutcomingMessageList().get(22));
    }
}

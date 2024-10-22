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
 * Тесты для команды изменения информации о себе - /edit.
 */
@DisplayName("[/edit] Редактирование информации")
final class EditingTest {
    private TestUtils utils;
    private MathMechBotCore logic;
    private DummyBot bot;

    private final static String ACCEPT_COMMAND = "/yes";
    private final static String DECLINE_COMMAND = "/no";
    private final static String BACK_COMMAND = "/back";
    private final static String INFO_COMMAND = "/info";
    private final static String REGISTER_COMMAND = "/register";
    private final static String EDIT_COMMAND = "/edit";
    private final static String EDITING_FULL_NAME_COMMAND = "full_name";
    private final static String EDITING_YEAR_COMMAND = "year";
    private final static String EDITING_SPECIALITY_COMMAND = "speciality";
    private final static String EDITING_GROUP_COMMAND = "number_of_group";
    private final static String EDITING_MEN_COMMAND = "men";

    private final LocalMessage registerMessage = new LocalMessage(REGISTER_COMMAND);
    private final LocalMessage editMessage = new LocalMessage(EDIT_COMMAND);
    private final LocalMessage acceptMessage = new LocalMessage(ACCEPT_COMMAND);
    private final LocalMessage declineMessage = new LocalMessage(DECLINE_COMMAND);
    private final LocalButton backButton = new LocalButton("Назад", BACK_COMMAND);

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
                    Если Вы не видите свое направление, то, возможно, Вы выбрали не тот курс.
                    """)
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
            .text("Хотите изменить что-нибудь еще?")
            .buttons(editingAdditionalButtons)
            .build();

    /**
     * Создаём объект логики, ложного бота и утилиты для каждого теста, регистрируемся.
     */
    @BeforeEach
    void setupTest() {
        logic = new MathMechBotCore(new MathMechStorage());
        bot = new DummyBot();
        utils = new TestUtils(logic, bot);

        logic.processMessage(utils.makeRequestFromMessage(registerMessage));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessage("Денисов Денис Денисович")));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessage("4")));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessage("КБ")));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessage("2")));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessage("МЕН-162534")));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessage(ACCEPT_COMMAND)));

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
        logic.processMessage(utils.makeRequestFromMessage(editMessage));

        Assertions.assertEquals(editingChooseMessage,
                bot.getOutcomingMessageList().getFirst());


        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(EDIT_COMMAND)));
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
        logic.processMessage(utils.makeRequestFromMessage(editMessage));

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(EDITING_FULL_NAME_COMMAND)));
        Assertions.assertEquals(editingFullNameMessage, bot.getOutcomingMessageList().get(1));


        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage("Иванов Иван Сергеевич")));
        Assertions.assertEquals(editingAdditionalMessage, bot.getOutcomingMessageList().get(3));

        logic.processMessage(utils.makeRequestFromMessage(declineMessage));
    }

    /**
     * Проверка работы кнопки "назад".
     */
    @Test
    @SuppressWarnings("MagicNumber")
    @DisplayName("Кнопка 'Назад'")
    void testBackButton() {
        logic.processMessage(utils.makeRequestFromMessage(editMessage));
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(BACK_COMMAND)));
        Assertions.assertEquals(help, bot.getOutcomingMessageList().get(1));
    }

    /**
     * Проверка изменения всей информации.
     */
    @Test
    @SuppressWarnings("MagicNumber")
    @DisplayName("Полное изменение")
    void testFullCheck() {
        final String TEST_SURNAME = "Иванов";
        final String TEST_NAME = "Иван";
        final String TEST_PATRONYM = "Иванович";
        final String TEST_YEAR = "2";
        final String TEST_SPECIALITY = "КН";
        final String TEST_GROUP = "1";
        final String TEST_MEN = "МЕН-200201";

        //Вводим /edit
        logic.processMessage(utils.makeRequestFromMessage(editMessage));
        Assertions.assertEquals(editingChooseMessage,
                bot.getOutcomingMessageList().getFirst());

        //Вводим изменить ФИО
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(EDITING_FULL_NAME_COMMAND)));
        Assertions.assertEquals(editingFullNameMessage, bot.getOutcomingMessageList().get(1));

        //Вводим ФИО
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(
                TEST_SURNAME + " " + TEST_NAME + " " + TEST_PATRONYM)));
        Assertions.assertEquals(editingAdditionalMessage, bot.getOutcomingMessageList().get(3));

        //Выбираем изменить что-то еще
        logic.processMessage(utils.makeRequestFromMessage(acceptMessage));
        Assertions.assertEquals(editingChooseMessage, bot.getOutcomingMessageList().get(4));

        //Выбираем изменить курс
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(EDITING_YEAR_COMMAND)));
        Assertions.assertEquals(editingYearMessage, bot.getOutcomingMessageList().get(5));

        //Выбираем курс
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(TEST_YEAR)));
        Assertions.assertEquals(editingAdditionalMessage, bot.getOutcomingMessageList().get(6));

        //Выбираем изменить что-то еще
        logic.processMessage(utils.makeRequestFromMessage(acceptMessage));
        Assertions.assertEquals(editingChooseMessage, bot.getOutcomingMessageList().get(7));

        //Выбираем изменить направление
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(EDITING_SPECIALITY_COMMAND)));
        Assertions.assertEquals(editingSpecialityMessage, bot.getOutcomingMessageList().get(8));

        //Выбираем направление
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(TEST_SPECIALITY)));
        Assertions.assertEquals(editingAdditionalMessage, bot.getOutcomingMessageList().get(9));

        //Выбираем изменить что-то еще
        logic.processMessage(utils.makeRequestFromMessage(acceptMessage));
        Assertions.assertEquals(editingChooseMessage, bot.getOutcomingMessageList().get(10));

        //Выбираем изменить группу
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(EDITING_GROUP_COMMAND)));
        Assertions.assertEquals(editingGroupMessage, bot.getOutcomingMessageList().get(11));

        //Выбираем группу
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(TEST_GROUP)));
        Assertions.assertEquals(editingAdditionalMessage, bot.getOutcomingMessageList().get(12));

        //Выбираем изменить что-то еще
        logic.processMessage(utils.makeRequestFromMessage(acceptMessage));
        Assertions.assertEquals(editingChooseMessage, bot.getOutcomingMessageList().get(13));

        //Выбираем изменить МЕН
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(EDITING_MEN_COMMAND)));
        Assertions.assertEquals(editingMenMessage, bot.getOutcomingMessageList().get(14));

        //Вводим МЕН
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(TEST_MEN)));
        Assertions.assertEquals(editingAdditionalMessage, bot.getOutcomingMessageList().get(15));

        //Выбираем ничего не менять
        logic.processMessage(utils.makeRequestFromMessage(declineMessage));
        Assertions.assertEquals(help, bot.getOutcomingMessageList().get(18));

        //Вызываем /info
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessage(INFO_COMMAND)));
        Assertions.assertEquals(new LocalMessage(EDITING_FULL_TEST_CHECK_INFO), bot.getOutcomingMessageList().get(19));
    }
}

package ru.urfu.logics.mathmechbot;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.DummyBot;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.MMBCore;

/**
 * Утилиты для тестирования матмех бота. В идеале должен быть статичным.
 */
public final class TestUtils {
    private final LogicCore logic;
    private final DummyBot bot;

    private final static String ACCEPT_COMMAND = "/yes";
    private final static String INFO_COMMAND = "/info";
    private final static String REGISTER_COMMAND = "/register";

    private final LocalMessage acceptMessage = new LocalMessage(ACCEPT_COMMAND);
    private final LocalMessage infoMessage = new LocalMessage(INFO_COMMAND);
    private final LocalMessage registerMessage = new LocalMessage(REGISTER_COMMAND);

    /**
     * Конструктор.
     *
     * @param logic ядро
     * @param bot   фейк-бот.
     */
    public TestUtils(MMBCore logic, DummyBot bot) {
        this.logic = logic;
        this.bot = bot;
    }

    /**
     * <p>Регистрирует человека со следующими данными.</p>
     *
     * <p>Нужна для быстрых тестов команд, где
     * требуется зарегистрированный пользователь.</p>
     *
     * @param id идентификатор отправителя
     * @param fullName  ФИО или ФИ
     * @param year      год обучения.
     * @param specialty аббревиатура направления.
     * @param group     номер группы.
     * @param men       группа в формате МЕН.
     */
    public void registerUser(long id, String fullName, int year, String specialty, int group, String men) {
        final List<LocalMessage> messages = List.of(
                registerMessage,
                new LocalMessageBuilder().text(fullName).build(),
                new LocalMessageBuilder().text(String.valueOf(year)).build(),
                new LocalMessageBuilder().text(specialty).build(),
                new LocalMessageBuilder().text(String.valueOf(group)).build(),
                new LocalMessageBuilder().text(men).build(),
                acceptMessage,
                infoMessage
        );

        for (final LocalMessage message : messages) {
            logic.processMessage(id, message, bot);
        }
        bot.getOutcomingMessageList().clear();
    }

    /**
     * <p>Посылает ядру сообщение на обработку. Помогает не писать все аргументы.</p>
     *
     * @param id идентификатор отправителя.
     * @param message отправляемое сообщение.
     */
    public void sendMessageToLogic(@NotNull Long id, @NotNull LocalMessage message) {
        logic.processMessage(id, message, bot);
    }

    /**
     * <p>Посылает ядру сообщение на обработку от пользователя с id=0.</p>
     *
     * @param message отправляемое сообщение.
     */
    public void sendMessageToLogic(@NotNull LocalMessage message) {
        sendMessageToLogic(0L, message);
    }
}

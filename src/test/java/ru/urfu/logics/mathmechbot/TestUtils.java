package ru.urfu.logics.mathmechbot;

import java.util.List;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.DummyBot;
import ru.urfu.logics.LogicCore;

/**
 * Утилиты для тестирования матмех бота. В идеале должен быть статичным.
 */
public final class TestUtils {
    private final LogicCore logic;
    private final DummyBot bot;

    private final static String ACCEPT_COMMAND = "/yes";
    private final static String INFO_COMMAND = "/info";
    private final static String REGISTER_COMMAND = "/register";

    private final LocalMessage acceptMessage = new LocalMessageBuilder().text(ACCEPT_COMMAND).build();

    private final LocalMessage infoMessage = new LocalMessageBuilder().text(INFO_COMMAND).build();
    private final LocalMessage registerMessage = new LocalMessageBuilder().text(REGISTER_COMMAND).build();

    /**
     * Конструктор.
     *
     * @param logic ядро
     * @param bot   фейк-бот.
     */
    public TestUtils(MathMechBotCore logic, DummyBot bot) {
        this.logic = logic;
        this.bot = bot;
    }

    /**
     * Регистрирует человека со следующими данными.
     * Нужна для быстрых тестов команд, где требуется зарегистрированный пользователь.
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
}

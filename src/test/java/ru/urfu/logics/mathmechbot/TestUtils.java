package ru.urfu.logics.mathmechbot;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.localobjects.Request;
import ru.urfu.logics.DummyBot;
import ru.urfu.logics.LogicCore;

/**
 * Утилиты для тестирования матмех бота. В идеале должен быть статичным.
 */
public final class TestUtils {
    private final LogicCore logic;
    private final DummyBot bot;

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
        final ArrayList<LocalMessage> messages = new ArrayList<>(List.of(
                TestConstants.REGISTER_MESSAGE,
                new LocalMessageBuilder().text(fullName).build(),
                new LocalMessageBuilder().text(String.valueOf(year)).build(),
                new LocalMessageBuilder().text(specialty).build(),
                new LocalMessageBuilder().text(String.valueOf(group)).build(),
                new LocalMessageBuilder().text(men).build(),
                TestConstants.ACCEPT_MESSAGE,
                TestConstants.INFO_MESSAGE
        ));

        for (final LocalMessage message : messages) {
            logic.processMessage(makeRequestFromMessage(message, id));
        }
        bot.getOutcomingMessageList().clear();
    }

    /**
     * Оборачивает сообщение в Request с DummyBot и id=0L.
     * @param message сообщение.
     * @return запрос.
     */
    public Request makeRequestFromMessage(@NotNull LocalMessage message) {
        return makeRequestFromMessage(message, 0L);
    }

    /**
     * Оборачивает сообщение в Request с DummyBot и переданным id.
     * @param message сообщение.
     * @param id идентификатор пользователя.
     * @return запрос.
     */
    public Request makeRequestFromMessage(@NotNull LocalMessage message, long id) {
        return new Request(id, message, bot);
    }
}

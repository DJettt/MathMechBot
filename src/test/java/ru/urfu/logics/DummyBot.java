package ru.urfu.logics;

import java.util.List;
import java.util.Stack;
import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;

/**
 * Ничего просто сохраняющий сообщения. Создан для тестов.
 */
public final class DummyBot implements Bot {
    final private List<LocalMessage> outcomingMessageList;

    /**
     * Конструктор.
     */
    public DummyBot() {
        outcomingMessageList = new Stack<>();
    }

    /**
     * Геттер списка отправленных сообщений.
     *
     * @return список отправленных сообщений.
     */
    public List<LocalMessage> getOutcomingMessageList() {
        return outcomingMessageList;
    }

    @Override
    public void start() {}

    @Override
    public void sendMessage(@NotNull LocalMessage msg, @NotNull Long id) {
        outcomingMessageList.add(msg);
    }
}

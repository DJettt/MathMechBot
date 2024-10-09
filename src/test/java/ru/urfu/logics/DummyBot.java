package ru.urfu.logics;

import java.util.List;
import java.util.Stack;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;

/**
 * Ничего просто сохраняющий сообщения. Создан для тестов.
 */
public final class DummyBot implements Bot {
    final private List<LocalMessage> outcomingMessageList;

    public DummyBot() {
        outcomingMessageList = new Stack<>();
    }

    public List<LocalMessage> getOutcomingMessageList() {
        return outcomingMessageList;
    }

    @Override
    public void start() {}

    @Override
    public void sendMessage(LocalMessage msg, Long id) {
        outcomingMessageList.add(msg);
    }
}
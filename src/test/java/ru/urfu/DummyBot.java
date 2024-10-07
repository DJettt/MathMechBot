package ru.urfu;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Ничего просто сохраняющий сообщения. Создан для тестов.
 */
public class DummyBot implements Bot {
    final private List<Message> outcomingMessageList;

    public DummyBot() {
        outcomingMessageList = new Stack<>();
    }

    public List<Message> getOutcomingMessageList() {
        return outcomingMessageList;
    }

    @Override
    public void start() {}

    @Override
    public void sendMessage(Message msg, Long id) {
        outcomingMessageList.add(msg);
    }
}

package ru.urfu;

/**
 * Ничего не делающий бот. Создан для тестов.
 */
public class DummyBot implements Bot {
    @Override
    public void start() {}

    @Override
    public void sendMessage(Message msg, Long id) {}

    @Override
    public void sendImages(Message msg, Long id) {}
}

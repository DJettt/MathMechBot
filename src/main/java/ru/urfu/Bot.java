package ru.urfu;

public interface Bot {
    void start();
    void sendMessage(LocalMessage msg, Long id);
}
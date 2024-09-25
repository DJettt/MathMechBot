package ru.urfu;

public interface Bot {
    void start();
    void sendMessage(Message msg, Long id);
}
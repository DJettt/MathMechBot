package ru.urfu;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public final class MessageBuilder {
    private String text;
    private List<File> images;

    public MessageBuilder() {
        text = null;
        images = new ArrayList<>();
    }

    public MessageBuilder text(String text) {
        this.text = text;
        return this;
    }

    public MessageBuilder images(@NotNull List<File> images) {
        this.images = images;
        return this;
    }

    public Message build() {
        return new Message(text, images);
    }
}
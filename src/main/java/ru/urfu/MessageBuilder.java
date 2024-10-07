package ru.urfu;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public final class MessageBuilder {
    private String text;
    private List<BufferedImage> images;

    public MessageBuilder() {
        text = null;
        images = new ArrayList<>();
    }

    public MessageBuilder text(@Nullable String text) {
        this.text = text;
        return this;
    }

    public MessageBuilder images(@NotNull List<BufferedImage> images) {
        this.images = images;
        return this;
    }

    public Message build() {
        return new Message(text, images);
    }
}
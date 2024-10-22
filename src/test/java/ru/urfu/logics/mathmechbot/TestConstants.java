package ru.urfu.logics.mathmechbot;

import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;

/**
 * Константы для теста матмех-бота.
 */
public final class TestConstants {
    public final static String ACCEPT_COMMAND = "/yes";
    public final static String INFO_COMMAND = "/info";
    public final static String REGISTER_COMMAND = "/register";

    public final LocalMessage acceptMessage = new LocalMessageBuilder().text(ACCEPT_COMMAND).build();

    public final LocalMessage infoMessage = new LocalMessageBuilder().text(INFO_COMMAND).build();
    public final LocalMessage registerMessage = new LocalMessageBuilder().text(REGISTER_COMMAND).build();
}

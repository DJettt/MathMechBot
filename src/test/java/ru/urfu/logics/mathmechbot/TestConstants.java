package ru.urfu.logics.mathmechbot;

import java.util.ArrayList;
import java.util.List;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;

public final class TestConstants {
    /**
     * Приватный конструктор для класса-утилит.
     */
    private TestConstants() {
    }

    public final static String ACCEPT_COMMAND = "/yes";
    public final static LocalMessage ACCEPT_MESSAGE = new LocalMessageBuilder().text(ACCEPT_COMMAND).build();
    public final static String DECLINE_COMMAND = "/no";
    public final static LocalMessage DECLINE_MESSAGE = new LocalMessageBuilder().text(DECLINE_COMMAND).build();
    public final static String BACK_COMMAND = "/back";
    public final static LocalMessage BACK_MESSAGE = new LocalMessageBuilder().text(BACK_COMMAND).build();
    public final static LocalButton BACK_BUTTON = new LocalButton("Назад", BACK_COMMAND);
    public final static List<LocalButton> YES_NO_BACK = new ArrayList<>(List.of(
            new LocalButton("Да", ACCEPT_COMMAND),
            new LocalButton("Неа", DECLINE_COMMAND),
            BACK_BUTTON
    ));
    public final static LocalMessage INFO_MESSAGE = new LocalMessageBuilder().text("/info").build();
    public final static LocalMessage DELETE_MESSAGE = new LocalMessageBuilder().text("/delete").build();
    public final static LocalMessage REGISTER_MESSAGE = new LocalMessageBuilder().text("/register").build();
    public final static LocalMessage ASK_FOR_REGISTRATION_MESSAGE = new LocalMessageBuilder()
            .text("Сперва нужно зарегистрироваться.").build();
    public final static LocalMessage TRY_AGAIN = new LocalMessageBuilder()
            .text("Попробуйте снова.").build();
    public final static LocalMessage HELP = new LocalMessageBuilder()
            .text("""
                    /start - начало общения с ботом
                    /help - выводит команды, которые принимает бот
                    /register - регистрация
                    /info - информация о Вас
                    /delete - удалить информацию о Вас""")
            .build();
}

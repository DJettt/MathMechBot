package ru.urfu.logics.mathmechbot;

import java.util.List;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;

/**
 * Константы для теста матмех-бота.
 */
public final class TestConstants {
    public final static String ACCEPT_COMMAND = "/yes";
    public final static String DECLINE_COMMAND = "/no";
    public final static String BACK_COMMAND = "/back";
    public final static String INFO_COMMAND = "/info";
    public final static String DELETE_COMMAND = "/delete";
    public final static String REGISTER_COMMAND = "/register";
    public final static String EDIT_COMMAND = "/edit";

    public final static String EDITING_FULL_NAME_COMMAND = "full_name";
    public final static String EDITING_YEAR_COMMAND = "year";
    public final static String EDITING_SPECIALITY_COMMAND = "speciality";
    public final static String EDITING_GROUP_COMMAND = "number_of_group";
    public final static String EDITING_MEN_COMMAND = "men";

    public final LocalMessage acceptMessage = new LocalMessageBuilder().text(ACCEPT_COMMAND).build();
    public final LocalMessage declineMessage = new LocalMessageBuilder().text(DECLINE_COMMAND).build();
    public final LocalMessage backMessage = new LocalMessageBuilder().text(BACK_COMMAND).build();
    public final LocalButton backButton = new LocalButton("Назад", BACK_COMMAND);

    public final List<LocalButton> yesNoBack = List.of(
            new LocalButton("Да", ACCEPT_COMMAND),
            new LocalButton("Нет", DECLINE_COMMAND),
            backButton
    );

    public final LocalMessage infoMessage = new LocalMessageBuilder().text(INFO_COMMAND).build();
    public final LocalMessage deleteMessage = new LocalMessageBuilder().text(DELETE_COMMAND).build();
    public final LocalMessage registerMessage = new LocalMessageBuilder().text(REGISTER_COMMAND).build();

    public final LocalMessage help = new LocalMessageBuilder()
            .text("""
                    /start - начало общения с ботом
                    /help - выводит команды, которые принимает бот
                    /register - регистрация
                    /info - информация о Вас
                    /edit - изменить информацию
                    /delete - удалить информацию о Вас""")
            .build();
    public final LocalMessage askForRegistration = new LocalMessageBuilder()
            .text("Сперва нужно зарегистрироваться.").build();
    public final LocalMessage tryAgain = new LocalMessageBuilder()
            .text("Попробуйте снова.").build();

    public final LocalMessage editMessage = new LocalMessageBuilder().text(EDIT_COMMAND).build();

    public final LocalMessage editingChooseMessage = new LocalMessageBuilder()
            .text("Что Вы хотите изменить?")
            .buttons(List.of(
                    new LocalButton("ФИО", EDITING_FULL_NAME_COMMAND),
                    new LocalButton("Курс", EDITING_YEAR_COMMAND),
                    new LocalButton("Направление", EDITING_SPECIALITY_COMMAND),
                    new LocalButton("Группа", EDITING_GROUP_COMMAND),
                    new LocalButton("МЕН", EDITING_MEN_COMMAND),
                    new Constants().backButton))
            .build();
}

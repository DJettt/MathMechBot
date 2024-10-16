package ru.urfu.logics.mathmechbot.registration;

import java.util.ArrayList;
import java.util.List;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.TestConstants;

/**
 * <p>Константы для тестирования команды регистрации.</p>
 *
 * <p>SuppressWarnings: считаю, что в случае тестов лучше явно повторить.</p>
 */
@SuppressWarnings("MultipleStringLiterals")
public final class RegistrationConstants {
    /**
     * Приватный конструктор утилит-класса.
     */
    private RegistrationConstants() {
    }

    final static String CONFIRMATION_PREFIX = "Всё верно?\n\n";

    final static LocalMessage ASK_FULL_NAME = new LocalMessageBuilder()
            .text("""
                    Введите свое ФИО в формате:
                    Иванов Артём Иванович
                    Без дополнительных пробелов и с буквой ё, если нужно.""")
            .buttons(new ArrayList<>(List.of(TestConstants.BACK_BUTTON)))
            .build();

    final static LocalMessage ASK_YEAR = new LocalMessageBuilder()
            .text("На каком курсе Вы обучаетесь?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("1 курс", "1"),
                    new LocalButton("2 курс", "2"),
                    new LocalButton("3 курс", "3"),
                    new LocalButton("4 курс", "4"),
                    new LocalButton("5 курс", "5"),
                    new LocalButton("6 курс", "6"),
                    TestConstants.BACK_BUTTON
            )))
            .build();

    final static List<LocalButton> ASK_FIRST_YEAR_SPECIALTY_BUTTONS = new ArrayList<>(
            List.of(
                    new LocalButton("КНМО", "КНМО"),
                    new LocalButton("ММП", "ММП"),
                    new LocalButton("КБ", "КБ"),
                    new LocalButton("ФТ", "ФТ"),
                    TestConstants.BACK_BUTTON
            ));
    final static LocalMessage ASK_FIRST_YEAR_SPECIALTY = new LocalMessageBuilder()
            .text("На каком направлении?")
            .buttons(ASK_FIRST_YEAR_SPECIALTY_BUTTONS)
            .build();

    final static List<LocalButton> ASK_LATER_YEAR_SPECIALTY_BUTTONS = new ArrayList<>(
            List.of(
                    new LocalButton("КН", "КН"),
                    new LocalButton("МО", "МО"),
                    new LocalButton("МХ", "МХ"),
                    new LocalButton("МТ", "МТ"),
                    new LocalButton("ПМ", "ПМ"),
                    new LocalButton("КБ", "КБ"),
                    new LocalButton("ФТ", "ФТ"),
                    TestConstants.BACK_BUTTON
            ));
    final static LocalMessage ASK_LATER_YEAR_SPECIALTY = new LocalMessageBuilder()
            .text("На каком направлении?")
            .buttons(ASK_LATER_YEAR_SPECIALTY_BUTTONS)
            .build();

    final static LocalMessage ASK_GROUP_NUMBER = new LocalMessageBuilder()
            .text("Какая у Вас группа?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("1 группа", "1"),
                    new LocalButton("2 группа", "2"),
                    new LocalButton("3 группа", "3"),
                    new LocalButton("4 группа", "4"),
                    new LocalButton("5 группа", "5"),
                    TestConstants.BACK_BUTTON
            )))
            .build();

    final static LocalMessage ASK_MEN = new LocalMessageBuilder()
            .text("Введите свою академическую группу в формате:\nМЕН-123456")
            .buttons(new ArrayList<>(List.of(TestConstants.BACK_BUTTON)))
            .build();

}

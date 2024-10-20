package ru.urfu.logics.mathmechbot.registration;

import java.util.ArrayList;
import java.util.List;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.TestConstants;

/**
 * <p>Константы для тестирования команды регистрации.</p>
 *
 * <p>SuppressWarnings: считаю, что в случае тестов лучше явно повторить.</p>
 */
@SuppressWarnings("MultipleStringLiterals")
public final class RegistrationConstants {
    final static String CONFIRMATION_PREFIX = "Всё верно?\n\n";

    final LocalMessage askFullName = new LocalMessageBuilder()
            .text("""
                    Введите свое ФИО в формате:
                    Иванов Артём Иванович
                    Без дополнительных пробелов и с буквой ё, если нужно.""")
            .build();

    final LocalMessage askYear = new LocalMessageBuilder()
            .text("На каком курсе Вы обучаетесь?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("1 курс", "1"),
                    new LocalButton("2 курс", "2"),
                    new LocalButton("3 курс", "3"),
                    new LocalButton("4 курс", "4"),
                    new LocalButton("5 курс", "5"),
                    new LocalButton("6 курс", "6"),
                    new TestConstants().backButton
            )))
            .build();

    final List<LocalButton> askFirstYearSpecialtyButtons = new ArrayList<>(
            List.of(
                    new LocalButton("КНМО", "КНМО"),
                    new LocalButton("ММП", "ММП"),
                    new LocalButton("КБ", "КБ"),
                    new LocalButton("ФТ", "ФТ"),
                    new TestConstants().backButton
            ));
    final LocalMessage askFirstYearSpecialty = new LocalMessageBuilder()
            .text("На каком направлении?")
            .buttons(askFirstYearSpecialtyButtons)
            .build();

    final List<LocalButton> askLaterYearSpecialtyButtons = new ArrayList<>(
            List.of(
                    new LocalButton("КН", "КН"),
                    new LocalButton("МО", "МО"),
                    new LocalButton("МХ", "МХ"),
                    new LocalButton("МТ", "МТ"),
                    new LocalButton("ПМ", "ПМ"),
                    new LocalButton("КБ", "КБ"),
                    new LocalButton("ФТ", "ФТ"),
                    new TestConstants().backButton
            ));
    final LocalMessage askLaterYearSpecialty = new LocalMessageBuilder()
            .text("На каком направлении?")
            .buttons(askLaterYearSpecialtyButtons)
            .build();

    final LocalMessage askGroupNumber = new LocalMessageBuilder()
            .text("Какая у Вас группа?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("1 группа", "1"),
                    new LocalButton("2 группа", "2"),
                    new LocalButton("3 группа", "3"),
                    new LocalButton("4 группа", "4"),
                    new LocalButton("5 группа", "5"),
                    new TestConstants().backButton
            )))
            .build();

    final LocalMessage askMen = new LocalMessageBuilder()
            .text("Введите свою академическую группу в формате:\nМЕН-123456")
            .buttons(new ArrayList<>(List.of(new TestConstants().backButton)))
            .build();

}

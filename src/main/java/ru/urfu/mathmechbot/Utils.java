package ru.urfu.mathmechbot;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.mathmechbot.models.Specialty;

/**
 * <p>Набор утилит для MathMechBot.</p>
 */
public final class Utils {
    /**
     * <p>Создаёт список, состоящий из кнопок "Да", "Нет"
     * и всех дополнительно переданных. Выделено в отдельный
     * метод, так как подобный набор кнопок используется во
     * многих сообщениях, независимо от состояния.</p>
     *
     * @param buttons дополнительные кнопки, которые нужно добавить к списку.
     * @return список, состоящий из кнопок "Да", "Нет" и всех дополнительно переданных.
     */
    @NotNull
    public List<LocalButton> makeYesNoButtons(@NotNull LocalButton... buttons) {
        final LocalButton yesButton = new LocalButton("Да", Constants.ACCEPT_COMMAND);
        final LocalButton noButton = new LocalButton("Нет", Constants.DECLINE_COMMAND);
        final List<LocalButton> buttonsList = new ArrayList<>(List.of(yesButton, noButton));
        buttonsList.addAll(List.of(buttons));
        return buttonsList;
    }

    /**
     * <p>Создаёт кнопку "Назад", которая используется
     * в большинстве сообщений для перехода на прежнее состояние.</p>
     *
     * @return кнопку "Назад".
     */
    @NotNull
    public LocalButton makeBackButton() {
        return new LocalButton("Назад", Constants.BACK_COMMAND);
    }

    /**
     * <p>Возвращает список разрешённых
     * специальностей для данного года обучения.</p>
     *
     * @param year год обучения данного студента.
     * @return список разрешённых специальностей.
     */
    @NotNull
    public List<Specialty> getAllowedSpecialties(int year) {
        if (year == 1) {
            return List.of(Specialty.KNMO, Specialty.MMP,
                    Specialty.KB, Specialty.FT);
        }
        return List.of(Specialty.KN, Specialty.MO, Specialty.MH,
                Specialty.MT, Specialty.PM, Specialty.KB, Specialty.FT);
    }
}

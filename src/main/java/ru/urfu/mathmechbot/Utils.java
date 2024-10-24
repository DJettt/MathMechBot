package ru.urfu.mathmechbot;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.urfu.mathmechbot.models.Specialty;

/**
 * <p>Набор утилит для MathMechBot.</p>
 */
public final class Utils {
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

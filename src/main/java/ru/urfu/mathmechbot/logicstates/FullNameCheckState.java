package ru.urfu.mathmechbot.logicstates;


import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;


/**
 * <p>Проверяет корректность ФИО.</p>
 */
public final class FullNameCheckState extends DataCheckState {
    private final static Pattern VALID_FULL_NAME_PATTERN =
            Pattern.compile("^\\s*[А-ЯЁ][а-яё]+\\s+[А-ЯЁ][а-яё]+(\\s+[А-ЯЁ][а-яё]+)?\\s*$");

    @Override
    public boolean validateData(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        // TODO: Проверить более сложные имена, содержащие дефисы или несколько слов.
        if (request.message().text() == null) {
            return false;
        }
        return VALID_FULL_NAME_PATTERN.matcher(request.message().text()).matches();
    }
}

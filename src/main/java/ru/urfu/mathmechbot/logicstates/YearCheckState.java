package ru.urfu.mathmechbot.logicstates;


import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;


/**
 * <p>Проверяет корректность года обучения.</p>
 */
public final class YearCheckState extends DataCheckState {
    private final static Pattern VALID_YEAR_STRING_PATTERN = Pattern.compile("^[1-6]$");

    @Override
    protected boolean validateData(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        if (request.message().text() == null) {
            return false;
        }
        return VALID_YEAR_STRING_PATTERN.matcher(request.message().text()).matches();
    }
}

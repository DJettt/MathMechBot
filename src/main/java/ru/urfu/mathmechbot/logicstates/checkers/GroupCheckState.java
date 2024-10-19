package ru.urfu.mathmechbot.logicstates.checkers;


import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.logicstates.DataCheckState;


/**
 * <p>Проверяет корректность группы.</p>
 */
public final class GroupCheckState extends DataCheckState {
    private final static Pattern VALID_GROUP_STRING_PATTERN = Pattern.compile("^[1-5]$");

    @Override
    protected boolean validateData(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        if (request.message().text() == null) {
            return false;
        }
        return VALID_GROUP_STRING_PATTERN.matcher(request.message().text()).matches();
    }
}

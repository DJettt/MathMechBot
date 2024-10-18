package ru.urfu.mathmechbot.logicstates;

import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.MMBCore;


/**
 * <p>Проверяет корректность МЕН-группы.</p>
 */
public final class MenCheckState extends DataCheckState {
    private final static Pattern VALID_MEN_GROUP_STRING = Pattern.compile("^\\s*МЕН-\\d{6}\\s*$");

    @Override
    protected boolean validateData(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        if (request.message().text() == null) {
            return false;
        }
        return VALID_MEN_GROUP_STRING.matcher(request.message().text()).matches();
    }
}

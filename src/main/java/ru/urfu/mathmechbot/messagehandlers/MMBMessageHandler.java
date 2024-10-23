package ru.urfu.mathmechbot.messagehandlers;

import ru.urfu.logics.MessageHandler;
import ru.urfu.logics.RequestEvent;
import ru.urfu.mathmechbot.MMBCore;

/**
 * <p>Интерфейс состояний для MathMechBot.</p>
 */
public interface MMBMessageHandler
        extends MessageHandler<MMBCore, RequestEvent<MMBCore>> {
}

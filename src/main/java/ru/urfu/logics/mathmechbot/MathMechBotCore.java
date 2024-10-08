package ru.urfu.logics.mathmechbot;

import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.State;
import ru.urfu.models.User;
import ru.urfu.models.UserEntry;
import ru.urfu.storages.ArrayStorage;
import ru.urfu.storages.Storage;


/**
 * Логическое ядро бота, парсящего каналы в Telegram на предмет упоминания студентов.
 * На данный момент просто сохраняет информацию о тех пользователях, чьи упоминания надо искать.
 */
public class MathMechBotCore implements LogicCore {
    private State currentState;

    final Storage<User, Long> users;
    final Storage<UserEntry, Long> userEntries;

    /**
     * Конструктор.
     */
    public MathMechBotCore() {
        currentState = new DefaultState(this);
        users = new ArrayStorage<>();
        userEntries = new ArrayStorage<>();
    }

    void changeState(State state) {
        currentState = state;
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        currentState.processMessage(msg, chatId, bot);
    }
}

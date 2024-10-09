package ru.urfu.enums;

public enum DefaultState implements State {
    DEFAULT;

    @Override
    public Class<? extends ru.urfu.logics.State> stateClass() {
        return ru.urfu.logics.mathmechbot.DefaultState.class;
    }
}

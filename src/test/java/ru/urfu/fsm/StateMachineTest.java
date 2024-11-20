package ru.urfu.fsm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * <p>Тесты для конечного автомата.</p>
 */
@DisplayName("Конечный автомат")
public final class StateMachineTest {
    private final static String LOCKED = "Locked";
    private final static String UNLOCKED = "Unlocked";
    private final static String PUSH = "Push";
    private final static String COIN = "Coin";

    private StateMachine<String, String, List<String>> fsm;

    /**
     * <p>Создаём автомат из распространённого примера с
     * турникетом (можно прочитать о нём на англоязычной вики).</p>
     *
     * <p>У турникета два состояния: закрытое и открытое</p>
     *
     * <p>Существует два события: бросить монетку и толкнуть турникет</p>
     *
     * <p>Существует четыре перехода:</p>
     * <ul>
     *     <li>Если толкнуть закрытый турникет, он останется закрытым.</li>
     *     <li>Если бросить монетку в закрытый турникет, он откроется.</li>
     *     <li>Если бросить монетку в открытый турникет, он останется открытым.</li>
     *     <li>Если толкнуть открытый турникет, он закроется.</li>
     * </ul>
     */
    @BeforeEach
    void setupTest() {
        final Set<String> states = new HashSet<>(List.of(LOCKED, UNLOCKED));
        this.fsm = new StateMachineImpl<>(states, LOCKED);

        fsm.registerTransition(new TransitionBuilder<String, String, List<String>>()
                .source(LOCKED).target(LOCKED).event(PUSH).build());
        fsm.registerTransition(new TransitionBuilder<String, String, List<String>>()
                .source(LOCKED).target(UNLOCKED).event(COIN).build());
        fsm.registerTransition(new TransitionBuilder<String, String, List<String>>()
                .source(UNLOCKED).target(UNLOCKED).event(COIN).build());
        fsm.registerTransition(new TransitionBuilder<String, String, List<String>>()
                .source(UNLOCKED).target(LOCKED).event(PUSH).build());
    }


    /**
     * <p>Тестируем корректность работы всех переходов.</p>
     */
    @Test
    @DisplayName("Корректность работы переходов")
    void testTransitions() {
        fsm.sendEvent(COIN, List.of());
        Assertions.assertEquals(UNLOCKED, fsm.getState());

        fsm.sendEvent(COIN, List.of());
        Assertions.assertEquals(UNLOCKED, fsm.getState());

        fsm.sendEvent(PUSH, List.of());
        Assertions.assertEquals(LOCKED, fsm.getState());

        fsm.sendEvent(PUSH, List.of());
        Assertions.assertEquals(LOCKED, fsm.getState());
    }

    /**
     * <p>Тестируем возникновение события, для которого нет
     * перехода. Состояние автомата измениться не должно.</p>
     */
    @Test
    @DisplayName("Событие без перехода")
    void testUnknownEvent() {
        final String unknownEvent = "Unknown";
        fsm.sendEvent(unknownEvent, List.of());
        Assertions.assertEquals(LOCKED, fsm.getState());
    }

    /**
     * <p>Тестируем работу действий на переходах.</p>
     *
     * <ol>
     *     <li>Создаём новое действие, которое будет ложить строку
     *     "Hack" в массив строк, выступающий в роли контекста событий.</li>
     *     <li>Добавляем переход из LOCKED в UNLOCKED под действием события Hack,
     *     с созданным ранее действием.</li>
     *     <li>Отправляем в автомат событие Hack.</li>
     *     <li>Проверяем, что состояние автомата сменилось.</li>
     *     <li>Проверяем, что действие при переходе выполнилось.</li>
     * </ol>
     */
    @Test
    @DisplayName("Действия")
    void testActions() {
        final String hack = "Hack";
        final Action<List<String>> hackAction = context -> context.add(hack);

        fsm.registerTransition(new TransitionBuilder<String, String, List<String>>()
                .source(LOCKED)
                .target(UNLOCKED)
                .event(hack)
                .action(hackAction)
                .build());

        final List<String> strings = new ArrayList<>();
        fsm.sendEvent(hack, strings);

        Assertions.assertEquals(UNLOCKED, fsm.getState());
        Assertions.assertIterableEquals(List.of(hack), strings);
    }

    /**
     * <p>Тестируем добавление в автомат некорректно определённых
     * переходов, которые должны вызвать исключение.</p>
     *
     * <ol>
     *     <li>Исходное состояние не содержится в множестве состояний автомата.</li>
     *     <li>Целевое состояние не содержится в множестве состояний автомата.</li>
     * </ol>
     */
    @Test
    @DisplayName("Добавление некорректно определённого перехода")
    void testAddInvalidTransition() {
        final String invalid = "Invalid";

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> fsm.registerTransition(
                        new TransitionBuilder<String, String, List<String>>()
                                .source(invalid)
                                .target(LOCKED)
                                .event(COIN)
                                .build()));

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> fsm.registerTransition(
                        new TransitionBuilder<String, String, List<String>>()
                                .source(LOCKED)
                                .target(invalid)
                                .event(COIN)
                                .build()));
    }
}

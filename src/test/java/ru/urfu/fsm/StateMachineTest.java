package ru.urfu.fsm;

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
public class StateMachineTest {
    private final static String LOCKED = "Locked";
    private final static String UNLOCKED = "Unlocked";
    private final static String PUSH = "Push";
    private final static String COIN = "Coin";

    private StateMachine<String, String, String> fsm;

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

        fsm.registerTransition(new TransitionBuilder<String, String, String>()
                .source(LOCKED).target(LOCKED).event(PUSH).build());
        fsm.registerTransition(new TransitionBuilder<String, String, String>()
                .source(LOCKED).target(UNLOCKED).event(COIN).build());
        fsm.registerTransition(new TransitionBuilder<String, String, String>()
                .source(UNLOCKED).target(UNLOCKED).event(COIN).build());
        fsm.registerTransition(new TransitionBuilder<String, String, String>()
                .source(UNLOCKED).target(LOCKED).event(PUSH).build());
    }


    /**
     * <p>Тестируем корректность работы всех переходов.</p>
     */
    @Test
    @DisplayName("Корректность работы переходов")
    void testTransitions() {
        fsm.sendEvent(COIN, "");
        Assertions.assertEquals(UNLOCKED, fsm.getState());

        fsm.sendEvent(COIN, "");
        Assertions.assertEquals(UNLOCKED, fsm.getState());

        fsm.sendEvent(PUSH, "");
        Assertions.assertEquals(LOCKED, fsm.getState());

        fsm.sendEvent(PUSH, "");
        Assertions.assertEquals(LOCKED, fsm.getState());
    }

    /**
     * <p>Тестируем добавление в автомат некорректно определённых переходов.</p>
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
                () -> fsm.registerTransition(new TransitionBuilder<String, String, String>()
                        .source(invalid)
                        .target(LOCKED)
                        .event(COIN)
                        .build()));

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> fsm.registerTransition(new TransitionBuilder<String, String, String>()
                        .source(LOCKED)
                        .target(invalid)
                        .event(COIN)
                        .build()));
    }
}

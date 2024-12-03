package ru.urfu.mathmechbot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.urfu.logics.localobjects.LocalMessage;

/**
 * Тестирует корректный вывод дневного расписания
 * в зависимости от того, какие данные туда поступают.
 */
public class DailyTimetableTest {
    private DummyBot bot;
    private TestUtils utils;
    private final static String GET_TIMETABLE_COMMAND = "/timetable";
    private final static String NO_TIMETABLE_TEST = "Расписание для указанной "
            + "группы не найдено. Проверьте корректность введённой группы в формате МЕН.";
    @SuppressWarnings("RegexpSingleline")
    private final static String STANDARD_OUT_CHECK_TEST = """
            1.   Английский язык
                     практика
                     1 Пушкина 1
            
            2.   ---
            
            3.   Питон
                     совмещенные занятия
                     Мира 1
            
               Физкультура
                     стадион
            
               Философия
                     рассуждение
                     под звездным небом
            
            4.   Матанализ
                     лекция
            
               История
            
            5.   ---
            
            6.   ООП
            
               АрхЭВМ
                     разборка процессоров
            
            """;
    @SuppressWarnings("RegexpSingleline")
    private final static String WITH_LESSON_WITHOUT_TIME_TEST = """
            1.   Физкультура
                     стадион
            
            2.   ---
            
            3.   Философия
                     рассуждение
                     под звездным небом
            
               Матанализ
                     лекция
            
            4.   ---
            
            5.   История
            
               ООП
            
            
            Занятия без точного времени:
            
               Английский язык
                     практика
                     1 Пушкина 1
            
               Питон
                     совмещенные занятия
                     Мира 1
            
               АрхЭВМ
                     разборка процессоров
            
            """;
    @SuppressWarnings("RegexpSingleline")
    private final static String ONLY_LESSONS_WITHOUT_NUMBER_TEST = """
            
            Занятия без точного времени:
            
               Английский язык
                     практика
                     1 Пушкина 1
            
               Питон
                     совмещенные занятия
                     Мира 1
            
               Физкультура
                     стадион
            
               Философия
                     рассуждение
                     под звездным небом
            
               Матанализ
                     лекция
            
               История
            
               ООП
            
               АрхЭВМ
                     разборка процессоров
            
            """;

    /**
     * Устанавливает базовые настройки для тестов: объект логики, ложного бота и утилиты.
     */
    @BeforeEach
    void setupTest() {
        final MMBCore logic = new MMBCore(new MathMechArrayStorage(), new TimetableStubFactory());
        bot = new DummyBot();
        utils = new TestUtils(logic, bot);
    }

    /**
     * Тестирует обычное расписание, когда у каждой пары есть порядковый номер.
     * Проверка идет через сообщение, которое "отправил" DummyBot.
     */
    @Test
    @DisplayName("Тест обычного расписания.")
    void testStandard() {
        utils.registerUser(1L, "Иванов Иван", 2, "КН", 1, "МЕН-111111");
        utils.sendMessageToLogic(1L, new LocalMessage(GET_TIMETABLE_COMMAND));
        Assertions.assertEquals(STANDARD_OUT_CHECK_TEST, bot.getOutcomingMessageList().getLast().text());
    }

    /**
     * Тестирует расписание, когда не у каждой пары есть порядковый номер.
     * Проверка идет через сообщение, которое "отправил" DummyBot.
     */
    @Test
    @DisplayName("Тест расписания пар с и без порядковых номеров.")
    void testWithLessonWithoutTime() {
        utils.registerUser(1L, "Петров Пётр", 2, "ФТ", 1, "МЕН-222222");
        utils.sendMessageToLogic(1L, new LocalMessage(GET_TIMETABLE_COMMAND));
        Assertions.assertEquals(WITH_LESSON_WITHOUT_TIME_TEST, bot.getOutcomingMessageList().getLast().text());
    }

    /**
     * Тестирует обычное расписание, когда у каждой пары нет порядкового номера.
     * Проверка идет через сообщение, которое "отправил" DummyBot.
     */
    @Test
    @DisplayName("Тест расписания пар без порядковых номеров.")
    void testOnlyLessonsWithoutNumber() {
        utils.registerUser(1L, "Соколов Сокол", 2, "КН", 1, "МЕН-333333");
        utils.sendMessageToLogic(1L, new LocalMessage(GET_TIMETABLE_COMMAND));
        Assertions.assertEquals(ONLY_LESSONS_WITHOUT_NUMBER_TEST, bot.getOutcomingMessageList().getLast().text());
    }

    /**
     * Тестируем тот случай, когда для какой-то группы никакого расписания не нашлось.
     */
    @Test
    @DisplayName("Такой группы нет.")
    void testNotExist() {
        utils.registerUser(1L, "Яесть Грут", 2, "ФТ", 1, "МЕН-444444");
        utils.sendMessageToLogic(1L, new LocalMessage(GET_TIMETABLE_COMMAND));
        Assertions.assertEquals(NO_TIMETABLE_TEST, bot.getOutcomingMessageList().getLast().text());
    }
}

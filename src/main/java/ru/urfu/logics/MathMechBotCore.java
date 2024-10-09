package ru.urfu.logics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import ru.urfu.bots.Bot;
import ru.urfu.enums.Specialty;
import ru.urfu.enums.processes.DeletionProcessState;
import ru.urfu.enums.processes.Process;
import ru.urfu.enums.processes.RegistrationProcessState;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.models.User;
import ru.urfu.models.UserEntry;
import ru.urfu.storages.ArrayStorage;
import ru.urfu.storages.Storage;

/**
 * Логическое ядро бота, парсящего каналы в Telegram на предмет упоминания студентов.
 * На данный момент просто сохраняет информацию о тех пользователях, чьи упоминания надо искать.
 */
public class MathMechBotCore implements LogicCore {
    final static String START_COMMAND = "/start";
    final static String HELP_COMMAND = "/help";
    final static String REGISTER_COMMAND = "/register";
    final static String INFO_COMMAND = "/info";
    final static String EDIT_COMMAND = "/edit";
    final static String DELETE_COMMAND = "/delete";
    final static String BACK_COMMAND = "/back";
    final static List<List<String>> REGISTRATION_COMMAND_CENTER = Arrays.asList(
            Arrays.asList("-"),
            Arrays.asList("1", "2", "3", "4", "back_button"),
            Arrays.asList("КНМО", "ММП", "КБ", "ФТ", "back_button"),
            Arrays.asList("КН", "МО", "МХ", "МТ", "ПМ", "ФТ", "КБ", "back_button"),
            Arrays.asList("1", "2", "3", "4", "5",  "back_button")
            );

    final static String USER_INFO_TEMPLATE = """
            ФИО: %s
            Направление: %s
            Курс: %s
            Группа: %s
            Академ. группа: %s
            """;

    final Storage<User, Long> users;
    final Storage<UserEntry, Long> userEntries;

    /**
     * Конструктор.
     */
    public MathMechBotCore() {
        users = new ArrayStorage<>();
        userEntries = new ArrayStorage<>();
    }


    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        if (msg.text() == null) {
            return;
        }

        switch (msg.text()) {
            case START_COMMAND, HELP_COMMAND -> helpCommandHandler(msg, chatId, bot);
            case REGISTER_COMMAND -> registerCommandHandler(msg, chatId, bot);
            case INFO_COMMAND -> infoCommandHandler(msg, chatId, bot);
            case EDIT_COMMAND -> editCommandHandler(msg, chatId, bot);
            case DELETE_COMMAND -> deleteCommandHandler(msg, chatId, bot);
            case BACK_COMMAND -> backCommandHandler(msg, chatId, bot);
            default -> defaultHandler(msg, chatId, bot);
        }
    }

    /**
     * Обрабатывает сообщения, не распознанные как заявленные команды.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void defaultHandler(LocalMessage inputMessage, long chatId, Bot bot) {
        final User user = users.getById(chatId);
        if (user == null) {
            bot.sendMessage(new LocalMessageBuilder()
                    .text("Сперва зарегистрируйтесь с помощью команды /register")
                    .build(), chatId);
            return;
        }

        switch (user.currentProcess()) {
            case Process.REGISTRATION -> registerCommandHandler(inputMessage, chatId, bot);
            case Process.DELETION -> deleteCommandHandler(inputMessage, chatId, bot);
            case Process.EDITION -> editCommandHandler(inputMessage, chatId, bot);
            default -> {
                LocalMessage msg = new LocalMessage("Извините, произошла непредвиденная ошибка.",
                        null);
                bot.sendMessage(msg, chatId);
            }
        }
    }

    /**
     * Подтверждает корректность введенного ФИО.
     * @param str ФИО (или ФИ)
     * @return true - все верно, false - это не ФИО.
     */
    @SuppressWarnings("ReturnCount")
    public boolean correctName(String str) {
        if (str.isEmpty()) {
            return false;
        }
        int spaceCount = 0;

        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                spaceCount++;
                if (i + 1 < str.length() && !Character.isUpperCase(str.charAt(i + 1))) {
                    return false;
                }
            } else if (Character.isUpperCase(str.charAt(i)) && str.charAt(i - 1) != ' ') {
                return false;
            }
        }
        if (!Character.isUpperCase(str.charAt(0)) || spaceCount > 2 || spaceCount < 0) {
            return false;
        }
        return true;
    }

    /**
     * Проверяет корректность строки МЕН которую отправил пользователь.
     * @param input строка от пользователя
     * @return true - все верно, false - не все верно...
     */
    @SuppressWarnings("MagicNumber")
    public boolean correctMen(String input) {
        if (!input.startsWith("МЕН") || input.charAt(3) != '-' || input.length() != 10) {
            return false;
        }
        for (int i = 4; i < 10; i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверка является ли полученное сообщение одной из ожидаемых команд.
     * @param index индекс списка команд
     * @param command полученный текст
     * @return да или нет
     */
    public boolean isCommand(int index, String command) {
        List<String> specificList = REGISTRATION_COMMAND_CENTER.get(index);
        for (String str : specificList) {
            if (Objects.equals(str, command)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Обрабатывает команды пользователя при регистрации.
     * @param inputMessage команда
     * @param chatId id чата
     * @param bot бот в котором пришло сообщение
     */
    private void registerCommandCatcher(LocalMessage inputMessage, long chatId, Bot bot) {
        switch (users.getById(chatId).currentState()) {
            case RegistrationProcessState.NAME -> {
                if (correctName(inputMessage.text())) {
                    userEntries.getById(chatId).setName(inputMessage.text());
                    users.getById(chatId).setCurrentState(RegistrationProcessState.YEAR);
                } else if (!inputMessage.text().equals(REGISTER_COMMAND)) {
                    bot.sendMessage(new LocalMessageBuilder()
                            .text("Некорректный формат ФИО.").build(), chatId);
                }
            }
            case RegistrationProcessState.YEAR -> {
                if (isCommand(1, inputMessage.text())) {
                    if (Objects.equals(inputMessage.text(), "back_button")) {
                        userEntries.getById(chatId).setName(null);
                        users.getById(chatId).setCurrentState(RegistrationProcessState.NAME);
                    } else {
                        userEntries.getById(chatId).setYear(inputMessage.text());
                        if (Objects.equals(inputMessage.text(), "1")) {
                            users.getById(chatId).setCurrentState(RegistrationProcessState.SPECIALTY1);
                        } else {
                            users.getById(chatId).setCurrentState(RegistrationProcessState.SPECIALTY2);
                        }
                    }
                } else {
                    bot.sendMessage(new LocalMessageBuilder()
                            .text("Некорректный формат курса.").build(), chatId);
                }
            }
            case RegistrationProcessState.SPECIALTY1 -> {
                switch (inputMessage.text()) {
                    case "КНМО" -> {
                        userEntries.getById(chatId).setSpecialty(Specialty.KNMO);
                        users.getById(chatId).setCurrentState(RegistrationProcessState.GROUP);
                    }
                    case "ММП" -> {
                        userEntries.getById(chatId).setSpecialty(Specialty.MMP);
                        users.getById(chatId).setCurrentState(RegistrationProcessState.GROUP);
                    }
                    case "КБ" -> {
                        userEntries.getById(chatId).setSpecialty(Specialty.KB);
                        users.getById(chatId).setCurrentState(RegistrationProcessState.GROUP);
                    }
                    case "ФТ" -> {
                        userEntries.getById(chatId).setSpecialty(Specialty.FT);
                        users.getById(chatId).setCurrentState(RegistrationProcessState.GROUP);
                    }
                    case "back_button" -> {
                        userEntries.getById(chatId).setYear(null);
                        users.getById(chatId).setCurrentState(RegistrationProcessState.YEAR);
                    }
                    default -> bot.sendMessage(new LocalMessageBuilder()
                            .text("Некорректный формат направления.").build(), chatId);
                }
            }
            case RegistrationProcessState.SPECIALTY2 -> {
                switch (inputMessage.text()) {
                    case "КН" -> {
                        userEntries.getById(chatId).setSpecialty(Specialty.KN);
                        users.getById(chatId).setCurrentState(RegistrationProcessState.GROUP);
                    }
                    case "МО" -> {
                        userEntries.getById(chatId).setSpecialty(Specialty.MO);
                        users.getById(chatId).setCurrentState(RegistrationProcessState.GROUP);
                    }
                    case "МХ" -> {
                        userEntries.getById(chatId).setSpecialty(Specialty.MH);
                        users.getById(chatId).setCurrentState(RegistrationProcessState.GROUP);
                    }
                    case "МТ" -> {
                        userEntries.getById(chatId).setSpecialty(Specialty.MT);
                        users.getById(chatId).setCurrentState(RegistrationProcessState.GROUP);
                    }
                    case "ПМ" -> {
                        userEntries.getById(chatId).setSpecialty(Specialty.PM);
                        users.getById(chatId).setCurrentState(RegistrationProcessState.GROUP);
                    }
                    case "КБ" -> {
                        userEntries.getById(chatId).setSpecialty(Specialty.KB);
                        users.getById(chatId).setCurrentState(RegistrationProcessState.GROUP);
                    }
                    case "ФТ" -> {
                        userEntries.getById(chatId).setSpecialty(Specialty.FT);
                        users.getById(chatId).setCurrentState(RegistrationProcessState.GROUP);
                    }
                    case "back_button" -> {
                        userEntries.getById(chatId).setYear(null);
                        users.getById(chatId).setCurrentState(RegistrationProcessState.YEAR);
                    }
                    default -> bot.sendMessage(new LocalMessageBuilder()
                            .text("Некорректный формат направления.").build(), chatId);
                }
            }
            case RegistrationProcessState.GROUP -> {
                if (isCommand(4, inputMessage.text())) {
                    if (Objects.equals(inputMessage.text(), "back_button")) {
                        userEntries.getById(chatId).setSpecialty(null);
                        if (Objects.equals(userEntries.getById(chatId).year(), "1")) {
                            users.getById(chatId).setCurrentState(RegistrationProcessState.SPECIALTY1);
                        } else {
                            users.getById(chatId).setCurrentState(RegistrationProcessState.SPECIALTY2);
                        }
                    } else {
                        userEntries.getById(chatId).setGroup(inputMessage.text());
                        users.getById(chatId).setCurrentState(RegistrationProcessState.MEN);
                    }
                } else {
                    bot.sendMessage(new LocalMessageBuilder()
                            .text("Некорректный формат группы.").build(), chatId);
                }
            }
            case RegistrationProcessState.MEN -> {
                if (inputMessage.text().equals("back_button")) {
                    userEntries.getById(chatId).setGroup(null);
                    users.getById(chatId).setCurrentState(RegistrationProcessState.GROUP);
                } else if (correctMen(inputMessage.text())) {
                    userEntries.getById(chatId).setMen(inputMessage.text());
                    users.getById(chatId).setCurrentState(RegistrationProcessState.CONFIRMATION);
                } else {
                    bot.sendMessage(new LocalMessageBuilder()
                            .text("Некорректный формат строки МЕН-123456.").build(), chatId);
                }
            }
            default -> {
                if (inputMessage.text().equals("confirm_yes")) {
                    bot.sendMessage(new LocalMessageBuilder()
                            .text("Ваши данные сохранены.").build(), chatId);
                    users.getById(chatId).setCurrentState(null);
                    users.getById(chatId).setCurrentProcess(null);
                } else if (inputMessage.text().equals("confirm_no")) {
                    users.deleteById(chatId);
                    userEntries.deleteById(chatId);
                    bot.sendMessage(new LocalMessageBuilder()
                            .text("Давайте начнем все с начала!")
                            .buttons(new ArrayList<>(
                                    List.of(
                                            new LocalButton("Регистрация", REGISTER_COMMAND)
                                    )))
                            .build(), chatId);
                    return;
                } else if (inputMessage.text().equals("back_button")) {
                    userEntries.getById(chatId).setMen(null);
                    users.getById(chatId).setCurrentState(RegistrationProcessState.MEN);
                } else {
                    bot.sendMessage(new LocalMessageBuilder()
                            .text("Некорректный формат ответа.").build(), chatId);
                }
            }
        }
    }

    /**
     * Отправляет команды пользователи при регистрации.
     * @param chatId id чата
     * @param bot бот в котором надо отправить сообщение
     */
    private void registerCommandSender(long chatId, Bot bot) {
        final User user = users.getById(chatId);
        final UserEntry userEntry = userEntries.getById(chatId);
        switch (user.currentState()) {
            case RegistrationProcessState.NAME -> {
                bot.sendMessage(new LocalMessageBuilder()
                        .text("Введите свое ФИО в формате: \nИванов Артём Иванович\nБез дополнительных "
                                + "пробелов и с буквой ё, если нужно.").build(), chatId);
                return;
            }
            case RegistrationProcessState.YEAR -> {
                bot.sendMessage(new LocalMessageBuilder()
                        .text("На каком курсе Вы обучаетесь?")
                        .buttons(new ArrayList<>(
                                List.of(
                                        new LocalButton("1 курс", "1"),
                                        new LocalButton("2 курс", "2"),
                                        new LocalButton("3 курс", "3"),
                                        new LocalButton("4 курс", "4"),
                                        new LocalButton("Назад", "back_button")
                                )))
                        .build(), chatId);
                return;
            }
            case RegistrationProcessState.SPECIALTY1 -> {
                bot.sendMessage(new LocalMessageBuilder()
                        .text("На каком направлении?")
                        .buttons(new ArrayList<>(
                                List.of(
                                        new LocalButton(Specialty.KNMO.getAbbreviation(),
                                                Specialty.KNMO.getAbbreviation()),
                                        new LocalButton(Specialty.MMP.getAbbreviation(),
                                                Specialty.MMP.getAbbreviation()),
                                        new LocalButton(Specialty.KB.getAbbreviation(),
                                                Specialty.KB.getAbbreviation()),
                                        new LocalButton(Specialty.FT.getAbbreviation(),
                                                Specialty.FT.getAbbreviation()),
                                        new LocalButton("Назад", "back_button")
                                )))
                        .build(), chatId);
            }
            case RegistrationProcessState.SPECIALTY2 -> {
                bot.sendMessage(new LocalMessageBuilder()
                        .text("На каком направлении?")
                        .buttons(new ArrayList<>(
                                List.of(
                                        new LocalButton(Specialty.KN.getAbbreviation(), Specialty.KN.getAbbreviation()),
                                        new LocalButton(Specialty.MO.getAbbreviation(), Specialty.MO.getAbbreviation()),
                                        new LocalButton(Specialty.MH.getAbbreviation(), Specialty.MH.getAbbreviation()),
                                        new LocalButton(Specialty.MT.getAbbreviation(), Specialty.MT.getAbbreviation()),
                                        new LocalButton(Specialty.PM.getAbbreviation(), Specialty.PM.getAbbreviation()),
                                        new LocalButton(Specialty.KB.getAbbreviation(), Specialty.KB.getAbbreviation()),
                                        new LocalButton(Specialty.FT.getAbbreviation(), Specialty.FT.getAbbreviation()),
                                        new LocalButton("Назад", "back_button")
                                )))
                        .build(), chatId);
            }
            case RegistrationProcessState.GROUP -> {
                bot.sendMessage(new LocalMessageBuilder()
                        .text("Какая у Вас группа?")
                        .buttons(new ArrayList<>(
                                List.of(
                                        new LocalButton("1 группа", "1"),
                                        new LocalButton("2 группа", "2"),
                                        new LocalButton("3 группа", "3"),
                                        new LocalButton("4 группа", "4"),
                                        new LocalButton("5 группа", "5"),
                                        new LocalButton("Назад", "back_button")
                                )))
                        .build(), chatId);
            }
            case RegistrationProcessState.MEN -> {
                bot.sendMessage(new LocalMessageBuilder()
                        .text("Введите свою академическую группу в формате:\nМЕН-123456")
                        .buttons(new ArrayList<>(
                                List.of(
                                        new LocalButton("Назад", "back_button")
                                )))
                        .build(), chatId);
            }
            case RegistrationProcessState.CONFIRMATION -> {
                bot.sendMessage(new LocalMessageBuilder()
                        .text("Ваша информация:\nФИО: " + userEntry.name() + "\nКурс: " + userEntry.year()
                                + "\nНаправление: " + userEntry.specialty() + "\nГруппа: " + userEntry.group()
                                + "\nАкадемическая группа: " + userEntry.men() + "\nВсе верно?")
                        .buttons(new ArrayList<>(
                                List.of(
                                        new LocalButton("Да", "confirm_yes"),
                                        new LocalButton("Неа", "confirm_no"),
                                        new LocalButton("Назад", "back_button")
                                )))
                        .build(), chatId);
            }
            case null -> {
            }
            default -> {
                bot.sendMessage(new LocalMessageBuilder()
                        .text("У нас что-то сломалось... Давайте попробуем заново?")
                        .buttons(new ArrayList<>(
                                List.of(
                                        new LocalButton("Регистрация", REGISTER_COMMAND)
                                )))
                        .build(), chatId);
            }
        }


    }

    /**
     * Запускает процесс регистрации пользователя.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void registerCommandHandler(LocalMessage inputMessage, long chatId, Bot bot) {
        if (users.getById(chatId) == null) {
            users.add(new User(chatId, chatId, chatId, Process.REGISTRATION, RegistrationProcessState.NAME));
            userEntries.add(new UserEntry(chatId, null, null, null, null, null,
                    null, null, chatId));
        }

        if (inputMessage.text() == null) {
            return;
        }
        if (users.getById(chatId).currentProcess() == null && users.getById(chatId) != null
                && inputMessage.text().equals(REGISTER_COMMAND)) {
            bot.sendMessage(new LocalMessageBuilder()
                    .text("Вы уже зарегистрированы.").build(), chatId);
        }
        registerCommandCatcher(inputMessage, chatId, bot);
        registerCommandSender(chatId, bot);
    }

    /**
     * Отправляет зарегистрированную информацию.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void infoCommandHandler(LocalMessage inputMessage, long chatId, Bot bot) {
        if (users.getById(chatId) == null) {
            bot.sendMessage(new LocalMessageBuilder()
                    .text("Пользователь не зарегистрирован.")
                            .buttons(new ArrayList<>(
                                    List.of(
                                            new LocalButton("Регистрация", REGISTER_COMMAND)
                                    )))
                    .build(), chatId);
        }
        LocalMessage msg;
        UserEntry currentUserEntry = userEntries.getById(chatId);
        if (users.getById(chatId).currentProcess() == null) {
            String userInfo = String.format(USER_INFO_TEMPLATE,
                    currentUserEntry.name(),
                    currentUserEntry.specialty(),
                    currentUserEntry.year(),
                    currentUserEntry.group(),
                    currentUserEntry.men()
            );
            msg = new LocalMessage(userInfo, null);
        } else {
            msg = new LocalMessage("Сейчас просмотр информации невозможен.\n"
                    + "Повторите попытку после завершения текущего действия",
                    null);
        }
        bot.sendMessage(msg, chatId);
    }

    /**
     * Запускает процесс редактирования зарегистрированной информации.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void editCommandHandler(LocalMessage inputMessage, long chatId, Bot bot) {
    }

    /**
     * Запускает процесс удаления зарегистрированной информации.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void deleteCommandHandler(LocalMessage inputMessage, long chatId, Bot bot) {
        final User user = users.getById(chatId);

        if (user == null) {
            bot.sendMessage(new LocalMessageBuilder()
                    .text("Сперва зарегистрируйтесь с помощью команды /register")
                    .build(), chatId);
            return;
        }

        if (user.currentProcess() == null) {
            user.setCurrentProcess(Process.DELETION);
            user.setCurrentState(DeletionProcessState.CONFIRMATION);
            bot.sendMessage(new LocalMessageBuilder()
                    .text("Точно удалить?")
                    .buttons(new ArrayList<>(
                            List.of(
                                    new LocalButton("да", "да"),
                                    new LocalButton("нет", "нет")
                            )))
                    .build(), chatId);
            return;
        } else if (user.currentProcess() != Process.DELETION) {
            bot.sendMessage(new LocalMessageBuilder()
                    .text("Вы находитесь посреди другого процесса, завершите его.")
                    .build(), chatId);
            return;
        }

        final LocalMessageBuilder responseBuilder = new LocalMessageBuilder();
        if (user.currentState() == DeletionProcessState.CONFIRMATION) {
            if (Objects.equals(inputMessage.text(), "да")) {
                // TODO: удаляй нормально
                users.deleteById(chatId);
                userEntries.deleteById(chatId);
                user.setCurrentProcess(null);
                user.setCurrentState(null);
                responseBuilder.text("Удаляю...");
            } else if (Objects.equals(inputMessage.text(), "нет")) {
                user.setCurrentProcess(null);
                user.setCurrentState(null);
                responseBuilder.text("Не удаляю...");
            } else {
                responseBuilder.text("Не понял, повтори.");
            }
        }
        bot.sendMessage(responseBuilder.build(), chatId);
    }

    /**
     * Возвращает пользователя на предыдущий шаг в запущенном процессе.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void backCommandHandler(LocalMessage inputMessage, long chatId, Bot bot) {
    }

    /**
     * Выдаёт справку.
     * @param inputMessage входящее сообщение с командой /help
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void helpCommandHandler(LocalMessage inputMessage, Long chatId, Bot bot) {
        final String HELP_MESSAGE = """
                /start - начало общения с ботом
                /help - выводит команды, которые принимает бот
                /register - регистрация
                /info - посмотреть информацию о себе
                /edit - изменение информации о себе
                /delete - удаление информации о себе
                """;
        final LocalMessage answer = new LocalMessage(HELP_MESSAGE, null);
        bot.sendMessage(answer, chatId);
    }
}

package se1_prog_lab.client;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import se1_prog_lab.client.commands.BaseCommand;
import se1_prog_lab.client.commands.Command;
import se1_prog_lab.client.interfaces.ClientCommandReceiver;
import se1_prog_lab.client.interfaces.CommandRepository;
import se1_prog_lab.client.interfaces.ServerIO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import static se1_prog_lab.util.BetterStrings.multiline;

/**
 * Класс, хранящий команды и возвращающий их по ключу.
 */
@Singleton
public class CommandInvoker implements CommandRepository {
    private final HashMap<String, BaseCommand> commandMap = new HashMap<>(); // HashMap команд
    private final ClientCommandReceiver clientCommandReceiver;
    private final ServerIO serverIO;

    @Inject
    public CommandInvoker(ClientCommandReceiver clientCommandReceiver, ServerIO serverIO, Set<BaseCommand> commands) {
        this.clientCommandReceiver = clientCommandReceiver;
        this.serverIO = serverIO;
        addCommand(commands.toArray(new BaseCommand[0]));
        clientCommandReceiver.setHelpText(multiline(commandMap.values().stream()
                .map(command -> command.getKey() + command.getHelpText()).toArray()));
    }

    /**
     * Добавляет массив команд в карту команд.
     *
     * @param commands команды.
     */
    private void addCommand(BaseCommand... commands) {
        Arrays.stream(commands).forEach(command -> commandMap.put(command.getKey(), command));
    }

    /**
     * По ключу достает команду, выполняет её клиентскую часть, добалвяет в историю и возвращает.
     *
     * @param commandKey ключ команды.
     * @param args       аргументы команды.
     * @return команду или null, если команда неудачно выполнилась на клиенте.
     */
    @Override
    public Command runCommand(String commandKey, String[] args) {
        if (!commandMap.containsKey(commandKey)) {
            System.out.println("Такой команды не существует. Список комманд: help.");
            return null;
        }
        Command command = commandMap.get(commandKey);
        if (!command.clientExecute(args, clientCommandReceiver)) command = null;
        clientCommandReceiver.addToHistory(commandKey); // Записываем в историю команд
        return command; // Возвращаем, чтобы отправить на сервер
    }

    /**
     * Парсит строку на ключ и аргументы и передает в runCommand(), затем возвращает полученную от него команду.
     *
     * @param input строка.
     * @return команду.
     */
    @Override
    public Command parseThenRun(String[] input) {
        String commandKey = input[0]; // Первый аргумент - ключ команды
        String[] ar = Arrays.copyOfRange(input, 1, input.length); // Создаем массив аргументов из старого (кроме 1 аргумента)
        // Передача ключа и аргументов обработчику команд
        return runCommand(commandKey, ar);
    }
}

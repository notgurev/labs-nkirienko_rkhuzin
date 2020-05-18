package se1_prog_lab.client.commands;

/**
 * Базовый класс команды
 */
public abstract class BaseCommand implements Command {
    private final transient String helpText;
    private final transient String key;

    public BaseCommand(String key, String helpText) {
        this.helpText = helpText;
        this.key = key;
    }

    @Override
    public String getHelpText() {
        return helpText;
    }

    @Override
    public String getKey() {
        return key;
    }
}

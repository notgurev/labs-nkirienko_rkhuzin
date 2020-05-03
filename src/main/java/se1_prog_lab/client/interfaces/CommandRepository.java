package se1_prog_lab.client.interfaces;

import se1_prog_lab.client.commands.Command;

public interface CommandRepository {
    Command runCommand(String commandKey, String[] args);

    Command parseThenRun(String[] input);
}

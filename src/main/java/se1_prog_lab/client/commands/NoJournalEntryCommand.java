package se1_prog_lab.client.commands;

public abstract class NoJournalEntryCommand extends BasicCommand {
    @Override
    public final String getJournalEntry() {
        return null;
    }
}

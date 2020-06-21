package se1_prog_lab.client;

import se1_prog_lab.collection.LabWork;

import java.util.Collection;
import java.util.LinkedList;

public class ClientData implements ClientModel {
    private final static int JOURNAL_SIZE_LIMIT = 13;
    private final LinkedList<String> journal = new LinkedList<>(); // Журнал (история) команд
    private Collection<LabWork> bufferedCollectionPage;
    private int pageNumber;
    private int pageSize = 10;

    @Override
    public void addJournalEntry(String entry) {
        if (entry != null) journal.addFirst(entry);
        while (journal.size() > JOURNAL_SIZE_LIMIT) {
            journal.removeLast();
        }
    }

    @Override
    public LinkedList<String> getJournal() {
        return journal;
    }

    @Override
    public Collection<LabWork> getBufferedCollectionPage() {
        return bufferedCollectionPage;
    }

    @Override
    public void setBufferedCollectionPage(Collection<LabWork> bufferedCollectionPage) {
        this.bufferedCollectionPage = bufferedCollectionPage;
    }

    @Override
    public long getPageNumber() {
        return pageNumber;
    }

    @Override
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

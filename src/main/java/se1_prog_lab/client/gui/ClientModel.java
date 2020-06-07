package se1_prog_lab.client.gui;

import se1_prog_lab.collection.LabWork;

import java.util.Collection;
import java.util.LinkedList;

public interface ClientModel {
    LinkedList<String> getJournal();

    Collection<LabWork> getBufferedCollectionPage();

    void setBufferedCollectionPage(Collection<LabWork> bufferedCollectionPage);

    long getPageNumber();

    void setPageNumber(int pageNumber);

    int getPageSize();

    void setPageSize(int pageSize);

    void addJournalEntry(String entry);
}

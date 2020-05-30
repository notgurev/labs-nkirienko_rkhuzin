package se1_prog_lab.client.gui;

import se1_prog_lab.collection.LabWork;

import java.util.Collection;

public interface ClientModel {
    Collection<LabWork> getBufferedCollectionPage();

    void setBufferedCollectionPage(Collection<LabWork> bufferedCollectionPage);

    long getPageNumber();

    void setPageNumber(long pageNumber);

    int getPageSize();

    void setPageSize(int pageSize);
}

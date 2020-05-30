package se1_prog_lab.client.gui;

import se1_prog_lab.collection.LabWork;

import java.util.Collection;

public class ClientData implements ClientModel {
    Collection<LabWork> bufferedCollectionPage;
    int pageNumber;
    int pageSize;

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

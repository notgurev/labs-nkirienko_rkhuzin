package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public interface CollectionWrapper {
    void add(LabWork labWork, long id);

    boolean isEmpty();

    void clear(List<Long> ids);

    void setVector(Vector<LabWork> labWorkVector);

    long countLessThanDescription(String description);

    String getCollectionType();

    int getSize();

    LocalDate getInitDate();

    boolean sort();

    Collection<LabWork> getListOfAllElements();

    Set<Integer> getUniqueTunedInWorks();

    Collection<LabWork> filterGreaterThanMinimalPoint(Integer minimalPoint);

    void removeByID(long id);

    void insertAtIndex(LabWork labWork, int index, long id);

    void updateByID(long id, LabWork labWork);

    Collection<LabWork> getCollectionSlice(int firstIndex, int lastIndex);
}

package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public interface CollectionWrapper {
    void add(LabWork labWork, long id);

    boolean isEmpty();

    void clear();

    void clear(List<Long> ids);

    void setVector(Vector<LabWork> labWorkVector);

    long countLessThanDescription(String description);

    String getCollectionType();

    int getSize();

    LocalDate getInitDate();

    boolean sort();

    String showAll();

    Set<Integer> getUniqueTunedInWorks();

    String filterGreaterThanMinimalPoint(Integer minimalPoint);

    void removeByID(long id);

    boolean insertAtIndex(LabWork labWork, int index);

    void updateByID(long id, LabWork labWork);
}

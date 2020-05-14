package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;

import java.time.LocalDate;
import java.util.Set;
import java.util.Vector;

public interface CollectionWrapper {
    boolean add(LabWork labWork);

    boolean isEmpty();

    void clear();

    void setVector(Vector<LabWork> labWorkVector);

    long countLessThanDescription(String description);

    String getCollectionType();

    int getSize();

    LocalDate getInitDate();

    boolean sort();

    String showAll();

    Set<Integer> getUniqueTunedInWorks();

    String filterGreaterThanMinimalPoint(Integer minimalPoint);

    boolean removeByID(long id);

    boolean insertAtIndex(LabWork labWork, int index);

    boolean updateByID(long id, LabWork labWork);
}

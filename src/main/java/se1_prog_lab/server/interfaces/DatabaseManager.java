package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;
import se1_prog_lab.server.VectorWrapper;

public interface DatabaseManager {
    boolean addElement(LabWork labWork);

    boolean removeElement(LabWork labWork);

    boolean replaceElement();

    boolean loadFromDatabase(VectorWrapper vectorWrapper);

    void getUser();

    void getPassword();
}

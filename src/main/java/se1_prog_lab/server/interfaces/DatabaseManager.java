package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;
import se1_prog_lab.util.AuthData;

import java.util.Vector;

public interface DatabaseManager {
    boolean addElement(LabWork labWork);

    boolean removeElement();

    boolean updateElement(LabWork labWork, long id);

    Vector<LabWork> loadCollectionFromDatabase();

    boolean checkAuth(AuthData authData);

    boolean doesUserExist(String username);

    boolean addUser(String username, String password);
}

package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;
import se1_prog_lab.util.AuthData;

public interface DatabaseManager {
    boolean addElement(LabWork labWork);

    boolean addThenLoad(LabWork labWork);

    boolean removeElement();

    boolean updateById(LabWork labWork, long id);

    boolean loadCollectionFromDatabase();

    boolean checkAuth(AuthData authData);

    boolean doesUserExist(String username);

    boolean addUser(String username, String password);

    boolean clear();

    boolean addThenLoad(LabWork labWork, int index);

    boolean addElementToIndex(LabWork labWork, int index);

    boolean sortById();

    boolean removeById(long id);
}

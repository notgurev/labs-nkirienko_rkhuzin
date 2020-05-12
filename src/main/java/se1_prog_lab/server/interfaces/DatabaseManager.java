package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;
import se1_prog_lab.util.AuthData;

import java.util.Collection;

public interface DatabaseManager {
    Long addElement(LabWork labWork);

    boolean updateById(LabWork labWork, long id);

    boolean loadCollectionFromDatabase(CollectionWrapper collectionWrapper);

    boolean checkAuth(AuthData authData);

    boolean doesUserExist(String username);

    boolean addUser(String username, String password);

    boolean clear();

    boolean addElementToIndex(LabWork labWork, int index);

    boolean sortById();

    boolean removeById(long id);
}

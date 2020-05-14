package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;
import se1_prog_lab.exceptions.DatabaseException;
import se1_prog_lab.util.AuthData;

import javax.xml.crypto.Data;

public interface DatabaseManager {
    Long addElement(LabWork labWork);

    boolean updateById(LabWork labWork, long id);

    boolean loadCollectionFromDatabase(CollectionWrapper collectionWrapper);

    String getPassword(String username) throws DatabaseException;

    boolean doesUserExist(String username) throws DatabaseException;

    boolean addUser(String username, String password) throws DatabaseException;

    boolean clear();

    boolean addElementToIndex(LabWork labWork, int index);

    boolean sortById();

    boolean removeById(long id);
}

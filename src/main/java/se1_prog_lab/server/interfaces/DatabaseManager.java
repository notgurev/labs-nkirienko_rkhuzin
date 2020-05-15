package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;
import se1_prog_lab.exceptions.DatabaseException;

import java.util.List;

public interface DatabaseManager {
    Long addElement(LabWork labWork, String username) throws DatabaseException;

    boolean updateById(LabWork labWork, long id, String username) throws DatabaseException;

    boolean loadCollectionFromDatabase(CollectionWrapper collectionWrapper);

    String getPassword(String username) throws DatabaseException;

    boolean doesUserExist(String username) throws DatabaseException;

    boolean addUser(String username, String password) throws DatabaseException;

    List<Long> clear(String username) throws DatabaseException;

    boolean removeById(long id, String username) throws DatabaseException;
}

package se1_prog_lab.server.interfaces;

import se1_prog_lab.exceptions.DatabaseException;
import se1_prog_lab.util.AuthData;

public interface AuthManager {
    boolean checkAuth(AuthData authData) throws DatabaseException;

    boolean doesUserExist(String username) throws DatabaseException;

    boolean register(AuthData authData) throws DatabaseException;
}

package se1_prog_lab.server.interfaces;

import se1_prog_lab.util.AuthData;

public interface AuthManager {
    boolean checkAuth(AuthData authData);

    boolean doesUserExist(String username);

    void register(AuthData authData);
}

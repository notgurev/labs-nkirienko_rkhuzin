package se1_prog_lab.server.interfaces;

import se1_prog_lab.exceptions.PasswordHashException;

public interface SecurePassword {
    String hash(String pass) throws PasswordHashException;
    boolean validatePassword(String incomingPassword, String hashedPassword) throws PasswordHashException;
}

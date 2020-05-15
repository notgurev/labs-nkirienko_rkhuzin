package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;
import se1_prog_lab.util.AuthData;

public interface ServerCommandReceiver {
    void loadCollectionFromDatabase();

    String add(LabWork labWork, AuthData authData);

    String clear(AuthData authData);

    String countLessThanDescription(String description);

    String info();

    String printUniqueTunedInWorks();

    String show();

    String sort();

    String filterGreaterThanMinimalPoint(int minimalPoint);

    String removeByID(long id, AuthData authData);

    String insertAt(LabWork labWork, int index, AuthData authData);

    String update(LabWork labWork, long id, AuthData authData);

    String register(AuthData authData);

    String login(AuthData authData);
}

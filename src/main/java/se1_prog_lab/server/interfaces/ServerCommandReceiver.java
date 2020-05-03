package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;
import se1_prog_lab.util.AuthData;

public interface ServerCommandReceiver {
    String add(LabWork labWork);

    String clear();

    String countLessThanDescription(String description);

    String info();

    String printUniqueTunedInWorks();

    String show();

    String sort();

    String filterGreaterThanMinimalPoint(int minimalPoint);

    String removeByID(long id);

    String insertAt(LabWork labWork, int index);

    String update(LabWork labWork, long id);

    String register(AuthData authData);

    String login(AuthData authData);
}

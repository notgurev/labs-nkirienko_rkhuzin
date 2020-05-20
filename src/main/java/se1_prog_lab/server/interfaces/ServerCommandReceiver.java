package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;
import se1_prog_lab.server.api.Response;
import se1_prog_lab.util.AuthData;

public interface ServerCommandReceiver {
    void loadCollectionFromDatabase();

    Response add(LabWork labWork, AuthData authData);

    Response clear(AuthData authData);

    Response countLessThanDescription(String description);

    Response info();

    Response printUniqueTunedInWorks();

    Response show();

    Response sort();

    Response filterGreaterThanMinimalPoint(int minimalPoint);

    Response removeByID(long id, AuthData authData);

    Response insertAt(LabWork labWork, int index, AuthData authData);

    Response update(LabWork labWork, long id, AuthData authData);

    Response register(AuthData authData);

    Response login(AuthData authData);
}

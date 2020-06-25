package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;
import se1_prog_lab.shared.api.AuthData;
import se1_prog_lab.shared.api.Response;

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

    Response update(LabWork labWork, long id, AuthData authData);

    Response register(AuthData authData);

    Response login(AuthData authData);

    Response getCollectionPage(int firstIndex, int size);

    Response insertBefore(LabWork carriedObject, Long id, AuthData authData);
}

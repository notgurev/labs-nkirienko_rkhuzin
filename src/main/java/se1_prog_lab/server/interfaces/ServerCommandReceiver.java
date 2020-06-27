package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;
import se1_prog_lab.shared.api.AuthData;
import se1_prog_lab.shared.api.Response;

import java.util.ResourceBundle;

public interface ServerCommandReceiver {
    void loadCollectionFromDatabase();

    Response add(LabWork labWork, AuthData authData, ResourceBundle resourceBundle);

    Response clear(AuthData authData, ResourceBundle resourceBundle);

    Response countLessThanDescription(String description, ResourceBundle resourceBundle);

    Response info(ResourceBundle resourceBundle);

    Response printUniqueTunedInWorks(ResourceBundle resourceBundle);

    Response sort(ResourceBundle resourceBundle);

    Response removeByID(long id, AuthData authData, ResourceBundle resourceBundle);

    Response update(LabWork labWork, long id, AuthData authData, ResourceBundle resourceBundle);

    Response register(AuthData authData, ResourceBundle resourceBundle);

    Response login(AuthData authData, ResourceBundle resourceBundle);

    Response getCollectionPage(int firstIndex, int size);

    Response insertBefore(LabWork carriedObject, Long id, AuthData authData, ResourceBundle resourceBundle);
}

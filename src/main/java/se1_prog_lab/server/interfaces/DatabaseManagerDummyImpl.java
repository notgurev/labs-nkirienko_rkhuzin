package se1_prog_lab.server.interfaces;

import se1_prog_lab.collection.LabWork;
import se1_prog_lab.server.VectorWrapper;

public class DatabaseManagerDummyImpl implements DatabaseManager {
    @Override
    public boolean addElement(LabWork labWork) {
        return false;
    }

    @Override
    public boolean removeElement(LabWork labWork) {
        return false;
    }

    @Override
    public boolean replaceElement() {
        return false;
    }

    @Override
    public boolean loadFromDatabase(VectorWrapper vectorWrapper) {
        return false;
    }

    @Override
    public void getUser() {

    }

    @Override
    public void getPassword() {

    }
}

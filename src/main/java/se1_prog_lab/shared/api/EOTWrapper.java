package se1_prog_lab.shared.api;

import se1_prog_lab.exceptions.EOTException;

public interface EOTWrapper {
    byte[] wrap(byte[] s);

    byte[] unwrap(byte[] s) throws EOTException;

    boolean hasEOTSymbol(byte[] s) throws EOTException;
}

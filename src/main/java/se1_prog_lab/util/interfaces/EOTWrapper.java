package se1_prog_lab.util.interfaces;

import se1_prog_lab.exceptions.EOTException;

public interface EOTWrapper {
    byte[] wrap(byte[] s);

    byte[] unwrap(byte[] s) throws EOTException;

    boolean hasEOTSymbol(byte[] s) throws EOTException;
}

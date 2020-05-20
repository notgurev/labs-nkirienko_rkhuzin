package se1_prog_lab.server.api;

import java.io.Serializable;

public enum ResponseType implements Serializable {
    PLAIN_TEXT,
    AUTH_STATUS,
    LABWORK_CLASS
}

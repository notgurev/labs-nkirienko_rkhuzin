package se1_prog_lab.server.api;

import java.io.Serializable;

public class Response implements Serializable {
    private final ResponseType RESPONSE_TYPE;
    private final Object message;

    public Response(ResponseType responseType, Object message) {
        this.RESPONSE_TYPE = responseType;
        this.message = message;
    }

    public ResponseType getResponseType() {
        return RESPONSE_TYPE;
    }

    public Object getMessage() {
        return message;
    }
}

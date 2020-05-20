package se1_prog_lab.server.api;

import java.io.Serializable;

public class Response implements Serializable {
    private final ResponseType RESPONSE_TYPE;
    private Object message;
    private final boolean isRejected;

    public Response(ResponseType responseType, Object message) {
        this.RESPONSE_TYPE = responseType;
        this.message = message;
        isRejected = false;
    }
    public Response(ResponseType responseType, Object message, boolean isRejected) {
        this.RESPONSE_TYPE = responseType;
        this.message = message;
        this.isRejected = isRejected;
    }



    public ResponseType getResponseType() {
        return RESPONSE_TYPE;
    }

    public Object getMessage() {
        return message;
    }

    public boolean isRejected() {
        return isRejected;
    }
}

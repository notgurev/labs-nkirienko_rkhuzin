package se1_prog_lab.shared.api;

import se1_prog_lab.collection.LabWork;

import java.io.Serializable;
import java.util.Collection;
import java.util.ResourceBundle;

import static se1_prog_lab.shared.api.AuthStatus.AUTH_FAILED;
import static se1_prog_lab.shared.api.AuthStatus.SERVER_ERROR;
import static se1_prog_lab.shared.api.ResponseType.AUTH_STATUS;
import static se1_prog_lab.shared.api.ResponseType.PLAIN_TEXT;

public class Response implements Serializable {
    private final ResponseType RESPONSE_TYPE;
    private final Object message;
    private final AuthStatus authStatus;
    private final boolean isRejected;
    private Object payload;

    public Response(ResponseType responseType, Object message) {
        this.RESPONSE_TYPE = responseType;
        this.message = message;
        isRejected = false;
        authStatus = null;
    }

    public Response(ResponseType responseType, Object message, boolean isRejected) {
        this.RESPONSE_TYPE = responseType;
        this.message = message;
        this.isRejected = isRejected;
        authStatus = null;
    }

    public Response(ResponseType responseType, AuthStatus authStatus, Object message, boolean isRejected) {
        this.RESPONSE_TYPE = responseType;
        this.message = message;
        this.isRejected = isRejected;
        this.authStatus = authStatus;
    }

    public ResponseType getResponseType() {
        return RESPONSE_TYPE;
    }

    public Object getPayload() { // todo убрать
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public String getMessage() {
        return message.toString();
    }

    @SuppressWarnings("unchecked")
    public Collection<LabWork> getCollection() {
        return (Collection<LabWork>) message;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public AuthStatus getAuthStatus() {
        return authStatus;
    }

    public static Response serverError(ResourceBundle r) {
        return new Response(PLAIN_TEXT, r.getString(SERVER_ERROR.getMessageLocalizationKey()), true);
    }

    public static Response authFailed(ResourceBundle r) {
        return new Response(AUTH_STATUS, AUTH_FAILED,
                r.getString(AUTH_FAILED.getMessageLocalizationKey()), true);
    }

    public static Response plainText(String localizedMessage) {
        return new Response(PLAIN_TEXT, localizedMessage);
    }
}

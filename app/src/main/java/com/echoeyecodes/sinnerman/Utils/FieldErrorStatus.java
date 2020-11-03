package com.echoeyecodes.sinnerman.Utils;

public class FieldErrorStatus {

    private String message;
    private boolean error;
    private String key;

    public FieldErrorStatus(String key, String message, boolean error) {
        this.message = message;
        this.error = error;
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

package com.ERP.exceptions;

public class LeavesNotFound extends Exception {
    public LeavesNotFound() {
        super();
    }

    public LeavesNotFound(String message) {
        super(message);
    }

    public LeavesNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public LeavesNotFound(Throwable cause) {
        super(cause);
    }

    protected LeavesNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

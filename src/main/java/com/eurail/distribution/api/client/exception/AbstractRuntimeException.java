package com.eurail.distribution.api.client.exception;

/**
 * Created  01/05/2018 09:00
 * Copyright (c) 2018 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public abstract class AbstractRuntimeException extends RuntimeException {
    protected AbstractRuntimeException() {
    }

    protected AbstractRuntimeException(String message) {
        super(message);
    }

    protected AbstractRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    protected AbstractRuntimeException(Throwable cause) {
        super(cause);
    }

    protected AbstractRuntimeException(String message, Object...parameters) {
        super(String.format(message, parameters));
    }
}

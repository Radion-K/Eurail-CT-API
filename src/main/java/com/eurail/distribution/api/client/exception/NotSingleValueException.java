package com.eurail.distribution.api.client.exception;

/**
 * Created  01/05/2018 09:00
 * Copyright (c) 2018 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public class NotSingleValueException extends AbstractRuntimeException {
    public NotSingleValueException(String message, Object... parameters) {
        super(message, parameters);
    }
}

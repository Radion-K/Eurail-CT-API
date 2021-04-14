package com.eurail.distribution.api.client.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created 08/04/2021
 * Copyright (c) 2021 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public enum Gender {
    UNDEFINED("u"),
    ;

    Gender(final String key) {
        this.key = key;
    }

    private final String key;

    @JsonValue
    public String getKey() {
        return key;
    }

}

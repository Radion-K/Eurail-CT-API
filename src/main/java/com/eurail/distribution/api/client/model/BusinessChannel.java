package com.eurail.distribution.api.client.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created 08/04/2021
 * Copyright (c) 2021 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public enum BusinessChannel {
    INTERRAIL("IN"),
    EURAIL("EU"),
    ;

    private final String key;

    BusinessChannel(final String key) {
        this.key = key;
    }

    @JsonValue
    public String getKey() {
        return this.key;
    }

}

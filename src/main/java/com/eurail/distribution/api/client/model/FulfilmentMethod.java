package com.eurail.distribution.api.client.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created 08/04/2021
 * Copyright (c) 2021 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public enum FulfilmentMethod {
    MOBILE("mobile"),
    PAPER("paper"),
    ETS("ets"),
    ;

    private final String key;

    FulfilmentMethod(final String key) {
        this.key = key;
    }

    @JsonValue
    public String getKey() {
        return key;
    }
}

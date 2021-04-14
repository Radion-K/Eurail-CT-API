package com.eurail.distribution.api.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Value;

import java.util.Collection;
import java.util.Set;

/**
 * Created 08/04/2021
 * Copyright (c) 2020 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

@Value
public class RefundableLineItems {
    @JsonCreator
    public RefundableLineItems(@JsonProperty("lineItems") final Collection<JsonNode> lineItems) {
        this.lineItems = lineItems;
    }

    @JsonDeserialize(as = Set.class)
    //  TODO Implement class(es) instead of Json
    Collection<JsonNode> lineItems;
}

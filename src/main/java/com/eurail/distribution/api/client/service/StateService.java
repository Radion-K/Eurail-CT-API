package com.eurail.distribution.api.client.service;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.states.State;
import io.sphere.sdk.states.queries.StateQuery;

import java.util.Collection;

/**
 * Created 08/04/2021
 * Copyright (c) 2020 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public class StateService extends AbstractService<State> {
    public StateService(SphereClient client) {
        super(client);
    }

    public Collection<State> getAll() {
        return executeQuery(StateQuery.of().withLimit(500)).getResults();
    }
}

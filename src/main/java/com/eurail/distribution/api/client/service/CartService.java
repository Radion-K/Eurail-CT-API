package com.eurail.distribution.api.client.service;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.queries.CartByIdGet;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;

import java.util.List;
import java.util.Locale;

/**
 * Created  01/05/2018 09:00
 * Copyright (c) 2018 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public class CartService extends AbstractService<Cart> {
    public CartService(SphereClient client) {
        super(client);
    }

    public Cart create(CartDraft cartDraft) {
        return executeRequest(CartCreateCommand.of(cartDraft));
    }

    public Cart getById(String id) {
        return executeRequest(CartByIdGet.of(id));
    }

    public static UpdateAction<Cart> setCustomFieldAction(final String field, final Object value) {
        return io.sphere.sdk.carts.commands.updateactions.SetCustomField.ofObject(field, value);
    }

    public static UpdateAction<Cart> setCustomerEmailAction(final String customerEmail) {
        return io.sphere.sdk.carts.commands.updateactions.SetCustomerEmail.of(customerEmail);
    }

    public static UpdateAction<Cart> setLocaleAction(final Locale locale) {
        return io.sphere.sdk.carts.commands.updateactions.SetLocale.of(locale);
    }

    public static UpdateAction<Cart> addLineItemAction(final String productId, final int variantId, final long quantity) {
        return io.sphere.sdk.carts.commands.updateactions.AddLineItem.of(productId, variantId, quantity);
    }

    public Cart executeUpdates(final Cart cart, List<UpdateAction<Cart>> updates) {
        return executeRequest(CartUpdateCommand.of(cart, updates));
    }
}


package com.eurail.distribution.api.client.service;

import com.eurail.distribution.api.client.model.RefundLineItems;
import com.eurail.distribution.api.client.model.RefundableLineItems;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.OrderFromCartDraft;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;
import io.sphere.sdk.orders.queries.OrderByIdGet;
import io.sphere.sdk.orders.queries.OrderQuery;

/**
 * Created  01/05/2018 09:00
 * Copyright (c) 2018 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public class OrderService extends AbstractService<Order> {
    public OrderService(SphereClient client) {
        super(client);
    }

    public Order create(final Cart cart) {
        return executeRequest(OrderFromCartCreateCommand.of(OrderFromCartDraft.of(cart)));
    }

    public Order getById(final String id) {
        return executeRequest(OrderByIdGet.of(id));
    }

    public Order getByCart(final Cart cart) {
        return getSingleObject(OrderQuery.of().withPredicates(order -> order.cart().isPresent().and(order.cart().id().is(cart.getId()))));
    }

    public RefundableLineItems getRefundableLineItems(final Order order) {
        //  TODO Implement OrderCreateCommand
        //  return executeRequest(OrderGetRefundableLineItemsCommand.of(cart.getId()));
        throw new UnsupportedOperationException("RefundableLineItems not implemented yet");
    }

    public Order createRefund(final Order order, final RefundLineItems refundLineItems) {
        //  TODO Implement it
        throw new UnsupportedOperationException("getByCartId not implemented yet");
    }
}
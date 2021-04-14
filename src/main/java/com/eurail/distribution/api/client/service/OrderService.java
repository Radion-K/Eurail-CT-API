package com.eurail.distribution.api.client.service;

import com.eurail.distribution.api.client.model.PaymentInterface;
import com.eurail.distribution.api.client.model.RefundableLineItems;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderByIdGet;

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

    public Order create(final Cart cart, final PaymentInterface paymentInterface, final boolean shortCycle) {
        //  TODO Implement OrderCreateCommand
        //  return executeRequest(OrderCreateCommand.of(cart.getId(), cart.getVersion(), paymentInterface));
        throw new UnsupportedOperationException("OrderCreateCommand not implemented yet");
    }

    public Order getById(final String id) {
        return executeRequest(OrderByIdGet.of(id));
    }

    public RefundableLineItems getRefundableLineItems(final Order order) {
        //  TODO Implement OrderCreateCommand
        //  return executeRequest(OrderGetRefundableLineItemsCommand.of(cart.getId()));
        throw new UnsupportedOperationException("RefundableLineItems not implemented yet");
    }

    public Order getByCartId(final String cartId) {
        //  TODO Implement it
        //  return getSingleObject(...);
        throw new UnsupportedOperationException("getByCartId not implemented yet");
    }
}


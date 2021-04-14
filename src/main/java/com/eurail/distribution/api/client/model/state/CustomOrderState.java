package com.eurail.distribution.api.client.model.state;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created  15/05/2018 18:10
 * Copyright (c) 2018 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public enum CustomOrderState {
    ON_HOLD("on-hold"),
    CART("cart"),
    PAYMENT_FAILED("payment-failed"),
    PAID("paid"),
    PASS_GENERATION("pass-generation"),
    FULFILMENT_SEND_ERROR("fulfilment-send-error"),
    SENT_TO_FULFILMENT("sent-to-fulfilment"),
    FULFILMENT_IN_PROGRESS("fulfilment-in-progress"),
    ERROR_IN_FULFILMENT("error-in-fulfilment"),
    ERROR_IN_MPS("error-in-mps"),
    MPS_UNAVAILABLE("mps-unavailable"),
    CANCELLED("cancelled"),
    SHIPPED("shipped"),
    RETURNED("returned"),
    LOST_SHIPMENT_INVESTIGATION("lost-shipment-investigation"),
    EXCEPTIONAL_REFUND_REQUESTED("exceptional-refund-requested"),
    EXCEPTIONAL_REFUND("exceptional-refund"),
    EXCEPTIONAL_REFUND_FAILED("exceptional-refund-failed"),
    CLAIM_REQUESTED("claim-requested"),
    CLAIM_DENIED("claim-denied"),
    CLAIM_ACCEPTED("claim-accepted"),
    CLAIM_COMPLETED("claim-completed"),
    CLAIM_REFUND_FAILED("claim-refund-failed"),
    SHIPPING_REFUND_REQUESTED("shipping-refund-requested"),
    SHIPPING_REFUND("shipping-refund"),
    SHIPPING_REFUND_FAILED("shipping-refund-failed"),
    CHARGEBACK_REVERSED("chargeback-reversed"),
    CHARGEBACK_REQUESTED("chargeback-requested"),
    CHARGEBACK("chargeback"),
    GIE_REFUND_REQUESTED("gie-refund-requested"),
    GIE_REFUND("gie-refund"),
    GIE_REFUND_FAILED("gie-refund-failed")
    ;

    private final String key;

    CustomOrderState(String key) {
        this.key = "order-" + key;
    }

    @JsonValue
    public final String getKey() {
        return this.key;
    }
}

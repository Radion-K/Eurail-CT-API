package com.eurail.distribution.api.client.model.state;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created  15/05/2018 18:09
 * Copyright (c) 2018 Eurail.com B.V.
 *
 * @author Radion, Rodion.Kyryliak@eurail.com
 */

public enum CustomLineItemState {
    BUILTIN_INITIAL("Initial"),
    INITIAL("line-item-initial"),
    ACTIVE("line-item-active"),
    CANCELLED("line-item-cancelled"),
    PARTIALLY_REFUNDED("line-item-partially-refunded"),
    REFUND_FAILED("line-item-refund-failed"),
    FULLY_REFUNDED("line-item-fully-refunded"),
    RMA_REQUESTED("line-item-rma-requested"),
    ERROR_IN_RMA("line-item-error-in-rma"),
    RMA_RECEIVED("line-item-rma-received"),
    PASS_RETURNED("line-item-pass-returned"),
    CANCELLATION_REQUESTED("line-item-cancellation-requested"),
    EXCHANGE_FEE_DEDUCTED_REQUESTED("line-item-exchange-fee-deducted-requested"),
    EXCHANGE_FEE_NOT_DEDUCTED_REQUESTED("line-item-exchange-fee-not-deducted-requested"),
    REFUND_REQUESTED_100("line-item-100percent-refund-requested"),
    REFUND_REQUESTED_85("line-item-85percent-refund-requested"),
    MOBILE_REFUND_REQUESTED("line-item-mobile-refund-requested"),
    EXCEPTIONAL_REFUND_CARRIERS_REQUESTED("line-item-exceptional-refund-requested")
    ;

    private final String key;

    CustomLineItemState(String key) {
        this.key = key;
    }

    @JsonValue
    public final String getKey() {
        return key;
    }

}

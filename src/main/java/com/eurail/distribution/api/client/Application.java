package com.eurail.distribution.api.client;

import com.eurail.distribution.api.client.executor.GetProductProjectionSearchCtExecutor;
import com.eurail.distribution.api.client.model.BusinessChannel;
import com.eurail.distribution.api.client.model.FulfilmentMethod;
import com.eurail.distribution.api.client.model.Gender;
import com.eurail.distribution.api.client.model.PassCategory;
import com.eurail.distribution.api.client.model.PaymentInterface;
import com.eurail.distribution.api.client.model.RefundableLineItems;
import com.eurail.distribution.api.client.service.CartService;
import com.eurail.distribution.api.client.service.OrderService;
import com.eurail.distribution.api.client.service.ProductProjectionSearchService;
import com.eurail.distribution.api.client.service.StateService;
import com.fasterxml.jackson.databind.JsonNode;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.LineItemDraft;
import io.sphere.sdk.carts.LineItemDraftBuilder;
import io.sphere.sdk.client.SphereApiConfig;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.DefaultCurrencyUnits;
import io.sphere.sdk.models.ResourceIdentifier;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.states.State;
import io.sphere.sdk.types.CustomFieldsDraft;
import org.apache.http.HttpResponse;

import javax.money.CurrencyUnit;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Application {
    private static final String API_URL = System.getenv("EURAIL_CT_API_URL");
    private static final String API_PROJECT = System.getenv("EURAIL_CT_API_PROJECT");;
    private static final String API_KEY = System.getenv("EURAIL_CT_API_KEY");

    private static final boolean SHORT_CYCLE = true;

    private static final CurrencyUnit CURRENCY = DefaultCurrencyUnits.EUR;
    private static final Locale LOCALE = Locale.UK;
    private static final BusinessChannel BUSINESS_CHANNEL = BusinessChannel.INTERRAIL;
    private static final FulfilmentMethod FULFILMENT_METHOD = FulfilmentMethod.MOBILE;
    private static final PassCategory PASS_CATEGORY = PassCategory.REGULAR;
    private static final ResourceIdentifier<ShippingMethod> NO_SHIPPING_METHOD = ResourceIdentifier.ofKey("no-shipping", "shipping-method");
    private static final Address MOBILE_SHIPPING_ADDRESS = Address.of(CountryCode.NL);
    private static final PaymentInterface PAYMENT_INTERFACE = PaymentInterface.NONE_PAYMENT;

    private static final String SKU = "30452000000111";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(1900, 1, 1);
    private static final CountryCode COUNTRY_OF_RESIDENCE = CountryCode.NL;
    private static final Gender GENDER = Gender.UNDEFINED;
    private static final String EMAIL = String.format("%s.%s@local", FIRST_NAME, LAST_NAME);

    public static void main(String[] args) {
        System.out.println(System.getenv());
        new Application().startApp();
    }

    private void startApp() {
        try(final SphereClient sphereClient = createClient(API_URL, API_PROJECT, API_KEY)) {
            final StateService stateService = new StateService(sphereClient);
            final CartService cartService = new CartService(sphereClient);
            final OrderService orderService = new OrderService(sphereClient);

            //  Read all states once and cache them (to check if order shipped/returned, line item active/refund-requested/refunded)
            final Collection<State> states = stateService.getAll();

            //  Read all products and save them. Use your storage for access to products.
            //  Does not work via service, as service executes Post request. We (Eurail) will implement POST request to make it working.
//            final Collection<ProductProjection> productProjections = new ProductProjectionSearchService(sphereClient).getAll();
            //  Read via http executor.
            final GetProductProjectionSearchCtExecutor getProductProjectionSearchCtExecutor = new GetProductProjectionSearchCtExecutor(API_URL, API_PROJECT, API_KEY);
            final HttpResponse response = getProductProjectionSearchCtExecutor.exec(new HashMap<String, List<Object>>(){{
                    put("limit", Collections.singletonList(10));
                }});
            if (GetProductProjectionSearchCtExecutor.isOk(response)) {
                final String bodyAsString = getProductProjectionSearchCtExecutor.getBodyAsString(response);
                final JsonNode bodyAsJsonNode = getProductProjectionSearchCtExecutor.getBodyAsJson(response);
                //  TODO. GetProductProjectionSearchCtExecutor already contains ObjectMapper, so implement getBodyAsObject(...) to get result.
                //  final PagedSearchResult<ProductProjection> pagedSearchResult = getProductProjectionSearchCtExecutor.getBodyAsObject(response, ...)
            }


            //  Create cart
            Cart cart = cartService.create(createCartDraft(
                    EMAIL, SKU, LOCALE, BUSINESS_CHANNEL, FULFILMENT_METHOD, MOBILE_SHIPPING_ADDRESS, NO_SHIPPING_METHOD, PASS_CATEGORY,
                    FIRST_NAME, LAST_NAME, DATE_OF_BIRTH, GENDER, COUNTRY_OF_RESIDENCE));

            //  Get cart by id
            cart = cartService.getById(cart.getId());

            //  TODO. Implement it.
            //  Update cart
            cart = updateCart(cartService, cart, LOCALE, EMAIL);

            //  TODO. Implement it.
            //  Create order
            Order order = orderService.create(cart, PAYMENT_INTERFACE, SHORT_CYCLE);

            //  TODO. Implement it.
            //  Get order by id
            order = orderService.getById(order.getId());

            //  TODO. Implement it.
            //  Get order by cart-id
            order = orderService.getByCartId(order.getCart().getId());

            //  TODO. Implement it.
            //  Get refundable line items
            RefundableLineItems refundableLineItems = orderService.getRefundableLineItems(order);

        } catch (final Exception e) {
            System.out.println(e);
        }
    }


    private static Cart updateCart(final CartService cartService, final Cart cart, final Locale locale, final String email) {
        //  Collect all update actions and make one http request
        return cartService.executeUpdates(cart, new ArrayList<UpdateAction<Cart>>() {{
            add(CartService.setLocaleAction(locale));
            add(CartService.setCustomerEmailAction(email));
        }});
    }

    private static CartDraft createCartDraft(
            final String customerEmail,
            final String sku,
            final Locale locale,
            final BusinessChannel businessChannel,
            final FulfilmentMethod fulfilmentMethod,
            final Address shippingAddress,
            final ResourceIdentifier<ShippingMethod> shippingMethod,
            final PassCategory passCategory,
            final String firstName,
            final String lastName,
            final LocalDate dateOfBirth,
            final Gender gender,
            final CountryCode countryOfResidence
    ) {

        final CartDraftBuilder cartDraftBuilder = CartDraftBuilder.of(CURRENCY)
                //  Set these fields later to show how update cart works
//                .customerEmail(customerEmail)
//                .locale(locale)
                .shippingAddress(shippingAddress)
                .custom(CustomFieldsDraft.ofTypeKeyAndObjects("cart-custom-fields",
                        createCartCustomFieldsObject(businessChannel, fulfilmentMethod)))
                .lineItems(new ArrayList<LineItemDraft>(){{
                    add(LineItemDraftBuilder
                            .ofSku(sku, 1L)
                            .custom(CustomFieldsDraft.ofTypeKeyAndObjects("line-item-pass-custom-fields",
                                    createLineItemCustomFields(firstName, lastName, dateOfBirth, gender, countryOfResidence, fulfilmentMethod, passCategory)))
                            .build());
                }});

        if (shippingMethod != null) {
            cartDraftBuilder.shippingMethod(shippingMethod);
        }

        return cartDraftBuilder.build();
    }

    private static Map<String, Object> createLineItemCustomFields(
            final String firstName,
            final String lastName,
            final LocalDate dateOfBirth,
            final Gender gender,
            final CountryCode countryOfResidence,
            final FulfilmentMethod fulfilmentMethod,
            final PassCategory passCategory) {
        return new HashMap<String, Object>(){{
            put("travellerFirstName", firstName);
            put("travellerLastName", lastName);
            put("dateOfBirth", dateOfBirth);
            put("gender", gender);
            put("passCategory", passCategory);
            put("countryOfResidence", countryOfResidence);
            put("fulfilmentMethod", fulfilmentMethod);
        }};
    }

    private static Map<String, Object> createCartCustomFieldsObject(
            final BusinessChannel businessChannel,
            final FulfilmentMethod fulfilmentMethod) {
        return new HashMap<String, Object>(){{
            put("businessChannel", businessChannel.getKey());
            put("fulfilmentMethod", fulfilmentMethod.getKey());
        }};
    }

    private static SphereClient createClient(final String apiUrl, final String projectKey, final String apiKey) {
        return SphereClientFactory.of().createClientOfApiConfigAndAccessToken(SphereApiConfig.of(projectKey, apiUrl), apiKey);
    }
}

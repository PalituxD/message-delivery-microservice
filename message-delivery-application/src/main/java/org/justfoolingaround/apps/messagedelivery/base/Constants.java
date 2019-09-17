package org.justfoolingaround.apps.messagedelivery.base;

public class Constants {

    private Constants() {
    }

    public static final String SUBMIT_MESSAGES_FIXED_RATE_IN_MILLISECONDS = "${submit.messages.fixed.rate.in.milliseconds}";
    public static final String SUBMIT_MESSAGES_INITIAL_DELAY_IN_MILLISECONDS = "${submit.messages.initial.delay.in.milliseconds}";

    public static final String MAX_NUMBER_OF_INTENTS_FOR_BATCHES = "${batch.max.number.intents}";

    public static final String URL_API_V1_PUT_EMAIL = "${url.api.v1.put.email}";
    public static final String URL_API_V1_SEARCH = "${url.api.v1.search}";
}

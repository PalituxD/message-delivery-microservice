package org.justfoolingaround.apps.messagedelivery.base;

public class Constants {

    private Constants() {
    }

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    public static final String SUBMIT_MESSAGES_FIXED_RATE_IN_MILLISECONDS = "${submit.messages.fixed.rate.in.milliseconds}";
    public static final String SUBMIT_MESSAGES_INITIAL_DELAY_IN_MILLISECONDS = "${submit.messages.initial.delay.in.milliseconds}";

    public static final String MAX_NUMBER_OF_INTENTS_FOR_BATCHES = "${batch.max.number.intents}";

    public static final String URL_API_V1_MESSAGE = "${url.api.v1.message}";
    public static final String URL_API_V1_MESSAGE_SEARCH = "${url.api.v1.message.search}";
    public static final String URL_API_V1_MESSAGE_SEARCH_BY = "${url.api.v1.message.search.by}";
    public static final String URL_API_V1_MESSAGE_EMAIL = "${url.api.v1.message.email}";
}

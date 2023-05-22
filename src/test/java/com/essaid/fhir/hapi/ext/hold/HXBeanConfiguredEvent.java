package com.essaid.fhir.hapi.ext.hold;

import org.springframework.context.PayloadApplicationEvent;

public class HXBeanConfiguredEvent<T>  extends PayloadApplicationEvent<T> {
    /**
     * Create a new PayloadApplicationEvent.
     *
     * @param source  the object on which the event initially occurred (never {@code null})
     * @param payload the payload object (never {@code null})
     */
    public HXBeanConfiguredEvent(Object source, T payload) {
        super(source, payload);
    }

    @Override
    public String toString() {
        return "HXBeanConfiguredEvent{" +
                "source=" + source + ",payload=" + getPayload() +
                '}';
    }
}

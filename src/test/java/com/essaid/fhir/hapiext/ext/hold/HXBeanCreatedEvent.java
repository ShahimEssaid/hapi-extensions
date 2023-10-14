package com.essaid.fhir.hapiext.ext.hold;

import org.springframework.context.PayloadApplicationEvent;

public class HXBeanCreatedEvent<T>  extends PayloadApplicationEvent<T> {
    /**
     * Create a new PayloadApplicationEvent.
     *
     * @param source  the object on which the event initially occurred (never {@code null})
     * @param payload the payload object (never {@code null})
     */
    public HXBeanCreatedEvent(Object source, T payload) {
        super(source, payload);
    }

    @Override
    public String toString() {
        return "HXBeanCreatedEvent{" +
                "source=" + source + ",payload=" + getPayload() +
                '}';
    }
}

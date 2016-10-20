package org.msh.etbm.desktop.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmemoria on 18/10/16.
 */
public class EventService {

    private static final EventService _instance = new EventService();

    private static final List<EventListener> listeners = new ArrayList<>();

    public static void addListener(EventListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(EventListener listener) {
        listeners.remove(listener);
    }

    public static void raise(Object event, Object data) {
        for (EventListener listener: listeners) {
            listener.onEvent(event, data);
        }
    }
}

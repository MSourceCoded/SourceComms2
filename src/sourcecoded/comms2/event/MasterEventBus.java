package sourcecoded.comms2.event;

import sourcecoded.events.AbstractEvent;
import sourcecoded.events.EventBus;
import sourcecoded.events.annotation.EventListener;

public class MasterEventBus extends EventBus {

    public static MasterEventBus instance;

    public static MasterEventBus instance() {
        if (instance == null) instance = new MasterEventBus();
        return instance;
    }

    @EventListener(respectsInheritance = true)
    public void eventRelay(AbstractEvent event) {
        this.raiseEvent(event);
    }

}

package sourcecoded.comms2.event;

import sourcecoded.comms2.network.SCSide;
import sourcecoded.events.AbstractEvent;

public class EventClientConnected extends AbstractEvent {

    public String clientID;
    public SCSide side;

    public EventClientConnected(String ID, SCSide raise) {
        this.clientID = ID;
        this.side = raise;
    }

}

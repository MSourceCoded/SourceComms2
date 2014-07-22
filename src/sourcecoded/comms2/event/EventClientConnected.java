package sourcecoded.comms2.event;

import sourcecoded.comms2.network.SCSide;
import sourcecoded.comms2.network.packet.ISourceCommsPacket;
import sourcecoded.events.AbstractEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventClientConnected extends AbstractEvent {

    public String clientID;
    public SCSide side;

    public EventClientConnected(String ID, SCSide raise) {
        this.clientID = ID;
        this.side = raise;
    }

}

package sourcecoded.comms2.event;

import sourcecoded.comms2.network.packet.ISourceCommsPacket;
import sourcecoded.events.AbstractEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventPacketReceived extends AbstractEvent {

    public ISourceCommsPacket packet;

    public EventPacketReceived(ISourceCommsPacket pkt) {
        packet = pkt;
    }

}

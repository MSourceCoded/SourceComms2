package sourcecoded.comms2.event;

import sourcecoded.comms2.network.packet.ISourceCommsPacket;
import sourcecoded.events.AbstractEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventPacketRegister extends AbstractEvent {

    ArrayList<Class<? extends ISourceCommsPacket>> packets = new ArrayList<Class<? extends ISourceCommsPacket>>();

    public void addPacket(Class<? extends ISourceCommsPacket> packet) {
        packets.add(packet);
    }

    public List<Class<? extends ISourceCommsPacket>> getPackets() {
        return Collections.unmodifiableList(packets);
    }

}

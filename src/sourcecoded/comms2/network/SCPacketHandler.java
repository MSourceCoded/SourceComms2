package sourcecoded.comms2.network;

import sourcecoded.comms2.event.EventPacketRegister;
import sourcecoded.comms2.network.packet.ISourceCommsPacket;
import sourcecoded.comms2.network.packet.Pkt0x00ConnectionRequest;
import sourcecoded.comms2.network.packet.Pkt0x01ConnectionAccepted;
import sourcecoded.comms2.network.packet.Pkt0x02ConnectionRejected;
import sourcecoded.events.EventBus;
import sourcecoded.events.EventPriority;
import sourcecoded.events.annotation.EventListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SCPacketHandler {

    public EventBus EVENT_BUS;
    public SCPacketCodecInstance CODEC;

    public SCPacketHandler() {
        EVENT_BUS = new EventBus();
        CODEC = new SCPacketCodecInstance();

        EVENT_BUS.register(CODEC);
        EVENT_BUS.raiseEvent(new EventPacketRegister());
    }

    public class SCPacketCodecInstance extends SCPacketCodec {

        @EventListener(priority = EventPriority.HIGHEST)
        public void addDiscriminators(EventPacketRegister event) {
            event.addPacket(Pkt0x00ConnectionRequest.class);
            event.addPacket(Pkt0x01ConnectionAccepted.class);
            event.addPacket(Pkt0x02ConnectionRejected.class);
        }

        @Override
        public void abstractEncode(ISourceCommsPacket packet, DataOutputStream stream) throws IOException {
            packet.encode(stream);
        }

        @Override
        public void abstractDecode(ISourceCommsPacket packet, DataInputStream stream) throws IOException {
            packet.decode(stream);
        }
    }

}

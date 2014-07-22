package sourcecoded.comms2.network;

import sourcecoded.comms2.event.EventPacketReceived;
import sourcecoded.comms2.event.EventPacketRegister;
import sourcecoded.comms2.event.MasterEventBus;
import sourcecoded.comms2.network.packet.ISourceCommsPacket;
import sourcecoded.comms2.network.packet.Pkt0x00PacketReceivedConfirmation;
import sourcecoded.events.EventBus;
import sourcecoded.events.EventPriority;
import sourcecoded.events.annotation.EventListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SCPacketHandler {

    public EventBus EVENT_BUS;
    protected SCPacketCodecInstance CODEC;

    public boolean isBusy = false;

    public SCPacketHandler() {
        EVENT_BUS = new EventBus();
        CODEC = new SCPacketCodecInstance();

        EVENT_BUS.register(CODEC);
        EVENT_BUS.register(MasterEventBus.instance());
        EVENT_BUS.raiseEvent(new EventPacketRegister());
    }

    public class SCPacketCodecInstance extends SCPacketCodec {

        @EventListener(priority = EventPriority.HIGHEST)
        public void addDiscriminators(EventPacketRegister event) {
            event.addPacket(Pkt0x00PacketReceivedConfirmation.class);
        }

        @Override
        public void abstractEncode(ISourceCommsPacket packet, DataOutputStream stream) throws IOException {
            packet.encode(stream);
        }

        @Override
        public void abstractDecode(ISourceCommsPacket packet, DataInputStream stream) throws IOException {
            packet.decode(stream);
            EVENT_BUS.raiseEvent(new EventPacketReceived(packet));
        }
    }

    public void sendPacket(DataOutputStream dos, ISourceCommsPacket packet) throws IOException {
        isBusy = true;
        CODEC.encodeInto(packet, dos);
    }

    @EventListener(priority = EventPriority.LOWEST)
    public void onReceiveConfirmation(Pkt0x00PacketReceivedConfirmation event) {
        isBusy = false;
    }

    public ISourceCommsPacket matchDiscriminator(int disc) throws IllegalAccessException, InstantiationException {
        return CODEC.discriminators.getForward(disc).newInstance();
    }

    public void handlePacket(int disc, DataInputStream input) throws InstantiationException, IllegalAccessException, IOException {
        ISourceCommsPacket packet = matchDiscriminator(disc);
        CODEC.decodeInto(packet, input);
    }
}

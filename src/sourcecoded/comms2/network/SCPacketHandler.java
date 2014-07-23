package sourcecoded.comms2.network;

import sourcecoded.comms2.event.EventPacketReceived;
import sourcecoded.comms2.event.EventPacketRegister;
import sourcecoded.comms2.event.MasterEventBus;
import sourcecoded.comms2.network.packet.ISourceCommsPacket;
import sourcecoded.comms2.network.packet.Pkt0x00PacketReceivedConfirmation;
import sourcecoded.comms2.network.packet.Pkt1x00PingRequest;
import sourcecoded.comms2.network.packet.Pkt1x01PingReply;
import sourcecoded.comms2.network.socket.SCClient;
import sourcecoded.data.buffer.array.GravityBuffer;
import sourcecoded.events.EventBus;
import sourcecoded.events.EventPriority;
import sourcecoded.events.annotation.EventListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SCPacketHandler {

    public EventBus EVENT_BUS;
    protected SCPacketCodecInstance CODEC;
    public GravityBuffer<ISourceCommsPacket> packetBuffer = new GravityBuffer<ISourceCommsPacket>();

    public volatile SCClient theClient;

    public volatile boolean isBusy = false;

    public SCPacketHandler(SCClient client) {
        EVENT_BUS = new EventBus();
        CODEC = new SCPacketCodecInstance();

        EVENT_BUS.register(CODEC);
        EVENT_BUS.register(MasterEventBus.instance());
        EVENT_BUS.raiseEvent(new EventPacketRegister());

        theClient = client;
    }

    public class SCPacketCodecInstance extends SCPacketCodec {

        @EventListener(priority = EventPriority.HIGHEST)
        public void addDiscriminators(EventPacketRegister event) {
            event.addPacket(Pkt0x00PacketReceivedConfirmation.class);
            event.addPacket(Pkt1x00PingRequest.class);
            event.addPacket(Pkt1x01PingReply.class);
        }

        @Override
        public void abstractEncode(ISourceCommsPacket packet, DataOutputStream stream) throws IOException {
            packet.encode(stream);
        }

        @Override
        public void abstractDecode(ISourceCommsPacket packet, DataInputStream stream) throws Exception {
            packet.decode(stream);
            packet.channelRead(theClient);
            EVENT_BUS.raiseEvent(new EventPacketReceived(packet));
        }
    }

    public void sendPacket(DataOutputStream dos, ISourceCommsPacket packet) throws IOException {
        if (!packet.getClass().equals(Pkt0x00PacketReceivedConfirmation.class))
            isBusy = true;

        CODEC.encodeInto(packet, dos);
    }

    public ISourceCommsPacket matchDiscriminator(int disc) throws IllegalAccessException, InstantiationException {
        System.err.println(disc);
        return CODEC.discriminators.getForward(disc).newInstance();
    }

    public boolean handlePacket(int disc, DataInputStream input) throws Exception {
        ISourceCommsPacket packet = matchDiscriminator(disc);
        CODEC.decodeInto(packet, input);
        return !packet.getClass().equals(Pkt0x00PacketReceivedConfirmation.class);
    }

    public void reset() throws IOException {
        isBusy = false;

        if (packetBuffer.size() > 0) {
            sendPacket(theClient.dos, packetBuffer.retrieve());
            packetBuffer.delete();
        }
    }
}

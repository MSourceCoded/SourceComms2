package sourcecoded.comms2.network.packet;

import sourcecoded.comms2.network.socket.SCClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Pkt0x00PacketReceivedConfirmation implements ISourceCommsPacket {

    @Override
    public void encode(DataOutputStream data) throws IOException {
    }

    @Override
    public void decode(DataInputStream data) throws IOException {
    }

    @Override
    public void channelRead(SCClient socket) throws IOException {
        socket.getPacketHandler().reset();
    }
}

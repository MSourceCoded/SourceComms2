package sourcecoded.comms2.network.packet;

import sourcecoded.comms2.network.socket.SCClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Pkt1x00PingRequest implements ISourceCommsPacket {

    public long onEncode;

    @Override
    public void encode(DataOutputStream data) throws IOException {
        onEncode = System.nanoTime();
        data.writeLong(onEncode);
    }

    @Override
    public void decode(DataInputStream data) throws IOException {
        onEncode = data.readLong();
    }

    @Override
    public void channelRead(SCClient socket) throws Exception {
        socket.sendPacket(new Pkt1x01PingReply(onEncode));
    }
}

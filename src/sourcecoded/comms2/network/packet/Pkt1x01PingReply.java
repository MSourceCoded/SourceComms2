package sourcecoded.comms2.network.packet;

import sourcecoded.comms2.network.socket.SCClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Pkt1x01PingReply implements ISourceCommsPacket {

    public long onEncode, onDecode, differenceNano, differenceMS;

    public Pkt1x01PingReply(){}
    public Pkt1x01PingReply(long onEncode) {
        this.onEncode = onEncode;
    }

    @Override
    public void encode(DataOutputStream data) throws IOException {
        data.writeLong(onEncode);
    }

    @Override
    public void decode(DataInputStream data) throws IOException {
        long onDecode = System.nanoTime();
        onEncode = data.readLong();

        differenceNano = (onDecode - onEncode);
        differenceMS = differenceNano / 1000000;
    }

    @Override
    public void channelRead(SCClient socket) {
    }
}

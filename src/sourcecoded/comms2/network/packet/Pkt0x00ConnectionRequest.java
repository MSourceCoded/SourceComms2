package sourcecoded.comms2.network.packet;

import sourcecoded.comms2.util.StreamUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Pkt0x00ConnectionRequest implements ISourceCommsPacket {

    public String clientID;

    public Pkt0x00ConnectionRequest(String clientID) {
        this.clientID = clientID;
    }

    @Override
    public void encode(DataOutputStream data) throws IOException {
        StreamUtils.writeString(clientID, data);
    }

    @Override
    public void decode(DataInputStream data) throws IOException {
        clientID = StreamUtils.readString(data);
    }
}

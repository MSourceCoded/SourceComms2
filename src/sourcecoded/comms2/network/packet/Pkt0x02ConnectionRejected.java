package sourcecoded.comms2.network.packet;

import sourcecoded.comms2.util.StreamUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Pkt0x02ConnectionRejected implements ISourceCommsPacket {

    public String clientID, reason;

    public Pkt0x02ConnectionRejected(String clientID, String reason) {
        this.clientID = clientID;
        this.reason = reason;
    }

    @Override
    public void encode(DataOutputStream data) throws IOException {
        StreamUtils.writeString(clientID, data);
        StreamUtils.writeString(reason, data);
    }

    @Override
    public void decode(DataInputStream data) throws IOException {
        clientID = StreamUtils.readString(data);
        reason = StreamUtils.readString(data);
    }
}

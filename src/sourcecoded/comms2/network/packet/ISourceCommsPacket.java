package sourcecoded.comms2.network.packet;

import sourcecoded.comms2.network.socket.SCClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Implement me when creating a custom packet. Includes everything you need for successful packet creation
 * @author MrSourceCoded
 *
 */
public interface ISourceCommsPacket {

	/**
	 * Encode a packet to the stream. Pass your stream.write things here
	 * @param data The stream to write to
	 * @throws IOException Something happened that wasn't meant to
	 */
	public void encode(DataOutputStream data) throws IOException;

	/**
	 * Decode a packet from the stream. Pass your stream.read things here
	 * @param data The stream to read from
	 * @throws IOException Something happened that wasn't meant to
	 */
	public void decode(DataInputStream data) throws IOException;

    /**
     * Triggered after the packet is decoded and handled. All resources should be available through the socket
     */
    public void channelRead(SCClient socket) throws Exception;

}

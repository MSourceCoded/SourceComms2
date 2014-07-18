package sourcecoded.comms2.network.socket;

import sourcecoded.comms2.api.data.map.GearedHashmap;

import java.net.Socket;

/**
 * Implemented Server-Side to handle multiple clients
 */
public class ServerConnectionHandler {

    /**
     * A Geared hashmap of all the clients. Type1 is the ClientID
     */
    public GearedHashmap<String, Socket> clients = new GearedHashmap<String, Socket>();



}

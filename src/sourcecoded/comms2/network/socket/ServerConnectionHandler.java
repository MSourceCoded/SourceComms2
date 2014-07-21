package sourcecoded.comms2.network.socket;


import sourcecoded.data.map.GearedHashmap;

/**
 * Implemented Server-Side to handle multiple clients
 */
public class ServerConnectionHandler {

    /**
     * A Geared hashmap of all the clients. Type1 is the ClientID
     */
    public GearedHashmap<String, SCClient> clients = new GearedHashmap<String, SCClient>();



}

package sourcecoded.comms2.network.socket;

import sourcecoded.comms2.network.packet.ISourceCommsPacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class SCServer {

    public ServerSocket server;
    public ServerConnectionHandler clientHandler;

    public boolean isAcceptingClients = true;

    Thread clientConnectionLoop = new Thread() {
        public void run() {
            while (isAcceptingClients && !isInterrupted()) {
                try {
                    clientHandler.addClient(server.accept());
                } catch (IOException e) {
                    if (!isInterrupted())
                        e.printStackTrace();
                }
            }
        }
    };

    public SCServer(int port) throws IOException {
        server = new ServerSocket(port);
        clientHandler = new ServerConnectionHandler();

        clientConnectionLoop.start();
    }

    /**
     * Send a packet to a specific client
     */
    public void sendPacketToClient(ISourceCommsPacket packet, String CLIENT_ID) throws Exception {
        getClient(CLIENT_ID).sendPacket(packet);
    }

    /**
     * Send a packet to all the clients
     */
    public void sendPacketToAll(ISourceCommsPacket packet) throws Exception {
        for (SCClient client : clientHandler.clients) {
            client.sendPacket(packet);
        }
    }

    /**
     * Get all the available clients
     */
    public List<String> getClientIDs() {
        ArrayList<String> list = new ArrayList<String>();
        for (SCClient client : clientHandler.clients) {
            if (client.CLIENT_ID != null) list.add(client.CLIENT_ID);
        }
        return list;
    }

    /**
     * Get the client for the client id provided
     */
    public SCClient getClient(String clientID) {
        return clientHandler.getClient(clientID);
    }

    /**
     * Force disconnect a client
     */
    public void disconnect(String clientID) throws IOException {
        clientHandler.getClient(clientID).close();
        clientHandler.remove(clientID);
    }

    /**
     * Disconnect all the clients
     */
    public void disconnectAll() throws IOException {
        for (String id : getClientIDs())
            disconnect(id);
    }

    /**
     * Close the server
     */
    public void close() throws IOException {
        disconnectAll();
        clientConnectionLoop.interrupt();
        isAcceptingClients = false;
        server.close();
    }

}

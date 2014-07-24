package sourcecoded.comms2.network.socket;


import sourcecoded.comms2.event.EventClientClosed;
import sourcecoded.comms2.event.MasterEventBus;
import sourcecoded.events.annotation.EventListener;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Implemented Server-Side to handle multiple clients
 */
public class ServerConnectionHandler {

    public ArrayList<SCClient> clients = new ArrayList<SCClient>();

    public ServerConnectionHandler() {
        MasterEventBus.instance().register(this);
    }

    public void addClient(Socket client) {
        clients.add(new SCClient(client));
    }

    public SCClient getClient(String id) {
        for (SCClient client : clients) {
            if (client.CLIENT_ID.equals(id)) return client;
        }

        return null;
    }

    public boolean idExists(String id) {
        return getClient(id) != null;
    }

    public void remove(String id) {
        clients.remove(getClient(id));
    }

    @EventListener
    public void disconnected(EventClientClosed event) {
        clients.remove(event.client);
    }


}

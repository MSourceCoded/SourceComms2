package sourcecoded.comms2.network.socket;


import java.net.Socket;
import java.util.ArrayList;

/**
 * Implemented Server-Side to handle multiple clients
 */
public class ServerConnectionHandler {

    public ArrayList<SCClient> clients = new ArrayList<SCClient>();

    public void addClient(Socket client) {
        clients.add(new SCClient(client));
    }

    public SCClient getClient(String id) {
        for (SCClient client : clients) {
            if (client.CLIENT_ID.equals(id)) return client;
        }

        return null;
    }

    public void remove(String id) {
        clients.remove(id);
    }
}

package sourcecoded.comms2.network.socket;

import sourcecoded.comms2.network.SCPacketHandler;
import sourcecoded.comms2.network.SCSide;
import sourcecoded.comms2.timeout.TimeoutController;
import sourcecoded.comms2.timeout.TimeoutException;
import sourcecoded.events.AbstractEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SCClient {

    String CLIENT_ID;

    Socket socket;

    DataInputStream dis;
    DataOutputStream dos;

    String hostname;
    int port;

    SCPacketHandler packetHandler;
    SCSide launchSide;

    /**
     * 10 seconds
     */
    public static int TIMEOUT_DEFAULT = 10000;

    Thread connect = new Thread() {
        @Override
        public void run() {
            try {
                socket = new Socket(hostname, port);
                setupStreams();
            } catch (Exception e) {
                if (this.isAlive())
                    e.printStackTrace();
            }
        }
    };

    /**
     * For server instances
     */
    public SCClient(Socket socket) {
        this.socket = socket;
        this.packetHandler = new SCPacketHandler();
        launchSide = SCSide.SERVER;
        try {
            setupStreams();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * For client instances. Timeout is in Milliseconds
     */
    public SCClient(String hostname, int port, String client_id, int timeout) {
        this.CLIENT_ID = client_id;
        this.hostname = hostname;
        this.port = port;
        this.packetHandler = new SCPacketHandler();
        launchSide = SCSide.CLIENT;

        connect.start();

        try {
            if (timeout > 0) {
                TimeoutController.execute(connect, timeout);
            }
        } catch (TimeoutException e) {
            //do the thing
        }

    }

    /**
     * For client instances without a timeout
     */
    public SCClient(String hostname, int port, String client_id) {
        this(hostname, port, client_id, -1);
    }

    /**
     * Setup the data streams for this socket
     */
    public void setupStreams() throws IOException {
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * Get the packet handler object for this socket
     */
    public SCPacketHandler getPacketHandler() {
        return packetHandler;
    }

    /**
     * Subscribe to the Event Bus for this socket
     */
    public void subscribeToEventBus(Object obj) {
        packetHandler.EVENT_BUS.register(obj);
    }

    /**
     * Raise an event
     */
    public void raiseEventBusEvent(AbstractEvent event) {
        packetHandler.EVENT_BUS.raiseEvent(event);
    }

}

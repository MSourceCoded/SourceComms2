package sourcecoded.comms2.network.socket;

import sourcecoded.comms2.event.EventClientConnected;
import sourcecoded.comms2.exception.NotAuthenticatedException;
import sourcecoded.comms2.exception.NotConnectedException;
import sourcecoded.comms2.network.SCPacketHandler;
import sourcecoded.comms2.network.SCSide;
import sourcecoded.comms2.network.packet.ISourceCommsPacket;
import sourcecoded.comms2.network.packet.Pkt0x00PacketReceivedConfirmation;
import sourcecoded.comms2.timeout.TimeoutController;
import sourcecoded.comms2.timeout.TimeoutException;
import sourcecoded.comms2.util.StreamUtils;
import sourcecoded.data.buffer.array.GravityBuffer;
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
    int timeout;

    SCPacketHandler packetHandler;
    SCSide launchSide;
    GravityBuffer<ISourceCommsPacket> packetBuffer = new GravityBuffer<ISourceCommsPacket>();

    boolean auth;

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
                if (!this.isInterrupted())
                    e.printStackTrace();
            }
        }
    };

    Thread doListen = new Thread() {
        @Override
        public void run() {
            try {
                while (isConnected() && !isInterrupted()) {
                    int discriminator = dis.readInt();
                    packetHandler.matchDiscriminator(discriminator);
                    packetHandler.sendPacket(dos, new Pkt0x00PacketReceivedConfirmation());
                }
            } catch (Exception e) {
                if (!this.isInterrupted())
                    e.printStackTrace();
            }
        }
    };

    Thread sending = new Thread() {
        public void run() {
            try {
                while (packetBuffer.size() > 0) {
                    if (!packetHandler.isBusy) {
                        packetHandler.sendPacket(dos, packetBuffer.retrieve());
                        packetBuffer.delete();
                    }
                }
            } catch (Exception e) {
                if (!this.isInterrupted())
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
        this.timeout = timeout;
    }

    /**
     * Only use this on the CLIENT side
     */
    public void connect() {
        try {
            if (timeout > 0) {
                TimeoutController.execute(connect, timeout);
            } else {
                connect.start();
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    /**
     * For client instances without a timeout
     */
    public SCClient(String hostname, int port, String client_id) {
        this(hostname, port, client_id, -1);
    }

    /**
     * Setup the data streams for this socket. This also registers the instance in the EventBus.
     */
    public void setupStreams() throws IOException {
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());

        subscribeToEventBus(this);

        initialAuthentication();
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

    /**
     * Am I connected?
     */
    public boolean isConnected() {
        return socket.isConnected();
    }

    /**
     * Am I connected to the server and allowed to send proper packets?
     */
    public boolean isAuthenticated() {
        return auth;
    }

    /**
     * Send a packet
     */
    public void sendPacket(ISourceCommsPacket packet) throws Exception {
        if (!isConnected()) throw new NotConnectedException();
        if (!isAuthenticated()) throw new NotAuthenticatedException();

        //packetHandler.sendPacket(this.dos, packet);
        packetBuffer.append(packet);
        sending.start();
    }

    //LISTENING

    private void initialAuthentication() throws IOException {
        if (launchSide == SCSide.CLIENT) {
            StreamUtils.writeString(CLIENT_ID, dos);

            if (dis.readInt() == -1) {
                auth = true;
            }
        }

        if (launchSide == SCSide.SERVER) {
            CLIENT_ID = StreamUtils.readString(dis);
            dos.writeInt(-1);
            auth = true;
        }

        listenLoop();

        getPacketHandler().EVENT_BUS.raiseEvent(new EventClientConnected(CLIENT_ID, launchSide));
    }

    private void listenLoop() {
        doListen.start();
    }

    /**
     * Close the thread
     */
    public void close() throws IOException {
        dos.close();
        dis.close();
        doListen.interrupt();
        socket.close();
    }
}
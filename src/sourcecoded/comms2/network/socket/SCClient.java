package sourcecoded.comms2.network.socket;

import sourcecoded.comms2.timeout.TimeoutController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SCClient {

    String CLIENT_ID;

    Socket socket;

    DataInputStream dis;
    DataOutputStream dos;

    String hostname;
    int port;

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
                e.printStackTrace();
            }
        }
    };

    /**
     * For server instances
     */
    public SCClient(Socket socket) {
        this.socket = socket;
        try {
            setupStreams();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SCClient("10.0.0.29", 1338, "Blah", 1000);
    }

    /**
     * For client instances. Timeout is in Milliseconds
     */
    public SCClient(String hostname, int port, String client_id, int timeout) {
        this.CLIENT_ID = client_id;
        this.hostname = hostname;
        this.port = port;

        connect.start();

        try {
            if (timeout > 0) {
                TimeoutController.execute(connect, timeout);
            }
        } catch (Exception e) {
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

}

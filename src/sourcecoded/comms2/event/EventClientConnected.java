package sourcecoded.comms2.event;

import sourcecoded.comms2.network.socket.SCClient;
import sourcecoded.events.AbstractEvent;

public class EventClientConnected extends AbstractEvent {

    public SCClient client;

    public EventClientConnected(SCClient client) {
        this.client = client;
    }

}

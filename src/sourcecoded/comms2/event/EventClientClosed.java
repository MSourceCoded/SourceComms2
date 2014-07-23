package sourcecoded.comms2.event;

import sourcecoded.comms2.network.socket.SCClient;
import sourcecoded.events.AbstractEvent;

public class EventClientClosed extends AbstractEvent {

    public SCClient client;
    public String reason;

    public EventClientClosed(SCClient client, String reason) {
        this.client = client;
        this.reason = reason;
    }

}

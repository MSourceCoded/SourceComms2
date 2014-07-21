package sourcecoded.comms2.network;

import sourcecoded.comms2.event.EventPacketRegister;
import sourcecoded.comms2.network.packet.ISourceCommsPacket;
import sourcecoded.data.map.GearedHashmap;
import sourcecoded.data.util.CollectionUtils;
import sourcecoded.events.EventPriority;
import sourcecoded.events.annotation.EventListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class SCPacketCodec {

    protected GearedHashmap<Integer, Class<? extends ISourceCommsPacket>> discriminators = new GearedHashmap<Integer, Class<? extends ISourceCommsPacket>>();
    private int lastKey = 0;

    private void addDiscriminator(Class<? extends ISourceCommsPacket> packetClass) {
        if (discriminators.getMapReverse().containsKey(packetClass)) return;
        discriminators.put(lastKey, packetClass);
        lastKey++;
    }

    /**
     * Called last, so all the other packets can do their thing
     */
    @EventListener(priority = EventPriority.LOWEST)
    public void registerPackets(EventPacketRegister event) {
        for (Class<? extends ISourceCommsPacket> packet : event.getPackets()) {
            addDiscriminator(packet);
        }

        sortDiscriminatorsList();
    }

    /**
     * Ensures the list is the same order on ALL implementations
     */
    void sortDiscriminatorsList() {
        Collection<Class<? extends ISourceCommsPacket>> coll = discriminators.getMapForward().values();

        List<Class<? extends ISourceCommsPacket>> list = new ArrayList<Class<? extends ISourceCommsPacket>>(coll);

        Collections.sort(list, CollectionUtils.CLASS_COMPARATOR);
        discriminators.clear();

        lastKey = 0;
        for (Class<? extends ISourceCommsPacket> packet : list) {
            addDiscriminator(packet);
        }
    }

    /**
     * Get the discriminator for the packet class provided
     */
    public int getDiscriminatorForClass(Class<? extends ISourceCommsPacket> p) {
        return discriminators.getReverse(p);
    }

    /**
     * Get the packet class for the discriminator provided
     */
    public Class<? extends ISourceCommsPacket> getClassForDisciminator(int d) {
        return discriminators.getForward(d);
    }

    public abstract void abstractEncode(ISourceCommsPacket packet, DataOutputStream stream) throws IOException;
    public abstract void abstractDecode(ISourceCommsPacket packet, DataInputStream stream) throws IOException;

    public void encodeInto(ISourceCommsPacket packet, DataOutputStream dos) throws IOException {
        dos.writeInt(getDiscriminatorForClass(packet.getClass()));
        abstractEncode(packet, dos);
    }

    public void decodeInto(ISourceCommsPacket packet, DataInputStream dis) throws IOException {
        abstractDecode(packet, dis);
    }

}

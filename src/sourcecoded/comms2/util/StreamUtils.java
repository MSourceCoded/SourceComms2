package sourcecoded.comms2.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StreamUtils {

    public static void writeString(String arg, DataOutputStream data) throws IOException {
        byte[] abyte = arg.getBytes();

        data.writeInt(abyte.length);
        for (byte curr : abyte)
            data.writeByte(curr);
    }

    public static String readString(DataInputStream data) throws IOException {
        int l = data.readInt();
        byte[] abyte = new byte[l];

        for (int i = 0; i < l; i++)
            abyte[i] = data.readByte();

        return new String(abyte);
    }

}

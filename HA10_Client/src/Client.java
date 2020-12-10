import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Client {

    public static final int PORT = 8000;

    private final InetAddress ia;
    private final MulticastSocket ms;

    public Client(String address) throws IOException {
        ia = InetAddress.getByName(address);
        if(!ia.isMulticastAddress()) {
            throw new IllegalArgumentException("required multicast address");
        }
        ms = new MulticastSocket(PORT);
    }

    public void start() throws IOException {
        byte[] buffer = new byte[30];
        try {
            ms.joinGroup(ia);
            for (int i = 0; i < 10; i++) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                ms.receive(packet);
                System.out.println(new String(packet.getData()));
            }
        } finally {
            ms.leaveGroup(ia);
        }
    }

    public void close() {
        ms.close();
    }

}

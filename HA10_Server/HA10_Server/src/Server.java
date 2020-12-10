import java.io.IOException;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {

    public static final int PORT = 8000;

    private final InetAddress ia;
    private final DatagramSocket socket;
    private final DateFormat df;

    public Server(String address) throws UnknownHostException, SocketException {
        ia = InetAddress.getByName(address);
        if(!ia.isMulticastAddress()) {
            throw new IllegalArgumentException("required multicast address");
        }
        socket = new DatagramSocket();
        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    }

    public void start() throws IOException {
        while(true) {
            String date = df.format(new Date());
            System.out.println(date);
            byte[] bytes = date.getBytes();
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, ia, PORT);
            socket.send(packet);
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {

            }
        }
    }

    public void close() {
        socket.close();
    }

}

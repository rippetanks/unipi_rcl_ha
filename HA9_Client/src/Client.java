import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Date;

public class Client {

    public static final int MAX_TIMEOUT = 2000;
    public static final int N_PING = 10;

    private final DatagramSocket socket;
    private final InetAddress address;
    private final int port;

    private int transmitted = 0;
    private int received = 0;
    private long[] rtts = new long[N_PING];

    public Client(String name, int port) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        socket.setSoTimeout(MAX_TIMEOUT);
        address = InetAddress.getByName(name);
        this.port = port;
    }

    public void ping() throws IOException {
        transmitted = 0;
        received = 0;
        Arrays.fill(rtts, 0);

        for(int i = 0; i < N_PING; i++) {
            try {
                send(i);
                DatagramPacket packet = receive();
                long rtt = parseAndGetRTT(packet);
                System.out.printf(" %d ms\r\n", rtt);
            } catch(SocketTimeoutException e) {
                System.out.println(" RTT *");
            }
        }
    }

    public void printStats() {

    }

    private void send(int i) throws IOException {
        Date now = new Date();
        String msg = String.format("PING %d %d", i, now.getTime());
        byte[] bytes = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
        socket.send(packet);

        transmitted++;
        System.out.print(msg);
    }

    private DatagramPacket receive() throws IOException {
        byte[] buffer = new byte[20];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        //socket.setSoTimeout(MAX_TIMEOUT);
        socket.receive(packet);

        received++;

        return packet;
    }

    private long parseAndGetRTT(DatagramPacket packet) {
        String msg = new String(packet.getData());
        String[] str = msg.split(" ");
        int seq = Integer.parseInt(str[1]);
        long timestamp = Long.parseLong(str[2]);
        Date now = new Date();
        long rtt = now.getTime() - timestamp;
        rtts[seq] = rtt;
        return rtt;
    }

}

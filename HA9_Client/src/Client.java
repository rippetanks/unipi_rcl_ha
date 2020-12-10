import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class Client {

    public static final int MAX_TIMEOUT = 2000;
    public static final int N_PING = 10;

    private final DatagramSocket socket;
    private final InetAddress address;
    private final int port;

    private int transmitted;
    private int received;
    private final long[] rtts = new long[N_PING];

    public Client(String name, int port) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        socket.setSoTimeout(MAX_TIMEOUT);
        address = InetAddress.getByName(name);
        this.port = port;
    }

    public void ping() throws IOException {
        transmitted = 0;
        received = 0;
        Arrays.fill(rtts, -1);

        for(int i = 0; i < N_PING; i++) {
            try {
                send(i);
                DatagramPacket packet = receive();
                long rtt = parseAndGetRTT(packet);
                System.out.printf(" RTT: %d ms\r\n", rtt);
            } catch(SocketTimeoutException e) {
                System.out.println(" RTT: *");
            }
        }
    }

    public void printStats() {
        System.out.print("\r\n\r\n");
        System.out.println("\t---- PING Statistics ----");
        float lossPer = (float) (transmitted - received) / transmitted * 100;
        System.out.printf("%d packets transmitted, %d packets received, %.0f%% packet loss\r\n", transmitted, received, lossPer);
        long min = Long.MAX_VALUE, max = 0, sum = 0;
        for(long rtt : rtts) {
            if(rtt >= 0) {
                if(rtt < min) {
                    min = rtt;
                }
                if(rtt > max) {
                    max = rtt;
                }
                sum += rtt;
            }
        }
        float avg = sum / (float) received;
        System.out.printf("round-trip (ms) min/avg/max = %d/%.2f/%d\r\n\r\n", min, avg, max);
    }

    private void send(int i) throws IOException {
        String msg = String.format("PING %d %d", i, System.currentTimeMillis());
        byte[] bytes = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
        socket.send(packet);
        transmitted++;
        System.out.print(msg);
    }

    private DatagramPacket receive() throws IOException {
        byte[] buffer = new byte[20];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        received++;
        return packet;
    }

    private long parseAndGetRTT(DatagramPacket packet) {
        String msg = new String(packet.getData());
        String[] str = msg.split(" ");
        int seq = Integer.parseInt(str[1]);
        long timestamp = Long.parseLong(str[2]);
        long rtt = System.currentTimeMillis() - timestamp;
        rtts[seq] = rtt;
        return rtt;
    }

}

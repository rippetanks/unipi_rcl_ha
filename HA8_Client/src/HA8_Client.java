import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HA8_Client {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        SocketAddress address = new InetSocketAddress("localhost", 8000);

        try(SocketChannel channel = SocketChannel.open(address)) {
            ByteBuffer readBuffer = ByteBuffer.allocate(16);
            String line;
            while (!(line = scanner.nextLine()).equals("exit")) {
                ByteBuffer buffer = ByteBuffer.wrap(line.getBytes());
                while(buffer.hasRemaining()) {
                    channel.write(buffer);
                }
                StringBuilder received = new StringBuilder();
                while(channel.read(readBuffer) >= 0) {
                    readBuffer.flip();
                    received.append(StandardCharsets.UTF_8.decode(readBuffer).toString());
                    readBuffer.clear();
                    if(received.toString().equals(line)) {
                        break;
                    }
                }
                System.out.println(received.toString());
            }
        }
    }

}

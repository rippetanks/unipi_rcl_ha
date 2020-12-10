import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Random;

class Server {

    public static final int P_LOSS = 25;
    public static final int MAX_WAIT = 1000;

    private final DatagramChannel channel;
    private final Selector selector;
    private final Random random;

    public Server(int port, Long seed) throws IOException {
        channel = DatagramChannel.open();
        selector = Selector.open();
        channel.socket().bind(new InetSocketAddress(port));
        channel.configureBlocking(false);

        random = new Random();
        if(seed != null) {
            random.setSeed(seed);
        }

        SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
        key.attach(new DataPacket());
    }

    public void start() throws IOException {
        while(true) {
            int n = selector.select();
            if(n == 0) continue;
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()) {
                try {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if(!key.isValid()) continue;
                    if(key.isReadable()) {
                        read(key);
                        if(canSend()) {
                            int delay = delay();
                            printDelayed(key, delay);
                            key.interestOps(SelectionKey.OP_WRITE);
                        } else {
                            printNotSent(key);
                        }
                    } else if(key.isWritable()) {
                        write(key);
                        key.interestOps(SelectionKey.OP_READ);
                    }
                } catch(IOException e) {
                    System.err.println("error " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void read(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        DataPacket dp = (DataPacket) key.attachment();
        dp.setAddress(channel.receive(dp.getBuffer()));
        dp.getBuffer().flip();
    }

    private void write(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        DataPacket dp = (DataPacket) key.attachment();
        channel.send(dp.getBuffer(), dp.getAddress());
        dp.getBuffer().clear();
    }

    private boolean canSend() {
        int ni = random.nextInt(100);
        return ni >= P_LOSS;
    }

    private int delay() {
        int ni = random.nextInt(MAX_WAIT);
        try {
            Thread.sleep(ni);
        } catch(InterruptedException e) {
            System.err.println("thread interrupted");
        }
        return ni;
    }

    private void printNotSent(SelectionKey key) {
        print(key, "not sent");
    }

    private void printDelayed(SelectionKey key, int delay) {
        print(key, String.format("delayed %d ms", delay));
    }

    private void print(SelectionKey key, String msg) {
        DataPacket dp = (DataPacket) key.attachment();
        String content = new String(dp.getBuffer().array());
        System.out.printf("%s > %s ACTION: %s\r\n", dp.getAddress(), content, msg);
    }

}

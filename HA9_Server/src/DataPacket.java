import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

class DataPacket implements Delayed {

    private final ByteBuffer buffer;
    private long start;
    private SocketAddress address;

    public DataPacket() {
        buffer = ByteBuffer.allocate(20);
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public void setAddress(SocketAddress address) {
        this.address = address;
    }

    public void start(long delay) {
        start = System.currentTimeMillis() + delay;
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
        long diff = start - System.currentTimeMillis();
        return timeUnit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return start - o.get
    }

}

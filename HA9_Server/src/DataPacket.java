import java.net.SocketAddress;
import java.nio.ByteBuffer;

class DataPacket {

    private final ByteBuffer buffer;
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

}

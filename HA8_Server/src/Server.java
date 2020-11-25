import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class Server {

    private final ServerSocketChannel server;
    private final Selector selector;

    public Server() throws IOException {
        server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.socket().bind(new InetSocketAddress(8000));

        selector = Selector.open();
    }

    public void start() throws IOException {
        server.register(selector, SelectionKey.OP_ACCEPT, null);

        while(true) {
            int n = selector.select();
            if(n == 0) {
                System.out.println("select return 0 keys");
                continue;
            }
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while(it.hasNext()) {
                SelectionKey key = it.next();
                if(key.isAcceptable()) {
                    accept(key);
                } else if(key.isReadable()) {
                    read(key);
                } else if(key.isWritable()) {
                    write(key);
                }
                it.remove();
            }
        }
    }

    private void accept(SelectionKey key) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel sc = ssc.accept();
            if (sc != null) {
                sc.configureBlocking(false);
                SelectionKey sck = sc.register(selector, SelectionKey.OP_READ);
                sck.attach(ByteBuffer.allocate(16));
            }
        } catch(IOException e) {
            close(key);
            System.err.println("can not accept connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key) {
        try {
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer buffer = (ByteBuffer) key.attachment();
            if(sc.read(buffer) == -1) {
                close(key);
                return;
            }
            buffer.flip();
            print("[READ]", buffer);
            key.interestOps(SelectionKey.OP_WRITE);
        } catch(IOException e) {
            close(key);
            System.err.println("can not read: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void write(SelectionKey key) {
        try {
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer buffer = (ByteBuffer) key.attachment();
            print("[WRITE]", buffer);
            sc.write(buffer);
            boolean hasRemaining = buffer.hasRemaining();
            buffer.compact();
            if (hasRemaining) {
                key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            } else {
                key.interestOps(SelectionKey.OP_READ);
            }
        } catch(IOException e) {
            close(key);
            System.err.println("can not write: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void close(SelectionKey key) {
        try {
            key.cancel();
            key.channel().close();
            System.out.println("closing connection");
        } catch(IOException e) {
            System.err.println("can not close: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void print(String prefix, ByteBuffer buffer) {
        System.out.printf("%s %s\r\n", prefix, StandardCharsets.UTF_8.decode(buffer).toString());
        buffer.flip();
    }

}

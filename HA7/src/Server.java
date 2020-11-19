import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static final int PORT = 6789;

    private final ExecutorService executor;

    public Server() {
        executor = Executors.newFixedThreadPool(10);
    }

    public void start() throws IOException {
        try(ServerSocket listener = new ServerSocket(PORT)) {
            while(true) {
                Socket socket = listener.accept();
                RequestHandler handler = new RequestHandler(socket);
                System.out.printf("request %s: %s%n", handler.getUuid(), socket.getRemoteSocketAddress());
                executor.submit(new RequestHandler(socket));
            }
        }
    }

}

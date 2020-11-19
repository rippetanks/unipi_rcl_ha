import java.io.IOException;

public class HA7 {

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.start();
            // if the method return an error occurred while opening the socket
        } catch(IOException e) {
            System.err.println("server fatal exception");
            e.printStackTrace();
        }
    }

}

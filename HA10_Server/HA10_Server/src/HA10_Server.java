import java.io.IOException;

public class HA10_Server {

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: java HA10_Server multicast_address");
            return;
        }
        try {
            Server server = new Server(args[0]);
            try {
                server.start();
            } finally {
                server.close();
            }
        } catch(IOException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

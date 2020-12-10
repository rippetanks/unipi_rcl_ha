import java.io.IOException;

public class HA10_Client {

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: java HA10_Server multicast_address");
            return;
        }
        try {
            Client client = new Client(args[0]);
            try {
                client.start();
            } finally {
                client.close();
            }
        } catch(IOException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

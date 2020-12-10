import java.io.IOException;

public class HA9_Client {

    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("Usage: server_name port");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            System.err.println("ERR -arg 2");
            return;
        }

        try {
            Client client = new Client(args[0], port);
            client.ping();
            client.printStats();
        } catch(IOException e) {
            System.err.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

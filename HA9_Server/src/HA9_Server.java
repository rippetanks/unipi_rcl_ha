import java.io.IOException;

public class HA9_Server {

    public static void main(String[] args) {
        if(args.length == 0 || args.length > 2) {
            System.out.println("Usage: port [seed]");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            System.err.println("ERR -arg 1");
            return;
        }
        Long seed = null;
        if(args.length == 2) {
            try {
                seed = Long.parseLong(args[1]);
            } catch(NumberFormatException e) {
                System.err.println("ERR -arg 2");
                return;
            }
        }

        try {
            Server server = new Server(port, seed);
            server.start();
        } catch(IOException e) {
            System.err.println("server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

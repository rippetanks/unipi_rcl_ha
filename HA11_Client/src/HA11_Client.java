import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class HA11_Client {

    public static void main(String[] args) {
        Client client = new Client(8000);
        try {
            client.start();
        } catch(RemoteException | NotBoundException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

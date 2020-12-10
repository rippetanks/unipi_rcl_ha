import java.rmi.RemoteException;

public class HA11_Server {

    public static void main(String[] args) {
        Congresso congresso = new Congresso(8000);
        try {
            congresso.start();
        } catch(RemoteException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

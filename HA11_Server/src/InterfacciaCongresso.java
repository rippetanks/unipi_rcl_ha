import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface InterfacciaCongresso extends Remote {

    List<Giornata> getGiornate() throws RemoteException;

    List<Sessione> getSessioni(int g)  throws RemoteException;

    List<Intervento> getInterventi(int g, int s) throws RemoteException;

    boolean newIntervento(int g, int s, Intervento intervento) throws RemoteException;

}

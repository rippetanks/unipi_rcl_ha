import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Congresso extends RemoteServer implements InterfacciaCongresso {

    private final int port;

    private final List<Giornata> giornate;

    public Congresso(int port) {
        this.port = port;
        giornate = new ArrayList<>(3);
        for(int i = 0; i < 3; i++) {
            giornate.add(new Giornata(i));
        }
    }

    public void start() throws RemoteException {
        InterfacciaCongresso stub = (InterfacciaCongresso) UnicastRemoteObject.exportObject(this, port);
        LocateRegistry.createRegistry(port);
        Registry registry = LocateRegistry.getRegistry(port);
        registry.rebind("congresso-server", stub);
    }

    @Override
    public List<Giornata> getGiornate() throws RemoteException {
        return Collections.unmodifiableList(giornate);
    }

    @Override
    public List<Sessione> getSessioni(int g) throws RemoteException {
        Giornata giornata = giornate.get(g);
        return giornata.getSessioni();
    }

    @Override
    public List<Intervento> getInterventi(int g, int s) throws RemoteException {
        Giornata giornata = giornate.get(g);
        Sessione sessione = giornata.getSessione(s);
        return sessione.getInterventi();
    }

    @Override
    public synchronized boolean newIntervento(int g, int s, Intervento intervento) throws RemoteException {
        Giornata giornata = giornate.get(g);
        Sessione sessione = giornata.getSessione(s);
        System.out.printf("[INTERVENTO] [%d] [%d] %s\r\n", g, s, intervento);
        return sessione.addIntervento(intervento);
    }

}

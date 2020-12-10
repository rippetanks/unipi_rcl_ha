import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class Client {

    private final int port;

    public Client(int port) {
        this.port = port;
    }

    public void start() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(port);
        InterfacciaCongresso congresso = (InterfacciaCongresso) registry.lookup("congresso-server");

        int op;
        Scanner in = new Scanner(System.in);

        do {
            printMenu();
            op = in.nextInt();
            try {
                doOp(congresso, in, op);
            } catch(Exception e) {
                System.err.println("ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        } while(op != 0);
    }

    private void doOp(InterfacciaCongresso congresso, Scanner in, int op) throws RemoteException {
        switch (op) {
            case 1:
                System.out.print("giorno [1-3] (altro per annullare): ");
                int giorno = in.nextInt();
                if(giorno > 0 && giorno <= 3) {
                    System.out.print("sessione [1-12] (altro per annullare): ");
                    int sessione = in.nextInt();
                    if(sessione > 0 && sessione <= 12) {
                        System.out.print("nome (vuoto per annullare): ");
                        String nome = in.next();
                        if(!nome.isEmpty()) {
                            Intervento intervento = new Intervento(nome);
                            congresso.newIntervento(giorno-1, sessione-1, intervento);
                            System.out.println("**\tregistrato\t**");
                        } else {
                            System.out.println("**\tannullato\t**");
                        }
                    } else {
                        System.out.println("**\tannullato\t**");
                    }
                } else {
                    System.out.println("**\tannullato\t**");
                }
                break;
            case 2:
                List<Giornata> giornate = congresso.getGiornate();
                printProgramma(giornate);
                break;
            case 0:
                System.out.println("**\tARRIVEDERCI\t**");
                break;
            default:
                System.err.println("Comando non riconosciuto!");
                break;
        }
    }

    private void printMenu() {
        System.out.println("\r\n**\tMENU\t**");
        System.out.println("**\t\t1 - registrare uno speaker");
        System.out.println("**\t\t2 - visualizza programma");
        System.out.println("**\t\t0 - esci");
        System.out.println("**\t    \t**\r\n");
    }

    private void printProgramma(List<Giornata> giornate) {
        for(Giornata g : giornate) {
            System.out.printf("giornata %d\r\n", g.getN());
            for(Sessione s : g.getSessioni()) {
                System.out.printf("\tsessione %d: ", s.getN());
                for(Intervento i : s.getInterventi()) {
                    System.out.printf("%s, ", i.getNome());
                }
                System.out.println();
            }
        }
    }

}

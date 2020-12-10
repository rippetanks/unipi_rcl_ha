import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sessione implements Serializable {

    private static final long serialVersionUID = 7780552017389042955L;

    private final int n;

    private final List<Intervento> interventi;

    public Sessione(int n) {
        this.n = n;
        interventi = new ArrayList<>(5);
    }

    public int getN() {
        return n;
    }

    public boolean addIntervento(Intervento intervento) {
        if(interventi.size() <= 5) {
            interventi.add(intervento);
            return true;
        } else {
            return false;
        }
    }

    public List<Intervento> getInterventi() {
        return Collections.unmodifiableList(interventi);
    }

    public Intervento getIntervento(int i) {
        return interventi.get(i);
    }

    @Override
    public String toString() {
        return "Sessione{" +
                "n=" + n +
                ", interventi=" + interventi +
                '}';
    }

}

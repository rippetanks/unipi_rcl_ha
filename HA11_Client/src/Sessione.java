import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Sessione implements Serializable {

    private static final long serialVersionUID = 7780552017389042955L;

    private int n;
    private List<Intervento> interventi;

    public int getN() {
        return n;
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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Giornata implements Serializable {

    private static final long serialVersionUID = -6248976974361696949L;

    private int n;
    private List<Sessione> sessioni;

    public int getN() {
        return n;
    }

    public List<Sessione> getSessioni() {
        return Collections.unmodifiableList(sessioni);
    }

    public Sessione getSessione(int i) {
        return sessioni.get(i);
    }

    @Override
    public String toString() {
        return "Giornata{" +
                "n=" + n +
                ", sessioni=" + sessioni +
                '}';
    }

}

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Giornata implements Serializable {

    private static final long serialVersionUID = -6248976974361696949L;

    private final int n;

    private final List<Sessione> sessioni;

    public Giornata(int n) {
        this.n = n;
        sessioni = new ArrayList<>(12);
        for(int i = 0; i < 12; i++) {
            sessioni.add(new Sessione(i));
        }
    }

    public int getN() {
        return n;
    }

    public boolean addSessione(Sessione sessione) {
        if(sessioni.size() <= 12) {
            sessioni.add(sessione);
            return true;
        } else {
            return false;
        }
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

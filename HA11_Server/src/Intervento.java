import java.io.Serializable;

public class Intervento implements Serializable {

    private static final long serialVersionUID = -1684806553483814284L;

    private final String nome;

    public Intervento(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Intervento{" +
                "nome='" + nome + '\'' +
                '}';
    }

}

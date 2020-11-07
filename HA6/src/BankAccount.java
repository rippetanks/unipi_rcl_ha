import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankAccount {

    private String name;
    private List<Movement> movements;

    protected BankAccount() {

    }

    public BankAccount(String name) {
        this.name = name;
        this.movements = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Movement> getMovements() {
        return Collections.unmodifiableList(movements);
    }

    public void addMovement(Movement movement) {
        movements.add(movement);
    }

}

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The consumer elaborates a bank account, counting all the reasons present in the movements.
 */
public class AccountConsumer implements Runnable {

    private final BankAccount account;
    private final Map<Reason, AtomicInteger> counter;

    public AccountConsumer(BankAccount account, Map<Reason, AtomicInteger> counter) {
        this.account = account;
        this.counter = counter;
    }

    @Override
    public void run() {
        //System.out.println("[CONSUMER] account name -> " + account.getName());
        for(Movement movement : account.getMovements()) {
            AtomicInteger c = counter.get(movement.getReason());
            c.getAndIncrement();
        }
    }

}

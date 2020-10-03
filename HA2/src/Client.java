import java.util.Random;

/**
 * A client is represented by a task that wait a random time between 0 and 10 seconds to be executed.
 *
 * The client takes a ticket at the entrance of the post office to show it at the counter.
 *
 * The ticket uniquely identify a client.
 */
public class Client implements Runnable {

    public static final long MAX_WAIT = 10000; // 10 s

    private Integer ticket;

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.printf("[%s] [start] client %d \n", name, ticket);
        long wait = Math.abs(new Random().nextLong()) % MAX_WAIT;
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            System.out.printf("[%s] [abort] client %d \n", name, ticket);
        }
        System.out.printf("[%s] [end] client %d [time] %d (ms) \n", name, ticket, wait);
    }

}

import java.util.concurrent.*;

public class PostOffice {

    // size of the second waiting room (client in line + client at the counter)
    // K must be greater than or equal to N_COUNTER
    public static final int K = 8;

    public static final int N_COUNTER = 4;

    private final ExecutorService counterService;

    // this thread simulates the movements of clients from the first waiting room to the second
    private Thread queueThread;

    // first waiting room queue, no size limit
    private final BlockingQueue<Client> firstRoomTail = new LinkedBlockingQueue<>();

    // queue ticket dispenser
    private int queueTicketDispenser;

    // if the post office is closing, no more customer are accepted
    private volatile boolean isPostalOfficeClosing = false;

    public PostOffice() {
        // second waiting room queue, limited size (K - N_COUNTER)
        BlockingQueue<Runnable> secondRoomTail = new ArrayBlockingQueue<>(K-N_COUNTER);

        // default -> counters remain open even if there are no clients
        counterService = new ThreadPoolExecutor(4, 4, 0, TimeUnit.MILLISECONDS, secondRoomTail);

        // optional -> the operator closes the counter after no customers show up at his counter in a certain period of time
        //counterService = new ThreadPoolExecutor(0, 4, 1, TimeUnit.SECONDS, secondRoomTail);
    }

    public void enter(Client client) {
        if(isPostalOfficeClosing) {
            throw new IllegalStateException("post office is closing");
        }

        queueTicketDispenser += 1;
        client.setTicket(queueTicketDispenser);

        firstRoomTail.add(client);
        System.out.printf("[post office] [enter] client %d \n", client.getTicket());
    }

    public void start() {
        startQueueThread();
    }

    public void stop() throws InterruptedException {
        isPostalOfficeClosing = true;
        queueThread.interrupt();
        queueThread.join();
        counterService.shutdown();
        // wait a max of 60 seconds for the thread pool termination
        counterService.awaitTermination(60, TimeUnit.SECONDS);
        if(!counterService.shutdownNow().isEmpty()) {
            System.out.println("some client were not served");
        }
    }

    private void startQueueThread() {
        queueThread = new Thread(() -> {
            while(!(isPostalOfficeClosing && firstRoomTail.isEmpty())) {
                try {
                    Client client = firstRoomTail.take();
                    while(true) {
                        try {
                            counterService.submit(client);
                            break;
                        } catch(RejectedExecutionException e) {
                            Thread.yield();
                        }
                    }
                    System.out.printf("[post office] client %d enter second waiting room \n", client.getTicket());
                } catch(InterruptedException e) {
                    System.out.println("PostOffice internal queue thread interrupted");
                }
            }
        });
        queueThread.start();
    }

}

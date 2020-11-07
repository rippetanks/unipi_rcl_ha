import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HA6 {

    public static final String PATH = "bank.json";

    public static final int MAX_THREAD = 10;

    public static final int N_ACCOUNTS = 100;
    public static final int MIN_MOV_PER_ACCOUNT = 100;
    public static final int MAX_MOV_PER_ACCOUNT = 1000;

    public static void main(String[] args) {
        // generate json file
        FileGenerator generator = new FileGenerator(N_ACCOUNTS, MIN_MOV_PER_ACCOUNT, MAX_MOV_PER_ACCOUNT, PATH);
        Thread generatorThread = new Thread(generator);
        System.out.println("generation bank accounts json file start");
        generatorThread.start();

        // prepare consumer thread pool & global counter
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
        ExecutorService executor =
                new ThreadPoolExecutor(MAX_THREAD / 2, MAX_THREAD, 2, TimeUnit.SECONDS, queue);
        Map<Reason, AtomicInteger> counter = getCounterInitialized();
        // prepare producer
        AccountProducer producer = new AccountProducer(PATH, executor, counter);
        Thread producerThread = new Thread(producer);

        try {
            generatorThread.join();
            System.out.println("generation bank accounts json file end");

            producerThread.start();
            producerThread.join();

            executor.shutdown();
            if(executor.awaitTermination(30, TimeUnit.SECONDS)) {
               System.out.println("thread pool terminated successfully");
            } else {
                System.out.println("thread pool ended without executing all the assigned tasks");
            }

            System.out.println("\n\n-- FINAL RESULT --");
            counter.forEach((k, v) -> System.out.printf("\t%s = %d\n", k, v.get()));
        } catch(InterruptedException e) {
            System.err.println("main thread interrupted");
        }
    }

    private static Map<Reason, AtomicInteger> getCounterInitialized() {
        Map<Reason, AtomicInteger> counter = new EnumMap<>(Reason.class);
        counter.put(Reason.ACCREDITO, new AtomicInteger());
        counter.put(Reason.BOLLETTINO, new AtomicInteger());
        counter.put(Reason.BONIFICO, new AtomicInteger());
        counter.put(Reason.F24, new AtomicInteger());
        counter.put(Reason.PAGO_BANCOMAT, new AtomicInteger());
        return counter;
    }

}

import java.util.Random;

public abstract class User implements Runnable {

    public static final int MAX_K = 10;
    public static final int MAX_WAIT = 5000; // 5 s

    protected final int k;

    protected User() {
        k = Math.abs(new Random().nextInt()) % MAX_K + 1;
    }

    public int getK() {
        return k;
    }

    @Override
    public void run() {
        long wait = Math.abs(new Random().nextLong()) % MAX_WAIT;
        try {
            Thread.sleep(wait);
        } catch(InterruptedException e) {
            System.out.printf("interrupt thread %s", Thread.currentThread().getName());
        }
    }

}


public abstract class User implements Runnable {

    // maximum waiting time between one request and another
    public static final int MAX_WAIT = 10000; // 10 s

    // maximum use time of a workstation or the whole laboratory
    public static final int MAX_OCCUPATION = 5000; // 5 s

    protected final Tutor tutor;
    protected final int k;

    protected int index;

    protected User(int k, Tutor tutor) {
        if(k < 0) {
            throw new IllegalArgumentException("k can not be negative");
        }
        if(tutor == null) {
            throw new NullPointerException("tutor can not be null");
        }
        this.k = k;
        this.tutor = tutor;
    }

    public int getK() {
        return k;
    }

    @Override
    public void run() {
        long wait = (long) (Math.random() * MAX_WAIT); // waiting time between two requests
        long occupationTime = (long) (Math.random() * MAX_OCCUPATION); // usage time
        //long wait = 10;
        //long occupationTime = 20;
        try {
            for(int i = 0; i < k; i++) {
                acquire();
                Thread.sleep(occupationTime);
                release();
                if(i < k - 1) { // skip last wait
                    Thread.sleep(wait);
                }
            }
        } catch(InterruptedException e) {
            System.out.printf("thread %s interrupted\n", Thread.currentThread().getName());
        }
    }

    protected abstract void acquire();

    protected abstract void release();

}

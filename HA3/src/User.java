
/**
 * Basic abstract representation of a laboratory user.
 */
public abstract class User implements Runnable {

    // maximum waiting time between one request and another
    public static final int MAX_WAIT = 10000; // 10 s

    // maximum use time of a workstation or the whole laboratory
    public static final int MAX_OCCUPATION = 5000; // 5 s

    protected final Tutor tutor;
    protected final int k;

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

}

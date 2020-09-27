
public class PiGreco implements Runnable {

    private final double accuracy;

    private double pi;

    public PiGreco(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getPiGreco() {
        return pi;
    }

    public void run() {
        long den = 1;
        long count = 0;
        while(Math.abs(Math.PI - pi) >= accuracy) {
            if(count % 2 == 0) {
                pi = pi + 4f / den;
            } else {
                pi = pi - 4f / den;
            }
            den += 2;
            count++;
            if(Thread.currentThread().isInterrupted()) {
                System.out.println("Thread " + Thread.currentThread().getName() + " interrupted");
                return;
            }
        }
        System.out.println("Thread " + Thread.currentThread().getName() + " ends in accuracy achieved");
    }

}

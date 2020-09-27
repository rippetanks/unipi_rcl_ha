
public class HA1 {

    public static void main(String[] args) throws InterruptedException {
        if(args.length != 2) {
            System.out.println("Usage: accuracy max-time (ms)");
            return;
        }
        double accuracy;
        try {
            accuracy = Double.parseDouble(args[0]);
            System.out.println("Accuracy -> " + accuracy);
        } catch(NumberFormatException e) {
            System.out.println("Accuracy must be a double");
            throw e;
        }
        long maxTime;
        try {
            maxTime = Long.parseLong(args[1]);
            System.out.println("MaxTime -> " + maxTime);
        } catch(NumberFormatException e) {
            System.out.println("MaxTime must be a long");
            throw e;
        }

        // create PiGreco and start thread
        PiGreco piGreco = new PiGreco(accuracy);
        Thread t = new Thread(piGreco);
        t.start();

        t.join(maxTime); // wait at most MaxTime for thread termination
        if(t.isAlive()) {
            // if the thread is still alive, send interrupt to stop the calculation of PI
            t.interrupt();
            t.join();
        }

        System.out.println("PI GRECO -> " + piGreco.getPiGreco());
    }

}

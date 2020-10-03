
public class HA2 {

    public static void main(String[] args) {
        PostOffice postOffice = new PostOffice();

        // default
        try {
            for (int i = 0; i < 20; i++) {
                postOffice.enter(new Client());
            }
            postOffice.start();
            postOffice.stop();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        // optional
        /*postOffice.start();
        try {
            for (int i = 0; i < 4; i++) {
                postOffice.enter(new Client());
                Thread.sleep(8000);
            }
            for (int i = 0; i < 10; i++) {
                postOffice.enter(new Client());
                Thread.sleep(500);
            }
            for (int i = 0; i < 4; i++) {
                postOffice.enter(new Client());
                Thread.sleep(5000);
            }
            postOffice.stop();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }*/
    }

}


public class HA5 {

    private static final int K = 4;

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: path");
            return;
        }

        // queue
        FileCrawlerQueue queue = new FileCrawlerQueue();

        // producer
        FileCrawlerProducer producer = new FileCrawlerProducer(queue, args[0]);
        Thread threadProducer = new Thread(producer);
        threadProducer.start();

        // consumers
        for(int i = 0; i < K; i++) {
            FileCrawlerConsumer consumer = new FileCrawlerConsumer(queue);
            Thread threadConsumer = new Thread(consumer);
            threadConsumer.start();
        }
    }

}

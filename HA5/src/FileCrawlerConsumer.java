import java.io.File;

/**
 * The consumer takes items (directories) from the queue and prints the content (file list).
 */
public class FileCrawlerConsumer implements Runnable {

    private final FileCrawlerQueue queue;

    public FileCrawlerConsumer(FileCrawlerQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        File file;
        try {
            while ((file = queue.dequeue()) != null) {
                if(file.isDirectory()) {
                    File[] files = file.listFiles();
                    if(files != null) {
                        for (File f : files) {
                            if (f.isFile()) {
                                System.out.println(f);
                            }
                        }
                    }
                }
            }
        } catch(InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted");
        }
    }

}

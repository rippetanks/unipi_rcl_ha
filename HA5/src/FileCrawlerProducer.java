import java.io.File;

/**
 * The producer visits the directory indicated as a parameter and recursively queuing all subdirectories.
 */
public class FileCrawlerProducer implements Runnable {

    private final FileCrawlerQueue queue;
    private final String path;

    public FileCrawlerProducer(FileCrawlerQueue queue, String path) {
        this.queue = queue;
        this.path = path;
    }

    @Override
    public void run() {
        explore(path);
        queue.close();
    }

    private void explore(String path) {
        File directory = new File(path);
        if(directory.isDirectory()) {
            queue.enqueue(directory);
            File[] files = directory.listFiles();
            if(files != null) {
                for(File file : files) {
                    if(file.isDirectory()) {
                        explore(file.getPath());
                    }
                }
            }
        }
    }

}

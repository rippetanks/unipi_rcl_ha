import java.io.File;
import java.util.LinkedList;

/**
 * This queue is implemented as a LinkedList.
 */
public class FileCrawlerQueue {

    private final LinkedList<File> queue;
    // true if the producer has finished browsing directories, false otherwise
    private boolean closed = false;

    public FileCrawlerQueue() {
        queue = new LinkedList<>();
    }

    /**
     * This method queue the file e and notifies a possibly pending consumer.
     *
     * @param e element
     */
    public synchronized void enqueue(File e) {
        queue.add(e);
        notify();
    }

    /**
     * Removes an item from the head of the queue.
     * If the queue is empty and has not been closed, it waits for an item.
     * If the queue is empty and has been closed, it returns null.
     *
     * @return a File (directory) or null if the queue is empty and closed
     * @throws InterruptedException
     */
    public synchronized File dequeue() throws InterruptedException {
        while(queue.isEmpty() && !closed) {
            wait();
        }
        if(!queue.isEmpty()) {
            return queue.remove();
        } else {
            return null;
        }
    }

    /**
     * This method closes the queue and notifies all consumers to allow them to terminate.
     */
    public synchronized void close() {
        closed = true;
        notifyAll();
    }

}

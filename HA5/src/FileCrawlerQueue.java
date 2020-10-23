import java.util.LinkedList;
import java.util.List;

public class FileCrawlerQueue {

    private final List<String> queue;
    private final Object object;

    public FileCrawlerQueue() {
        queue = new LinkedList<>();
        object = new Object();
    }

}

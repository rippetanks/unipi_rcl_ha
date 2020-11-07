import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The producer reads and parse the json file and sends the bank account data to the consumer thread pool.
 */
public class AccountProducer implements Runnable {

    private final ExecutorService executor;
    private final Path path;
    private final Map<Reason, AtomicInteger> counter;

    public AccountProducer(String path, ExecutorService executor, Map<Reason, AtomicInteger> counter) {
        this.path = Paths.get(path);
        this.executor = executor;
        this.counter = counter;
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            long size = Files.size(path);
            if(size > Integer.MAX_VALUE) {
                throw new IOException("file too big");
            }
            byte[] bytes = new byte[(int) size];
            int index = 0;
            try(ReadableByteChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
                ByteBuffer buffer = ByteBuffer.allocateDirect(2048);
                while(channel.read(buffer) != -1) {
                    buffer.flip();
                    while(buffer.hasRemaining()) {
                        bytes[index++] = buffer.get();
                    }
                    buffer.clear();
                }
                // jackson -> parse bytes (json) to Java objects
                List<BankAccount> accounts = mapper.readValue(bytes, new TypeReference<>() { });
                System.out.println("[PRODUCER] accounts -> " + accounts.size());
                for(BankAccount account : accounts) {
                    executor.submit(new AccountConsumer(account, counter));
                }
            }
        } catch(IOException e) {
            System.err.println("can not open/parse bank accounts json file");
            System.err.println(e.getMessage());
        }
    }

}

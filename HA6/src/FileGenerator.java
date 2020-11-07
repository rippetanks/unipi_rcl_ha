import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class FileGenerator implements Runnable {

    private final int nAccount;
    private final int minMov;
    private final int maxMov;
    private final Path path;

    public FileGenerator(int nAccount, int minMov, int maxMov, String path) {
        this.nAccount = nAccount;
        this.minMov = minMov;
        this.maxMov = maxMov;
        this.path = Paths.get(path);
    }

    @Override
    public void run() {
        List<BankAccount> accounts = generate();
        try {
            write(accounts);
        } catch(IOException e) {
            System.err.println("can not generate bank json file");
            System.err.println(e.getMessage());
        }
    }

    private void write(List<BankAccount> accounts) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes = mapper.writeValueAsBytes(accounts);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try(WritableByteChannel channel =
                    FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            while(buffer.hasRemaining()) {
                channel.write(buffer);
            }
        }
        /*
        try(FileWriter writer = new FileWriter(file)) {
            mapper.writeValue(writer, accounts);
        }
        */
    }

    private List<BankAccount> generate() {
        List<BankAccount> accounts = new ArrayList<>();
        Random random = new Random();

        Reason[] reasons = Reason.values();
        Map<Reason, Integer> reasonCount = new EnumMap<>(Reason.class);

        for(int i = 0; i < nAccount; i++) {
            BankAccount account = generateAccount(random, "bank_" + i, reasons, reasonCount);
            accounts.add(account);
        }

        printInfo(reasonCount, accounts);
        return accounts;
    }

    private void printInfo(Map<Reason, Integer> counter, List<BankAccount> accounts) {
        System.out.println("\n-- REASON COUNT --");
        counter.forEach((k, v) -> System.out.println(k + " -> " + v));
        System.out.println("--              --");
        System.out.println("TOTAL BANK ACCOUNT GENERATED -> " + accounts.size());
        System.out.println("--              --\n");
    }

    private BankAccount generateAccount(Random random, String name, Reason[] reasons, Map<Reason, Integer> counter) {
        BankAccount account = new BankAccount(name);
        Date now = new Date();

        int nMov = random.nextInt(maxMov - minMov + 1) + minMov;
        for(int j = 0; j < nMov; j++) {
            Reason reason = reasons[j % reasons.length];
            Movement movement = new Movement(now, reason);
            account.addMovement(movement);

            Integer count = counter.get(reason);
            if(count == null) {
                count = 0;
            }
            counter.put(reason, count + 1);
        }
        return account;
    }

}

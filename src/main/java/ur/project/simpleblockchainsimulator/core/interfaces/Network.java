package ur.project.simpleblockchainsimulator.core.interfaces;

import ur.project.simpleblockchainsimulator.core.Transaction;

import java.util.Optional;

public interface Network extends AutoCloseable {
    void queueTransaction(Transaction transaction);
    void start();
    void close();
    Optional<Transaction> searchTransaction(String hash);
}

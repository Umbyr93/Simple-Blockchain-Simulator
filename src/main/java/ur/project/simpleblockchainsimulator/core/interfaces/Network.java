package ur.project.simpleblockchainsimulator.core.interfaces;

import ur.project.simpleblockchainsimulator.core.Transaction;

import java.util.Optional;

public interface Network {
    void queueTransaction(Transaction transaction);
    void start();
    void stop();
    Optional<Transaction> searchTransaction(String hash);
}

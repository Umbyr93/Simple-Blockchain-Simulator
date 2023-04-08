package ur.project.simpleblockchainsimulator.core.interfaces;

import ur.project.simpleblockchainsimulator.transfer.Transaction;

public interface Network {
    void queueTransaction(Transaction transaction);
    void processTransaction();
    void start();
    void stop();
}

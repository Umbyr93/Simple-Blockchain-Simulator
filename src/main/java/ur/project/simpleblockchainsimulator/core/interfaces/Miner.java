package ur.project.simpleblockchainsimulator.core.interfaces;

import ur.project.simpleblockchainsimulator.core.SimpleBlock;
import ur.project.simpleblockchainsimulator.core.SimpleBlockchain;
import ur.project.simpleblockchainsimulator.core.Transaction;

import java.util.Optional;

public interface Miner {
    Optional<SimpleBlock> mine(Transaction transaction);
    Optional<SimpleBlock> resumeMining();
    void cleanPool();
    boolean hasPendingTransactions();
    void setBlockChain(SimpleBlockchain blockchain);
}

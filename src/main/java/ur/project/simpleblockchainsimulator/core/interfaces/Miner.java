package ur.project.simpleblockchainsimulator.core.interfaces;

import ur.project.simpleblockchainsimulator.core.Blockchain;
import ur.project.simpleblockchainsimulator.transfer.Transaction;

public interface Miner {
    void mine(Transaction transaction);
    void setBlockchain(Blockchain blockchain);
}

package ur.project.simpleblockchainsimulator.core;

import lombok.extern.slf4j.Slf4j;
import ur.project.simpleblockchainsimulator.core.interfaces.Miner;
import ur.project.simpleblockchainsimulator.core.interfaces.Network;
import ur.project.simpleblockchainsimulator.transfer.Transaction;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class SimpleNetwork implements Network {
    private final Miner miner;
    private final BlockingQueue<Transaction> transactionQueue;
    private boolean isStarted;

    public SimpleNetwork(Blockchain blockchain, Miner miner) {
        this.miner = miner;
        this.miner.setBlockchain(blockchain);
        this.transactionQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void queueTransaction(Transaction transaction) {
        log.info("Queueing transaction with hash: " + transaction.getHash());
        if(isStarted)
            new Thread(() -> transactionQueue.add(transaction)).start();
        else
            throw new RuntimeException("Network offline, couldn't connect");
    }

    @Override
    public void processTransaction() {
        while(isStarted) {
            try {
                //Stays blocked until a new element is added to the queue
                miner.mine(transactionQueue.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void start() {
        isStarted = true;
        log.info("Network started");
        new Thread(this::processTransaction).start();
    }

    @Override
    public void stop() {
        isStarted = false;
        log.info("Network stopped");
    }
}

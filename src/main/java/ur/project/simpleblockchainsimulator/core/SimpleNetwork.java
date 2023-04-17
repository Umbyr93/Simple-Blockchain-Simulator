package ur.project.simpleblockchainsimulator.core;

import lombok.extern.slf4j.Slf4j;
import ur.project.simpleblockchainsimulator.core.enums.TransactionStatus;
import ur.project.simpleblockchainsimulator.core.interfaces.Miner;
import ur.project.simpleblockchainsimulator.core.interfaces.Network;
import ur.project.simpleblockchainsimulator.core.interfaces.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class SimpleNetwork implements Network {
    private final Miner miner;
    private final Node node;
    private final BlockingQueue<Transaction> transactionQueue;
    private final List<Transaction> allTransactions = new ArrayList<>();
    private boolean isStarted;

    public SimpleNetwork(SimpleBlockchain blockchain, Node node, Miner miner) {
        node.setBlockChain(blockchain);
        miner.setBlockChain(blockchain);

        this.miner = miner;
        this.node = node;
        this.transactionQueue = new LinkedBlockingQueue<>();
    }

    private void processTransaction() {
        while(isStarted) {
            try {
                Optional<SimpleBlock> minedBlock;
                if(miner.hasPendingTransactions()) {
                    minedBlock = miner.resumeMining();
                } else {
                    //Stays blocked until a new element is added to the queue
                    // so this isn't actively cycling.
                    minedBlock = miner.mine(transactionQueue.take());
                }

                if(minedBlock.isPresent()) {
                    node.approve(minedBlock.get());
                    allTransactions.forEach(t -> changeTransactionStatus(t, minedBlock.get().getTransactions()));
                    miner.cleanPool();
                }
            } catch (InterruptedException ie) {
                throw new RuntimeException(ie);
            } catch (ApprovationException ae) {
                log.error(ae.getMessage());
            }
        }
    }

    private void changeTransactionStatus(Transaction transactionToChange, List<Transaction> transactions) {
        boolean anyMatch = transactions.stream()
                .anyMatch(t -> t.getHash().equals(transactionToChange.getHash()));

        if(anyMatch)
            transactionToChange.setStatus(TransactionStatus.AUTHORIZED);
    }

    @Override
    public void queueTransaction(Transaction transaction) {
        log.info("Queueing transaction {}", transaction.fourDigitsHash());
        if(isStarted)
            new Thread(() -> {
                transactionQueue.add(transaction);
                allTransactions.add(transaction);
            }).start();
        else
            throw new RuntimeException("Network offline, couldn't connect");
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

    @Override
    synchronized public Optional<Transaction> searchTransaction(String hash) {
        return allTransactions.stream()
                .filter(t -> hash.equals(t.getHash()))
                .findFirst();
    }
}

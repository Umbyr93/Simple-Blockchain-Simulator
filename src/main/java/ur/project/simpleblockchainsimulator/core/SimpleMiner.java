package ur.project.simpleblockchainsimulator.core;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import ur.project.simpleblockchainsimulator.core.interfaces.Miner;
import ur.project.simpleblockchainsimulator.utils.SHA256;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SimpleMiner implements Miner {
    private final SimpleBlockchain blockchain;
    private final List<Transaction> transactionPool = new ArrayList<>();

    public SimpleMiner(SimpleBlockchain blockchain) {
        this.blockchain = blockchain;
    }

    @Override
    public Optional<SimpleBlock> mine(Transaction transaction) {
        transactionPool.add(transaction);
        log.info("Added transaction {} to the pool", transaction.fourDigitsHash());

        if(transactionPool.size() >= blockchain.getBlocksSize()) {
            SimpleBlock newBlock = blockchain.createNewBlock(transactionPool.subList(0, transactionPool.size()));
            proofOfWork(newBlock);

            return Optional.of(newBlock);
        }

        return Optional.empty();
    }

    @Override
    public Optional<SimpleBlock> resumeMining() {
        SimpleBlock newBlock = blockchain.createNewBlock(transactionPool.subList(0, transactionPool.size()));
        proofOfWork(newBlock);

        return Optional.of(newBlock);
    }

    private void proofOfWork(SimpleBlock newBlock) {
        log.info("Starting proof of work with difficulty: {}", blockchain.getMiningDifficulty());
        String target = "0".repeat(blockchain.getMiningDifficulty());

        String serializedData = new Gson().toJson(transactionPool);
        String blockHash = SHA256.generateHash(newBlock.getTimestamp() + newBlock.getHeight() + serializedData +
                newBlock.getPreviousHash());

        long testNonce = 0;
        boolean nonceFound = false;
        while(!nonceFound) {
            String testHash = SHA256.generateHash(blockHash + testNonce);

            if(testHash.substring(0, blockchain.getMiningDifficulty()).equals(target)) {
                newBlock.setHash(testHash);
                newBlock.setNonce(String.valueOf(testNonce));

                nonceFound = true;
                log.info("Nonce found, created block {}. Sending to the network for validation...", newBlock.fourDigitsHash());
            }

            testNonce++;
        }
    }

    @Override
    public void cleanPool() {
        //Removes the transactions we just added to the blockchain
        transactionPool.subList(0, blockchain.getBlocksSize()).clear();
    }

    @Override
    public boolean hasPendingTransactions() {
        return transactionPool.size() >= blockchain.getBlocksSize();
    }
}

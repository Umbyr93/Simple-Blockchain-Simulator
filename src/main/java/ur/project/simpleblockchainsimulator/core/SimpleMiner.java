package ur.project.simpleblockchainsimulator.core;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import ur.project.simpleblockchainsimulator.core.interfaces.Miner;
import ur.project.simpleblockchainsimulator.transfer.Transaction;
import ur.project.simpleblockchainsimulator.utils.SHA256;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SimpleMiner implements Miner {
    private Blockchain blockchain;
    private List<Transaction> transactionPool = new ArrayList<>();

    @Override
    public void setBlockchain(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    @Override
    public void mine(Transaction transaction) {
        transactionPool.add(transaction);
        log.info("Retrieved transaction with hash: {}", transaction.getHash());

        if(blockchain.getBlocksSize() == transactionPool.size()) {
            Block block = blockchain.createNewBlock(transactionPool);
            proofOfWork(block);
            blockchain.addBlock(block);
        }
    }

    private void proofOfWork(Block block) {
        log.info("Starting proof of work with difficulty: {}", blockchain.getMiningDifficulty());
        String target = "0".repeat(blockchain.getMiningDifficulty());

        String serializedData = new Gson().toJson(transactionPool);
        String blockHash = SHA256.generateHash(block.getTimestamp() + block.getHeight() + serializedData +
                block.getPreviousHash());

        long testNonce = 0;
        boolean nonceFound = false;
        while(!nonceFound) {
            String testHash = SHA256.generateHash(blockHash + testNonce);

            if(testHash.substring(0, blockchain.getMiningDifficulty()).equals(target)) {
                block.setHash(testHash);
                block.setNonce(String.valueOf(testNonce));
                transactionPool = new ArrayList<>();

                nonceFound = true;
                log.info("Nonce found, linking the new block to the Blockchain");
            }

            testNonce++;
        }
    }
}

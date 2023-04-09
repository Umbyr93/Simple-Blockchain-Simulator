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
    private final SimpleBlockchain simpleBlockchain;
    private List<Transaction> transactionPool = new ArrayList<>();

    public SimpleMiner(SimpleBlockchain simpleBlockchain) {
        this.simpleBlockchain = simpleBlockchain;
    }

    @Override
    public void mine(Transaction transaction) {
        transactionPool.add(transaction);
        log.info("Retrieved transaction with hash: {}", transaction.getHash());

        if(simpleBlockchain.getBlocksSize() == transactionPool.size()) {
            SimpleBlock simpleBlock = simpleBlockchain.createNewBlock(transactionPool);
            proofOfWork(simpleBlock);
            simpleBlockchain.addBlock(simpleBlock);
        }
    }

    private void proofOfWork(SimpleBlock simpleBlock) {
        log.info("Starting proof of work with difficulty: {}", simpleBlockchain.getMiningDifficulty());
        String target = "0".repeat(simpleBlockchain.getMiningDifficulty());

        String serializedData = new Gson().toJson(transactionPool);
        String blockHash = SHA256.generateHash(simpleBlock.getTimestamp() + simpleBlock.getHeight() + serializedData +
                simpleBlock.getPreviousHash());

        long testNonce = 0;
        boolean nonceFound = false;
        while(!nonceFound) {
            String testHash = SHA256.generateHash(blockHash + testNonce);

            if(testHash.substring(0, simpleBlockchain.getMiningDifficulty()).equals(target)) {
                simpleBlock.setHash(testHash);
                simpleBlock.setNonce(String.valueOf(testNonce));
                transactionPool = new ArrayList<>();

                nonceFound = true;
                log.info("Nonce found, linking the new block to the Blockchain");
            }

            testNonce++;
        }
    }
}

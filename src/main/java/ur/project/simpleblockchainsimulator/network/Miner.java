package ur.project.simpleblockchainsimulator.network;

import com.google.gson.Gson;
import ur.project.simpleblockchainsimulator.chain.Block;
import ur.project.simpleblockchainsimulator.chain.Blockchain;
import ur.project.simpleblockchainsimulator.transfer.Transaction;
import ur.project.simpleblockchainsimulator.utils.SHA256;

import java.util.ArrayList;
import java.util.List;

public class Miner {
    private List<Transaction> transactionPool = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactionPool.add(transaction);

        if(Blockchain.BLOCKS_SIZE == transactionPool.size())
            mineBlock();
    }

    private void mineBlock() {
        Block block = Blockchain.get().createNewBlock(transactionPool);
        proofOfWork(block);
        Blockchain.get().addBlock(block);
    }

    private void proofOfWork(Block block) {
        String target = "0".repeat(Blockchain.MINING_DIFFICULTY);

        String serializedData = new Gson().toJson(transactionPool);
        String blockHash = SHA256.generateHash(block.getTimestamp() + block.getHeight() + serializedData +
                block.getPreviousHash());

        long testNonce = 0;
        boolean nonceFound = false;
        while(!nonceFound) {
            String testHash = SHA256.generateHash(blockHash + testNonce);

            if(testHash.substring(0, Blockchain.MINING_DIFFICULTY).equals(target)) {
                block.setHash(testHash);
                block.setNonce(String.valueOf(testNonce));
                transactionPool = new ArrayList<>();

                nonceFound = true;
            }

            testNonce++;
        }
    }
}

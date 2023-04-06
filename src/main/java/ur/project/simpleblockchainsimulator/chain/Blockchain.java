package ur.project.simpleblockchainsimulator.chain;

import ur.project.simpleblockchainsimulator.transfer.Transaction;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    public static int BLOCKS_SIZE = 2;
    public static int MINING_DIFFICULTY = 3;
    public static int INCREASE_DIFFICULTY_AFTER = 15;

    private static Blockchain instance;
    private final List<Block> blocks;
    public int difficultyCount;

    private Blockchain() {
        this.blocks = new ArrayList<>();

        //Creating the Genesis block
        this.blocks.add(new Block(0));
        difficultyCount = 1;
    }

    public static Blockchain get() {
        if(instance == null)
            instance = new Blockchain();

        return instance;
    }

    public Block createNewBlock(List<Transaction> transactions) {
        String previousHash = blocks.get(blocks.size()-1).getHash();
        long nextHeight = blocks.size();

        checkChainValidity();
        return new Block(transactions, previousHash, nextHeight);
    }

    public void addBlock(Block block) {
        String previousHash = blocks.get(blocks.size()-1).getHash();

        if(previousHash.equals(block.getPreviousHash())) {
            blocks.add(block);
            difficultyCount++;
            difficultyCheck();
        } else {
            throw new RuntimeException("PreviousHash not valid, Block not added to the chain");
        }
    }

    private void checkChainValidity() {
        int maxIndex = blocks.size()-1;

        for(int i=0; i<maxIndex; i++) {
            Block actualBlock = blocks.get(i);
            Block nextBlock = blocks.get(i+1);

            String correctHash = actualBlock.getHash();
            String hashToCheck = nextBlock.getPreviousHash();

            if(!correctHash.equals(hashToCheck))
                throw new RuntimeException("BlockChain is not valid");
        }
    }

    private void difficultyCheck() {
        if(difficultyCount >= INCREASE_DIFFICULTY_AFTER) {
            MINING_DIFFICULTY++;
            difficultyCount = 0;
        }
    }
}

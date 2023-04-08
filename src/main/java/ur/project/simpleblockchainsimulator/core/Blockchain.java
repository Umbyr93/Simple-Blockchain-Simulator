package ur.project.simpleblockchainsimulator.core;

import lombok.Getter;
import ur.project.simpleblockchainsimulator.transfer.Transaction;
import ur.project.simpleblockchainsimulator.utils.Config;
import ur.project.simpleblockchainsimulator.utils.ConfigConst;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    @Getter
    private final int blocksSize = Config.getInt(ConfigConst.BLOCKS_SIZE);
    @Getter
    private final int increaseDifficultyAfter = Config.getInt(ConfigConst.INCREASE_DIFFICULTY_AFTER);
    @Getter
    private int miningDifficulty = Config.getInt(ConfigConst.MINING_DIFFICULTY);

    private final List<Block> blocks;
    private int difficultyCount;

    public Blockchain() {
        this.blocks = new ArrayList<>();

        //Creating the Genesis block
        this.blocks.add(new Block(0));
        difficultyCount = 1;
    }

    protected Block createNewBlock(List<Transaction> transactions) {
        String previousHash = blocks.get(blocks.size()-1).getHash();
        long nextHeight = blocks.size();

        checkChainValidity();
        return new Block(transactions, previousHash, nextHeight);
    }

    protected void addBlock(Block block) {
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

    protected String getLastHash() {
        return blocks.get(blocks.size()-1).getHash();
    }

    private void difficultyCheck() {
        if(difficultyCount >= increaseDifficultyAfter) {
            miningDifficulty++;
            difficultyCount = 0;
        }
    }
}

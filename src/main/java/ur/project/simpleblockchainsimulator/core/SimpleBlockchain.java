package ur.project.simpleblockchainsimulator.core;

import lombok.Getter;
import lombok.Setter;
import ur.project.simpleblockchainsimulator.transfer.Transaction;

import java.util.ArrayList;
import java.util.List;

public class SimpleBlockchain {
    private int startingMiningDifficulty = 3;

    @Getter@Setter
    private int blocksSize = 2;
    @Getter@Setter
    private int increaseDifficultyAfter = 15;
    @Getter
    private int miningDifficulty = startingMiningDifficulty;

    private final List<SimpleBlock> simpleBlocks;
    private int difficultyCount;

    public SimpleBlockchain() {
        this.simpleBlocks = new ArrayList<>();

        //Creating the Genesis block
        this.simpleBlocks.add(new SimpleBlock(0));
        difficultyCount = 1;
    }

    protected SimpleBlock createNewBlock(List<Transaction> transactions) {
        String previousHash = simpleBlocks.get(simpleBlocks.size()-1).getHash();
        long nextHeight = simpleBlocks.size();

        checkChainValidity();
        return new SimpleBlock(transactions, previousHash, nextHeight);
    }

    protected void addBlock(SimpleBlock simpleBlock) {
        String previousHash = simpleBlocks.get(simpleBlocks.size()-1).getHash();

        if(previousHash.equals(simpleBlock.getPreviousHash())) {
            simpleBlocks.add(simpleBlock);
            difficultyCount++;
            difficultyCheck();
        } else {
            throw new RuntimeException("PreviousHash not valid, Block not added to the chain");
        }
    }

    private void checkChainValidity() {
        int maxIndex = simpleBlocks.size()-1;

        for(int i=0; i<maxIndex; i++) {
            SimpleBlock actualSimpleBlock = simpleBlocks.get(i);
            SimpleBlock nextSimpleBlock = simpleBlocks.get(i+1);

            String correctHash = actualSimpleBlock.getHash();
            String hashToCheck = nextSimpleBlock.getPreviousHash();

            if(!correctHash.equals(hashToCheck))
                throw new RuntimeException("BlockChain is not valid");
        }
    }

    private void difficultyCheck() {
        if(difficultyCount >= increaseDifficultyAfter) {
            miningDifficulty++;
            difficultyCount = 0;
        }
    }

    public void setStartingMiningDifficulty(int difficulty) {
        this.startingMiningDifficulty = difficulty;
        this.miningDifficulty = difficulty;
    }
}

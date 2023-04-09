package ur.project.simpleblockchainsimulator.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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

    @Getter(value = AccessLevel.PROTECTED)
    private final List<SimpleBlock> chain;
    private int difficultyCount;

    public SimpleBlockchain() {
        this.chain = new ArrayList<>();

        //Creating the Genesis block
        this.chain.add(new SimpleBlock(0));
        difficultyCount = 1;
    }

    protected SimpleBlock createNewBlock(List<Transaction> transactions) {
        String previousHash = chain.get(chain.size()-1).getHash();
        long nextHeight = chain.size();

        return new SimpleBlock(transactions, previousHash, nextHeight);
    }

    protected void addBlock(SimpleBlock block) {
        chain.add(block);
        difficultyCount++;
        difficultyCheck();
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

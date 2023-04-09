package ur.project.simpleblockchainsimulator.core;

import lombok.extern.slf4j.Slf4j;
import ur.project.simpleblockchainsimulator.core.interfaces.Node;

import java.util.List;

@Slf4j
public class SimpleFullNode implements Node {
    private final SimpleBlockchain blockChain;

    public SimpleFullNode(SimpleBlockchain blockChain) {
        this.blockChain = blockChain;
    }

    @Override
    public void approve(SimpleBlock block) {
        checkBlockValidity(block);
        checkChainValidity();
        blockChain.addBlock(block);

        log.info("Block {} added to the blockchain", block.fourDigitsHash());
    }

    private void checkBlockValidity(SimpleBlock block) {
        int difficulty = blockChain.getMiningDifficulty();
        String target = "0".repeat(difficulty);
        String hashZeroes = block.getHash().substring(0, difficulty);
        final List<SimpleBlock> chain = blockChain.getChain();
        String previousHash = chain.get(chain.size() - 1).getHash();

        if(!target.equals(hashZeroes))
            throw new ApprovationException("Block's hash not valid");
        else if(!previousHash.equals(block.getPreviousHash()))
            throw new ApprovationException("Block's previousHash not valid");

        log.info("Block {} valid", block.fourDigitsHash());
    }

    private void checkChainValidity() {
        final List<SimpleBlock> blocks = blockChain.getChain();
        int maxIndex = blocks.size()-1;

        for(int i=0; i<maxIndex; i++) {
            SimpleBlock actualSimpleBlock = blocks.get(i);
            SimpleBlock nextSimpleBlock = blocks.get(i+1);

            String correctHash = actualSimpleBlock.getHash();
            String hashToCheck = nextSimpleBlock.getPreviousHash();

            if(!correctHash.equals(hashToCheck))
                throw new RuntimeException("Blockchain not valid");
        }

        log.info("Blockchain valid");
    }
}

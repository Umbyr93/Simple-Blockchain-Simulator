package ur.project.simpleblockchainsimulator.core.interfaces;

import ur.project.simpleblockchainsimulator.core.SimpleBlock;
import ur.project.simpleblockchainsimulator.core.SimpleBlockchain;

public interface Node {
    void approve(SimpleBlock simpleBlock);
    void setBlockChain(SimpleBlockchain blockchain);
}

package ur.project.simpleblockchainsimulator.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigConst {
    public static final String BLOCKS_SIZE = "simple-block-chain.block-size";
    public static final String MINING_DIFFICULTY = "simple-block-chain.mining-difficulty";
    public static final String INCREASE_DIFFICULTY_AFTER = "simple-block-chain.increase-difficulty-after";
}

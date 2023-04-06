package ur.project.simpleblockchainsimulator.transfer;

import lombok.Getter;
import ur.project.simpleblockchainsimulator.utils.SHA256;

@Getter
public class Transaction {
    private final String hash;
    private final String value;

    public Transaction(String value) {
        this.value = value;
        this.hash = SHA256.generateHash(value);
    }
}

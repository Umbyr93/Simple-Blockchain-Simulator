package ur.project.simpleblockchainsimulator.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ur.project.simpleblockchainsimulator.core.enums.TransactionStatus;
import ur.project.simpleblockchainsimulator.utils.SHA256;

@Getter
public class Transaction {
    private final String hash;
    private final String value;
    @Setter(value = AccessLevel.PROTECTED)
    private TransactionStatus status = TransactionStatus.PENDING;

    public Transaction(String value) {
        this.value = value;
        this.hash = SHA256.generateHash(value);
    }

    protected String fourDigitsHash() {
        return hash.substring(0, 4);
    }
}

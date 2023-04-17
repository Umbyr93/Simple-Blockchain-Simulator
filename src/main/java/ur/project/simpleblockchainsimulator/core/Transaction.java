package ur.project.simpleblockchainsimulator.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ur.project.simpleblockchainsimulator.core.enums.TransactionStatus;
import ur.project.simpleblockchainsimulator.utils.SHA256;

@Getter
public class Transaction {
    private final String hash;
    private final String senderAddress;
    private final String recipientAddress;
    private final String value;
    private final String currency;
    private final long timestamp;

    @Setter(value = AccessLevel.PROTECTED)
    private TransactionStatus status = TransactionStatus.PENDING;

    public Transaction(String senderAddress, String recipientAddress, String value, String currency) {
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.value = value;
        this.currency = currency;
        this.timestamp = System.currentTimeMillis();
        this.hash = SHA256.generateHash(senderAddress + recipientAddress + value + currency + timestamp);
    }

    protected String fourDigitsHash() {
        return hash.substring(0, 4);
    }
}

package ur.project.simpleblockchainsimulator.chain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ur.project.simpleblockchainsimulator.transfer.Transaction;
import ur.project.simpleblockchainsimulator.utils.SHA256;

import java.util.ArrayList;
import java.util.List;

public class Block {
    @Getter(AccessLevel.PROTECTED)
    private List<Transaction> transactions;

    @Getter(AccessLevel.PUBLIC)
    private String previousHash;

    @Getter(AccessLevel.PUBLIC)
    private final long timestamp;

    @Getter(AccessLevel.PUBLIC)
    private String merkleRoot;

    @Getter(AccessLevel.PUBLIC)
    private final long height;

    @Setter(AccessLevel.PUBLIC)
    @Getter(AccessLevel.PROTECTED)
    private String hash;

    @Setter(AccessLevel.PUBLIC)
    private String nonce;

    protected Block(List<Transaction> transactions, String previousHash, long height) {
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.timestamp = System.currentTimeMillis();
        this.height = height;
        this.merkleRoot = merkleRoot();
    }

    protected Block(long height) {
        this.timestamp = System.currentTimeMillis();
        this.height = height;
        this.hash = SHA256.generateHash(timestamp + String.valueOf(height));
    }

    private String merkleRoot() {
        List<String> treeList = merkleTree();
        // Last element is the merkle root hash of transactions
        return treeList.get(treeList.size() - 1);
    }

    /*
	    This method was adapted from the https://github.com/bitcoinj/bitcoinj project
	      Copyright 2011 Google Inc.
          Copyright 2014 Andreas Schildbach

	    The Merkle root is based on a tree of hashes calculated from the
	    transactions:

			 root
			 / \
			 A B
			 / \ / \
		   t1 t2 t3 t4

		The tree is represented as a list: t1,t2,t3,t4,A,B,root where each
		entry is a hash.

		The hashing algorithm is SHA-256. The leaves are a hash of the
		serialized contents of the transaction.
	    The interior nodes are hashes of the concatenation of the two child
	    hashes.

		This structure allows the creation of proof that a transaction was
		included into a block without having to
		provide the full block contents. Instead, you can provide only a
		Merkle branch. For example to prove tx2 was
		in a block you can just provide tx2, the hash(tx1) and A. Now the
		other party has everything they need to
		derive the root, which can be checked against the block header. These
		proofs aren't used right now but
		will be helpful later when we want to download partial block
		contents.

		Note that if the number of transactions is not even the last tx is
		repeated to make it so (see
		tx3 above). A tree with 5 transactions would look like this:

		        root
		 	    / \
		        1 5
		      / \ / \
		     2 3 4 4
		   / \ / \ / \
		t1 t2 t3 t4 t5 t5
	*/
    private List<String> merkleTree() {
        List<String> tree = new ArrayList<>();
        // add all transactions as leaves of the tree.
        for (Transaction t : transactions) {
            tree.add(t.getHash());
        }

        int levelOffset = 0; // first level

        // Iterate through each level, stopping when we reach the root (levelSize
        // == 1).
        for (int levelSize = transactions.size(); levelSize > 1; levelSize = (levelSize + 1) / 2) {
            // For each pair of nodes on that level:
            for (int left = 0; left < levelSize; left += 2) {
                // The right hand node can be the same as the left hand, in the
                // case where we don't have enough
                // transactions.
                int right = Math.min(left + 1, levelSize - 1);
                String tleft = tree.get(levelOffset + left);
                String tright = tree.get(levelOffset + right);
                tree.add(SHA256.generateHash(tleft + tright));
            }
            // Move to the next level.
            levelOffset += levelSize;
        }
        return tree;
    }
}

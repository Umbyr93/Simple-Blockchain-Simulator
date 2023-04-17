# Simple SimpleBlockchain Simulator
### 1. Create the SimpleBlockchain object.
```
SimpleBlockchain simpleBlockchain = new SimpleBlockchain();
```

#### You can modify some configurations, otherwise the defaults will be used.
```
simpleBlockchain.setBlocksSize(1); //Default is 1
simpleBlockchain.setStartingMiningDifficulty(1); //Default is 3
simpleBlockchain.setIncreaseDifficultyAfter(5); //Default is 15
```

**BlocksSize (Default: 1)** = This is used to determine how many transactions a block can contain. When the Miner's TransactionPool has enough transactions in order to build a block, the miner starts mining. <br><br>
**StartingMiningDifficulty (Default: 3)** = The more higher it is, the more time it will take to solve the ProofOfWork's problem. Starting from 6 onwards it might take a lot of time.<br><br>
**IncreaseDifficultyAfter (Default: 15)** = Every X blocks added to the SimpleBlockchain, the MiningDifficulty will be increased by 1.

### 2. Create the Network object
```
Network network = new SimpleNetwork(simpleBlockchain, new SimpleNode(), new SimpleMiner());
```

### 3. Start the network: a new Thread asynchronously waiting for new Transactions is created.
```
network.start();
```

### 4. Create a Transaction and queue it asynchronously in the network. In this stage the Transaction will have the status property set to PENDING.
```
Transaction transaction = new Transaction("12134", "949", "12.94470246", "BTC");
network.queueTransaction(t1);
```

### 5. Wait for the miners to finish the procedure, logs will show all the steps. 
```
INFO ur.project.simpleblockchainsimulator.core.SimpleNetwork - Network started
INFO ur.project.simpleblockchainsimulator.core.SimpleNetwork - Queueing transaction bb2e
INFO ur.project.simpleblockchainsimulator.core.SimpleMiner - Added transaction bb2e to the pool
INFO ur.project.simpleblockchainsimulator.core.SimpleMiner - Starting proof of work with difficulty: 1
INFO ur.project.simpleblockchainsimulator.core.SimpleMiner - Nonce found, created block 5d29. Sending to the network for validation...
INFO ur.project.simpleblockchainsimulator.core.SimpleNode - Block 5d29 valid
INFO ur.project.simpleblockchainsimulator.core.SimpleNode - SimpleBlockchain valid
INFO ur.project.simpleblockchainsimulator.core.SimpleNode - Block 5d29 added to the SimpleBlockchain
```

### 6. You can call the network's API "searchTransaction" to get a Transaction from the SimpleBlockchain by its hash code. If the whole procedure is finished and the block was validated, the status will be AUTHORIZED.
```
Optional<Transaction> optTransaction = network.searchTransaction(transaction.getHash());
```

### 7. You can stop the network at any time.
```
network.stop();
```

##### If you try to queue new Transactions when the network is offline, the following exception will be thrown.
```
Exception in thread "main" ur.project.simpleblockchainsimulator.core.exceptions.NetworkException: Network offline, couldn't connect
```
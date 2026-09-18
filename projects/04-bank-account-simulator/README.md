# Project 4 — Bank Account Simulator

## Goal

Model a bank account with a private balance. Deposit and withdraw through methods. Reject overdrafts. Demonstrate two separate account objects.

## Concepts this practices

- Classes and objects
- Encapsulation (`private` fields, public methods)
- `this`
- Object state that changes over time

## How to run

From this project directory:

```bash
mvn -q compile exec:java
```

Optional tests:

```bash
mvn test
```

## Expected output

```text
Aisha opening balance: 100.0
Aisha after deposit 50.0: 150.0
Aisha after withdraw 20.0: 130.0
Aisha withdraw 500.0 -> Insufficient funds for Aisha
Ben opening balance: 25.0
Ben after deposit 75.0: 100.0
```

## What success looks like

Each account keeps its own balance. You cannot change `balance` from outside the class. A withdraw larger than the balance is rejected and the money stays put.

## Stretch challenge

Add a `transferTo(BankAccount target, double amount)` method that withdraws from one account and deposits into the other, or fails without changing either balance.

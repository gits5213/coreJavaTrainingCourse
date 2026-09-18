package com.sdet.projects.bank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankAccountTest {

    @Test
    void depositIncreasesBalance() {
        BankAccount account = new BankAccount("Aisha", 100.00);

        account.deposit(50.00);

        assertEquals(150.00, account.getBalance());
    }

    @Test
    void withdrawDecreasesBalance() {
        BankAccount account = new BankAccount("Aisha", 100.00);

        account.withdraw(20.00);

        assertEquals(80.00, account.getBalance());
    }

    @Test
    void withdrawMoreThanBalanceIsRejected() {
        BankAccount account = new BankAccount("Aisha", 100.00);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> account.withdraw(500.00)
        );

        assertEquals("Insufficient funds for Aisha", exception.getMessage());
        assertEquals(100.00, account.getBalance());
    }

    @Test
    void twoAccountsKeepSeparateBalances() {
        BankAccount aisha = new BankAccount("Aisha", 100.00);
        BankAccount ben = new BankAccount("Ben", 25.00);

        aisha.deposit(10.00);
        ben.withdraw(5.00);

        assertEquals(110.00, aisha.getBalance());
        assertEquals(20.00, ben.getBalance());
    }
}

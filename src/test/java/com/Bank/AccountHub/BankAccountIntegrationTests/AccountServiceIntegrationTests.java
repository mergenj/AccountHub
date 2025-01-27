package com.Bank.AccountHub.BankAccountIntegrationTests;

import com.Bank.AccountHub.Entities.Account;
import com.Bank.AccountHub.Entities.CurrencyBalance;
import com.Bank.AccountHub.Enums.CurrencyType;
import com.Bank.AccountHub.Exceptions.InsufficientBalance;
import com.Bank.AccountHub.Exceptions.InvalidAmountException;
import com.Bank.AccountHub.Repositories.AccountRepository;
import com.Bank.AccountHub.Services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AccountServiceIntegrationTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    private Account testAccount;

    @BeforeEach
    public void setup() {
        // Setup a test account in the database
        testAccount = new Account();
        testAccount.setAccountHolderName("Test User");

        CurrencyBalance usdBalance = new CurrencyBalance(CurrencyType.USD, BigDecimal.valueOf(1000), testAccount);
        CurrencyBalance eurBalance = new CurrencyBalance(CurrencyType.EUR, BigDecimal.valueOf(500), testAccount);
        testAccount.getBalances().add(usdBalance);
        testAccount.getBalances().add(eurBalance);

        accountRepository.save(testAccount);
    }

    @Test
    public void testDepositInvalidAmount() {
        // Assert
        assertThrows(InvalidAmountException.class, () ->
                accountService.deposit(testAccount.getId(), CurrencyType.USD, BigDecimal.valueOf(-100)));
    }

    @Test
    public void testDebitInsufficientBalance() {
        // Assert
        assertThrows(InsufficientBalance.class, () ->
                accountService.debit(testAccount.getId(), CurrencyType.EUR, BigDecimal.valueOf(600)));
    }

    @Test
    public void testExchangeInvalidAmount() {
        // Assert
        assertThrows(InvalidAmountException.class, () ->
                accountService.exchange(testAccount.getId(), CurrencyType.USD, CurrencyType.EUR, BigDecimal.valueOf(-50)));
    }

    @Test
    public void testExchangeInsufficientBalance() {
        // Assert
        assertThrows(InsufficientBalance.class, () ->
                accountService.exchange(testAccount.getId(), CurrencyType.USD, CurrencyType.EUR, BigDecimal.valueOf(1500)));
    }
}


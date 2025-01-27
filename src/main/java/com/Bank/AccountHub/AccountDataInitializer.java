package com.Bank.AccountHub;

import com.Bank.AccountHub.Entities.Account;
import com.Bank.AccountHub.Entities.CurrencyBalance;
import com.Bank.AccountHub.Enums.CurrencyType;
import com.Bank.AccountHub.Repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
public class AccountDataInitializer implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();

        for (int i = 1; i <= 100; i++) {
            Account account = new Account();
            account.setAccountHolderName("Account " + i);

            // Initialize random balances for each currency
            CurrencyBalance usdBalance = new CurrencyBalance(CurrencyType.USD, BigDecimal.valueOf(random.nextInt(1000) + 100), account);
            CurrencyBalance eurBalance = new CurrencyBalance(CurrencyType.EUR, BigDecimal.valueOf(random.nextInt(1000) + 100), account);
            CurrencyBalance sekBalance = new CurrencyBalance(CurrencyType.SEK, BigDecimal.valueOf(random.nextInt(1000) + 100), account);
            CurrencyBalance rubBalance = new CurrencyBalance(CurrencyType.RUB, BigDecimal.valueOf(random.nextInt(1000) + 100), account);

            account.getBalances().add(usdBalance);
            account.getBalances().add(eurBalance);
            account.getBalances().add(sekBalance);
            account.getBalances().add(rubBalance);

            accountRepository.save(account);
        }

        System.out.println("100 accounts have been populated successfully.");
    }
}

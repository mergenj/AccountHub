package com.Bank.AccountHub.Services;

import com.Bank.AccountHub.Entities.Account;
import com.Bank.AccountHub.Entities.CurrencyBalance;
import com.Bank.AccountHub.Enums.CurrencyType;
import com.Bank.AccountHub.Exceptions.AccountNotFoundException;
import com.Bank.AccountHub.Exceptions.CurrencyBalanceNotFound;
import com.Bank.AccountHub.Exceptions.InsufficientBalance;
import com.Bank.AccountHub.Exceptions.InvalidAmountException;
import com.Bank.AccountHub.Repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    CurrencyExchangeService currencyExchangeService;

    @Transactional
    public void deposit(Long accountId, CurrencyType currency, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        CurrencyBalance balance = account.getBalances().stream()
                .filter(b -> b.getCurrency() == currency)
                .findFirst()
                .orElseGet(() -> {
                    CurrencyBalance newBalance = new CurrencyBalance(currency, BigDecimal.ZERO, account);
                    account.getBalances().add(newBalance);
                    return newBalance;
                });

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
        balance.setBalance(balance.getBalance().add(amount));
        accountRepository.save(account);
    }

    @Transactional
    public void debit(Long accountId, CurrencyType currency, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        CurrencyBalance balance = account.getBalances().stream()
                .filter(b -> b.getCurrency() == currency)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Insufficient balance in specified currency"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        if (balance.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalance("Insufficient balance");
        }

        balance.setBalance(balance.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    public Map<CurrencyType, BigDecimal> getBalances(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        Map<CurrencyType, BigDecimal> balances = new HashMap<>();
        for (CurrencyBalance balance : account.getBalances()) {
            balances.put(balance.getCurrency(), balance.getBalance());
        }

        return balances;
    }

    @Transactional
    public void exchange(Long accountId, CurrencyType fromCurrency, CurrencyType toCurrency, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        CurrencyBalance fromBalance = account.getBalances().stream()
                .filter(b -> b.getCurrency() == fromCurrency)
                .findFirst()
                .orElseThrow(() -> new CurrencyBalanceNotFound("Currency balance does not exist"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        if (fromBalance.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalance("Insufficient balance");
        }

        CurrencyBalance toBalance = account.getBalances().stream()
                .filter(b -> b.getCurrency() == toCurrency)
                .findFirst()
                .orElseGet(() -> {
                    CurrencyBalance newBalance = new CurrencyBalance(toCurrency, BigDecimal.ZERO, account);
                    account.getBalances().add(newBalance);
                    return newBalance;
                });

        fromBalance.setBalance(fromBalance.getBalance().subtract(amount));
        BigDecimal convertedAmount = currencyExchangeService.exchange(fromCurrency, toCurrency, amount);
        toBalance.setBalance(toBalance.getBalance().add(convertedAmount));

        accountRepository.save(account);
    }
}

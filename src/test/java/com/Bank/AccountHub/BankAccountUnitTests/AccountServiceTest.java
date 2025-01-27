package com.Bank.AccountHub.BankAccountUnitTests;

import com.Bank.AccountHub.Entities.Account;
import com.Bank.AccountHub.Entities.CurrencyBalance;
import com.Bank.AccountHub.Enums.CurrencyType;
import com.Bank.AccountHub.Exceptions.InsufficientBalance;
import com.Bank.AccountHub.Repositories.AccountRepository;
import com.Bank.AccountHub.Repositories.TransactionRepository;
import com.Bank.AccountHub.Services.AccountService;
import com.Bank.AccountHub.Services.CurrencyExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CurrencyExchangeService currencyExchangeService;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setId(1L);
        List<CurrencyBalance> balances = new ArrayList<>();
        balances.add(new CurrencyBalance(CurrencyType.USD, BigDecimal.valueOf(1000), account));
        account.setBalances(balances);
    }

    @Test
    void deposit_ShouldIncreaseBalance_WhenCurrencyExists() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.deposit(1L, CurrencyType.USD, BigDecimal.valueOf(500));

        verify(accountRepository, times(1)).save(account);
        assertEquals(BigDecimal.valueOf(1500), account.getBalances().getFirst().getBalance());
    }

    @Test
    void deposit_ShouldAddNewCurrencyBalance_WhenCurrencyDoesNotExist() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.deposit(1L, CurrencyType.EUR, BigDecimal.valueOf(200));

        verify(accountRepository, times(1)).save(account);
        assertEquals(2, account.getBalances().size());
        assertTrue(account.getBalances().stream().anyMatch(b -> b.getCurrency() == CurrencyType.EUR && b.getBalance().equals(BigDecimal.valueOf(200))));
    }

    @Test
    void debit_ShouldDecreaseBalance_WhenSufficientFundsExist() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.debit(1L, CurrencyType.USD, BigDecimal.valueOf(500));

        verify(accountRepository, times(1)).save(account);
        assertEquals(BigDecimal.valueOf(500), account.getBalances().get(0).getBalance());
    }

    @Test
    void debit_ShouldThrowException_WhenInsufficientFunds() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Exception exception = assertThrows(InsufficientBalance.class, () ->
                accountService.debit(1L, CurrencyType.USD, BigDecimal.valueOf(2000)));

        assertEquals("Insufficient balance", exception.getMessage());
        verify(accountRepository, never()).save(account);
    }

    @Test
    void getBalances_ShouldReturnAllBalances() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        var balances = accountService.getBalances(1L);

        assertEquals(1, balances.size());
        assertEquals(BigDecimal.valueOf(1000), balances.get(CurrencyType.USD));
    }

    @Test
    void exchange_ShouldConvertCurrency_WhenSufficientFundsExist() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(currencyExchangeService.exchange(CurrencyType.USD, CurrencyType.EUR, BigDecimal.valueOf(100)))
                .thenReturn(BigDecimal.valueOf(85));

        accountService.exchange(1L, CurrencyType.USD, CurrencyType.EUR, BigDecimal.valueOf(100));

        verify(accountRepository, times(1)).save(account);
        assertEquals(BigDecimal.valueOf(900), account.getBalances().get(0).getBalance());
        assertTrue(account.getBalances().stream().anyMatch(b -> b.getCurrency() == CurrencyType.EUR && b.getBalance().equals(BigDecimal.valueOf(85))));
    }

    @Test
    void exchange_ShouldThrowException_WhenInsufficientFunds() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Exception exception = assertThrows(InsufficientBalance.class, () ->
                accountService.exchange(1L, CurrencyType.USD, CurrencyType.EUR, BigDecimal.valueOf(2000)));

        assertEquals("Insufficient balance", exception.getMessage());
        verify(accountRepository, never()).save(account);
    }
}

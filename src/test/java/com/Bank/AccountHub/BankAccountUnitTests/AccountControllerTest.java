package com.Bank.AccountHub.BankAccountUnitTests;

import com.Bank.AccountHub.Controllers.AccountController;
import com.Bank.AccountHub.Enums.CurrencyType;
import com.Bank.AccountHub.Services.AccountService;
import com.Bank.AccountHub.Services.CurrencyExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    @Mock
    private CurrencyExchangeService currencyExchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeposit() {
        // Arrange
        Long accountId = 1L;
        CurrencyType currency = CurrencyType.USD;
        BigDecimal amount = BigDecimal.valueOf(100);

        doNothing().when(accountService).deposit(accountId, currency, amount);

        // Act
        ResponseEntity<String> response = accountController.deposit(accountId, currency, amount);

        // Assert
        assertEquals("Deposit successful", response.getBody());
        assertEquals(200, response.getStatusCode().value());
        verify(accountService, times(1)).deposit(accountId, currency, amount);
    }

    @Test
    void testDebit() {
        // Arrange
        Long accountId = 1L;
        CurrencyType currency = CurrencyType.USD;
        BigDecimal amount = BigDecimal.valueOf(50);

        doNothing().when(accountService).debit(accountId, currency, amount);

        // Act
        ResponseEntity<String> response = accountController.debit(accountId, currency, amount);

        // Assert
        assertEquals("Debit successful", response.getBody());
        assertEquals(200, response.getStatusCode().value());
        verify(accountService, times(1)).debit(accountId, currency, amount);
    }

    @Test
    void testGetBalances() {
        // Arrange
        Long accountId = 1L;
        Map<CurrencyType, BigDecimal> balances = Map.of(CurrencyType.USD, BigDecimal.valueOf(100), CurrencyType.EUR, BigDecimal.valueOf(50));

        when(accountService.getBalances(accountId)).thenReturn(balances);

        // Act
        ResponseEntity<Map<CurrencyType, BigDecimal>> response = accountController.getBalances(accountId);

        // Assert
        assertEquals(balances, response.getBody());
        assertEquals(200, response.getStatusCode().value());
        verify(accountService, times(1)).getBalances(accountId);
    }

    @Test
    void testExchange() {
        // Arrange
        Long accountId = 1L;
        CurrencyType fromCurrency = CurrencyType.USD;
        CurrencyType toCurrency = CurrencyType.EUR;
        BigDecimal amount = BigDecimal.valueOf(100);

        doNothing().when(accountService).exchange(accountId, fromCurrency, toCurrency, amount);

        // Act
        ResponseEntity<String> response = accountController.exchange(accountId, fromCurrency, toCurrency, amount);

        // Assert
        assertEquals("Exchange successful", response.getBody());
        assertEquals(200, response.getStatusCode().value());
        verify(accountService, times(1)).exchange(accountId, fromCurrency, toCurrency, amount);
    }

    @Test
    void testDepositInvalidAccount() {
        // Arrange
        Long accountId = 999L;
        CurrencyType currency = CurrencyType.USD;
        BigDecimal amount = BigDecimal.valueOf(100);

        doThrow(new IllegalArgumentException("Account not found")).when(accountService).deposit(accountId, currency, amount);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> accountController.deposit(accountId, currency, amount));
        assertEquals("Account not found", exception.getMessage());
        verify(accountService, times(1)).deposit(accountId, currency, amount);
    }

    @Test
    void testExchangeInvalidAmount() {
        // Arrange
        Long accountId = 1L;
        CurrencyType fromCurrency = CurrencyType.USD;
        CurrencyType toCurrency = CurrencyType.EUR;
        BigDecimal amount = BigDecimal.valueOf(-100);

        doThrow(new IllegalArgumentException("Amount must be greater than zero")).when(accountService).exchange(accountId, fromCurrency, toCurrency, amount);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> accountController.exchange(accountId, fromCurrency, toCurrency, amount));
        assertEquals("Amount must be greater than zero", exception.getMessage());
        verify(accountService, times(1)).exchange(accountId, fromCurrency, toCurrency, amount);
    }
}

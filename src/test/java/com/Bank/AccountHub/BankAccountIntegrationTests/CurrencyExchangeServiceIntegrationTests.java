package com.Bank.AccountHub.BankAccountIntegrationTests;

import com.Bank.AccountHub.Enums.CurrencyType;
import com.Bank.AccountHub.Exceptions.ExchangeRateNotFoundException;
import com.Bank.AccountHub.Exceptions.InvalidAmountException;
import com.Bank.AccountHub.Services.CurrencyExchangeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class CurrencyExchangeServiceIntegrationTests {

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    @Test
    public void testExchange_ValidCurrenciesAndAmount() {
        // Arrange
        CurrencyType fromCurrency = CurrencyType.USD;
        CurrencyType toCurrency = CurrencyType.EUR;
        BigDecimal amount = BigDecimal.valueOf(100);

        // Act
        BigDecimal result = currencyExchangeService.exchange(fromCurrency, toCurrency, amount);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    public void testExchange_InvalidAmount() {
        // Arrange
        CurrencyType fromCurrency = CurrencyType.USD;
        CurrencyType toCurrency = CurrencyType.EUR;
        BigDecimal amount = BigDecimal.valueOf(-50);

        // Act & Assert
        assertThatThrownBy(() -> currencyExchangeService.exchange(fromCurrency, toCurrency, amount))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessageContaining("Amount must be greater than zero");
    }

    @Test
    public void testGetExchangeRate_ValidPair() {
        // Arrange
        String fromCurrency = "USD";
        String toCurrency = "EUR";

        // Act
        BigDecimal rate = currencyExchangeService.getExchangeRate(fromCurrency, toCurrency);

        // Assert
        assertThat(rate).isNotNull();
        assertThat(rate).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    public void testGetExchangeRate_InvalidPair() {
        // Arrange
        String fromCurrency = "USD";
        String toCurrency = "XYZ";

        // Act & Assert
        assertThatThrownBy(() -> currencyExchangeService.getExchangeRate(fromCurrency, toCurrency))
                .isInstanceOf(ExchangeRateNotFoundException.class)
                .hasMessageContaining("Exchange rate not found");
    }
}


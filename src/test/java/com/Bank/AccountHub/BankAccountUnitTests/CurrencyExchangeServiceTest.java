package com.Bank.AccountHub.BankAccountUnitTests;

import com.Bank.AccountHub.Enums.CurrencyType;
import com.Bank.AccountHub.Exceptions.ExchangeRateNotFoundException;
import com.Bank.AccountHub.Exceptions.InvalidAmountException;
import com.Bank.AccountHub.Services.CurrencyExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CurrencyExchangeServiceTest {

    private CurrencyExchangeService currencyExchangeService;

    @BeforeEach
    void setUp() {
        currencyExchangeService = new CurrencyExchangeService();
    }

    @Test
    void testGetExchangeRate_Success() {
        BigDecimal rate = currencyExchangeService.getExchangeRate("EUR", "USD");
        assertThat(rate).isEqualTo(BigDecimal.valueOf(1.18));
    }

    @Test
    void testGetExchangeRate_NotFound() {
        assertThatThrownBy(() -> currencyExchangeService.getExchangeRate("EUR", "JPY"))
                .isInstanceOf(ExchangeRateNotFoundException.class)
                .hasMessageContaining("Exchange rate not found for EUR_TO_JPY");
    }

    @Test
    void testExchange_Success() {
        BigDecimal exchangedAmount = currencyExchangeService.exchange(CurrencyType.EUR, CurrencyType.USD, BigDecimal.valueOf(100));
        assertThat(exchangedAmount).isEqualTo(BigDecimal.valueOf(118.00).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void testExchange_InvalidAmount_Zero() {
        assertThatThrownBy(() -> currencyExchangeService.exchange(CurrencyType.EUR, CurrencyType.USD, BigDecimal.ZERO))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessage("Amount must be greater than zero");
    }

    @Test
    void testExchange_InvalidAmount_Negative() {
        assertThatThrownBy(() -> currencyExchangeService.exchange(CurrencyType.EUR, CurrencyType.USD, BigDecimal.valueOf(-50)))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessage("Amount must be greater than zero");
    }
}

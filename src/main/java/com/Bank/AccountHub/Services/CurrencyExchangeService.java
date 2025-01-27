package com.Bank.AccountHub.Services;

import com.Bank.AccountHub.Enums.CurrencyType;
import com.Bank.AccountHub.Exceptions.ExchangeRateNotFoundException;
import com.Bank.AccountHub.Exceptions.InvalidAmountException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyExchangeService {

    private final Map<String, BigDecimal> exchangeRates = new HashMap<>();

    public CurrencyExchangeService() {
        exchangeRates.put("EUR_TO_USD", BigDecimal.valueOf(1.18));
        exchangeRates.put("EUR_TO_SEK", BigDecimal.valueOf(10.20));
        exchangeRates.put("EUR_TO_RUB", BigDecimal.valueOf(102.86));
        exchangeRates.put("USD_TO_EUR", BigDecimal.valueOf(0.85));
        exchangeRates.put("USD_TO_SEK", BigDecimal.valueOf(8.64));
        exchangeRates.put("USD_TO_RUB", BigDecimal.valueOf(75.00));
        exchangeRates.put("SEK_TO_EUR", BigDecimal.valueOf(0.098));
        exchangeRates.put("SEK_TO_USD", BigDecimal.valueOf(0.12));
        exchangeRates.put("SEK_TO_RUB", BigDecimal.valueOf(9.00));
        exchangeRates.put("RUB_TO_EUR", BigDecimal.valueOf(0.0097));
        exchangeRates.put("RUB_TO_USD", BigDecimal.valueOf(0.013));
        exchangeRates.put("RUB_TO_SEK", BigDecimal.valueOf(0.11));
    }

    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        String key = fromCurrency + "_TO_" + toCurrency;
        if (!exchangeRates.containsKey(key)) {
            throw new ExchangeRateNotFoundException("Exchange rate not found for " + key);
        }
        return exchangeRates.get(key);
    }

    public BigDecimal exchange(CurrencyType fromCurrency, CurrencyType toCurrency, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
        BigDecimal rate = getExchangeRate(fromCurrency.toString(), toCurrency.toString());
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}

package com.Bank.AccountHub.DTOs;

import com.Bank.AccountHub.Enums.CurrencyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ExchangeRequestDTO {

    @NotNull(message = "From currency is required")
    private CurrencyType fromCurrency;

    @NotNull(message = "To currency is required")
    private CurrencyType toCurrency;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    public ExchangeRequestDTO() {
    }

    public ExchangeRequestDTO(CurrencyType fromCurrency, CurrencyType toCurrency, BigDecimal amount) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
    }

    public CurrencyType getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(CurrencyType fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public CurrencyType getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(CurrencyType toCurrency) {
        this.toCurrency = toCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

package com.Bank.AccountHub.DTOs;

import com.Bank.AccountHub.Enums.CurrencyType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class DepositRequestDTO {

    @NotNull(message = "Currency is required")
    private CurrencyType currency;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    public DepositRequestDTO() {
    }

    public DepositRequestDTO(CurrencyType currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

package com.Bank.AccountHub.Controllers;

import com.Bank.AccountHub.DTOs.DebitRequestDTO;
import com.Bank.AccountHub.DTOs.DepositRequestDTO;
import com.Bank.AccountHub.DTOs.ExchangeRequestDTO;
import com.Bank.AccountHub.Enums.CurrencyType;
import com.Bank.AccountHub.Exceptions.AccountNotFoundException;
import com.Bank.AccountHub.Services.AccountService;
import com.Bank.AccountHub.Services.CurrencyExchangeService;
import com.Bank.AccountHub.Services.ExternalApiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    @Autowired
    private ExternalApiService externalApiService;

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<String> deposit(@PathVariable Long accountId,
                                          @Valid @RequestBody DepositRequestDTO depositRequestDTO) {
        try {
            accountService.deposit(accountId, depositRequestDTO.getCurrency(), depositRequestDTO.getAmount());
        } catch (AccountNotFoundException e) {
            return ResponseEntity.badRequest().body("Account not found");
        }
        return ResponseEntity.ok("Deposit successful");
    }

    @PostMapping("/{accountId}/debit")
    public ResponseEntity<String> debit(@PathVariable Long accountId,
                                        @Valid @RequestBody DebitRequestDTO debitRequestDTO) {
        accountService.debit(accountId, debitRequestDTO.getCurrency(), debitRequestDTO.getAmount());
        return ResponseEntity.ok("Debit successful");
    }

    @GetMapping("/{accountId}/balances")
    public ResponseEntity<Map<CurrencyType, BigDecimal>> getBalances(@PathVariable Long accountId) {
        Map<CurrencyType, BigDecimal> balances = accountService.getBalances(accountId);
        return ResponseEntity.ok(balances);
    }

    @PostMapping("/{accountId}/exchange")
    public ResponseEntity<String> exchange(@PathVariable Long accountId,
                                           @Valid @RequestBody ExchangeRequestDTO exchangeRequestDTO) {
        accountService.exchange(accountId, exchangeRequestDTO.getFromCurrency(), exchangeRequestDTO.getToCurrency(), exchangeRequestDTO.getAmount());
        return ResponseEntity.ok("Exchange successful");
    }

    @GetMapping("/ExternalApiCall")
    public ResponseEntity<String> externalApiCall() {
        String response = externalApiService.logToExternalSystem(200);
        System.out.println("External system response: " + response);
        return ResponseEntity.ok(response);
    }
}

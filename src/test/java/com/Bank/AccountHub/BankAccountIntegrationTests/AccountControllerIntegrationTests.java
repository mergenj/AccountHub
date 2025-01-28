package com.Bank.AccountHub.BankAccountIntegrationTests;

import com.Bank.AccountHub.DTOs.DebitRequestDTO;
import com.Bank.AccountHub.DTOs.DepositRequestDTO;
import com.Bank.AccountHub.DTOs.ExchangeRequestDTO;
import com.Bank.AccountHub.Enums.CurrencyType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testDepositMoneyIntegration() {
        // Arrange
        long accountId = 1L;
        BigDecimal amount = BigDecimal.valueOf(1);
        DepositRequestDTO depositRequestDTO = new DepositRequestDTO(CurrencyType.USD, amount);

        ResponseEntity<Map> balancesResponse = restTemplate.getForEntity(
                "/api/accounts/" + accountId + "/balances",
                Map.class);

        assertThat(balancesResponse.getBody()).containsKey("USD");

        BigDecimal usdBalanceBeforeDeposit = new BigDecimal(balancesResponse.getBody().get("USD").toString());

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + accountId + "/deposit",
                depositRequestDTO, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Deposit successful");

        // Verify
        balancesResponse = restTemplate.getForEntity(
                "/api/accounts/" + accountId + "/balances",
                Map.class);
        assertThat(balancesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(balancesResponse.getBody()).containsKey("USD");
        assertThat(new BigDecimal(balancesResponse.getBody().get("USD").toString())).isEqualTo(usdBalanceBeforeDeposit.add(amount));
    }

    @Test
    public void testDebitMoneyIntegration() {
        // Arrange
        long accountId = 1L;
        BigDecimal amount = BigDecimal.valueOf(1);
        DebitRequestDTO debitRequestDTO = new DebitRequestDTO(CurrencyType.USD, amount);

        ResponseEntity<Map> balancesResponse = restTemplate.getForEntity(
                "/api/accounts/" + accountId + "/balances",
                Map.class);

        assertThat(balancesResponse.getBody()).containsKey("USD");

        BigDecimal usdBalanceBeforeDebit = new BigDecimal(balancesResponse.getBody().get("USD").toString());

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + accountId + "/debit",
                debitRequestDTO, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Debit successful");

        // Verify
        balancesResponse = restTemplate.getForEntity(
                "/api/accounts/" + accountId + "/balances",
                Map.class);
        assertThat(balancesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(new BigDecimal(balancesResponse.getBody().get("USD").toString())).isEqualTo(usdBalanceBeforeDebit.subtract(amount));
    }

    @Test
    public void testCurrencyExchangeIntegration() {
        // Arrange
        long accountId = 1L;
        BigDecimal amount = BigDecimal.valueOf(1);
        ExchangeRequestDTO exchangeRequestDTO = new ExchangeRequestDTO(CurrencyType.USD, CurrencyType.EUR, amount);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + accountId + "/exchange",
                exchangeRequestDTO, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Exchange successful");

        // Verify
        ResponseEntity<Map> balancesResponse = restTemplate.getForEntity(
                "/api/accounts/" + accountId + "/balances",
                Map.class);
        assertThat(balancesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(balancesResponse.getBody()).containsKeys("USD", "EUR");
    }

    @Test
    public void testInvalidAccountIntegration() {
        // Arrange
        long invalidAccountId = 9999L;
        BigDecimal amount = BigDecimal.valueOf(1);
        DepositRequestDTO depositRequestDTO = new DepositRequestDTO(CurrencyType.USD, amount);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + invalidAccountId + "/deposit",
                depositRequestDTO, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}


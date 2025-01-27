package com.Bank.AccountHub.BankAccountIntegrationTests;

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
        String currency = "USD";
        BigDecimal amount = BigDecimal.valueOf(1);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + accountId + "/deposit?currency=" + currency + "&amount=" + amount,
                null, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Deposit successful");

        // Verify
        ResponseEntity<Map> balancesResponse = restTemplate.getForEntity(
                "/api/accounts/" + accountId + "/balances",
                Map.class);
        assertThat(balancesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(balancesResponse.getBody()).containsKey("USD");
        assertThat(new BigDecimal(balancesResponse.getBody().get("USD").toString())).isGreaterThanOrEqualTo(amount);
    }

    @Test
    public void testDebitMoneyIntegration() {
        // Arrange
        long accountId = 1L;
        String currency = "USD";
        BigDecimal amount = BigDecimal.valueOf(1);

        ResponseEntity<Map> balancesResponse = restTemplate.getForEntity(
                "/api/accounts/" + accountId + "/balances",
                Map.class);

        assertThat(balancesResponse.getBody()).containsKey("USD");

        BigDecimal usdBalanceBeforeDeposit = new BigDecimal(balancesResponse.getBody().get("USD").toString());

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + accountId + "/debit?currency=" + currency + "&amount=" + amount,
                null, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Debit successful");

        // Verify
        balancesResponse = restTemplate.getForEntity(
                "/api/accounts/" + accountId + "/balances",
                Map.class);
        assertThat(balancesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(new BigDecimal(balancesResponse.getBody().get("USD").toString())).isLessThanOrEqualTo(usdBalanceBeforeDeposit);
    }

    @Test
    public void testCurrencyExchangeIntegration() {
        // Arrange
        long accountId = 1L;
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(1);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + accountId + "/exchange?fromCurrency=" + fromCurrency + "&toCurrency=" + toCurrency + "&amount=" + amount,
                null, String.class);

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
        String currency = "USD";
        BigDecimal amount = BigDecimal.valueOf(1);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + invalidAccountId + "/deposit?currency=" + currency + "&amount=" + amount,
                null, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testInvalidCurrencyIntegration() {
        // Arrange
        long accountId = 1L;
        String invalidCurrency = "XYZ";
        BigDecimal amount = BigDecimal.valueOf(1);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + accountId + "/deposit?currency=" + invalidCurrency + "&amount=" + amount,
                null, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}


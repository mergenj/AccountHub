package com.Bank.AccountHub.BankAccountUnitTests;

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
public class BankAccountRestControllerUnitTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testDepositMoney() {
        // Arrange
        long accountId = 1L;
        String currency = "USD";
        BigDecimal amount = BigDecimal.valueOf(500);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + accountId + "/deposit?currency=" + currency + "&amount=" + amount,
                null, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Deposit successful");
    }

    @Test
    public void testDebitMoney() {
        // Arrange
        long accountId = 1L;
        String currency = "USD";
        BigDecimal amount = BigDecimal.valueOf(200);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + accountId + "/debit?currency=" + currency + "&amount=" + amount,
                null, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Debit successful");
    }

    @Test
    public void testGetBalances() {
        // Arrange
        long accountId = 1L;

        // Act
        ResponseEntity<Map> response = restTemplate.getForEntity(
                "/api/accounts/" + accountId + "/balances",
                Map.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).containsKeys("USD", "EUR", "SEK", "RUB");
    }

    @Test
    public void testCurrencyExchange() {
        // Arrange
        long accountId = 1L;
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + accountId + "/exchange?fromCurrency=" + fromCurrency + "&toCurrency=" + toCurrency + "&amount=" + amount,
                null, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Exchange successful");
    }

    @Test
    public void testInvalidCurrency() {
        // Arrange
        long accountId = 1L;
        String invalidCurrency = "ABC";
        BigDecimal amount = BigDecimal.valueOf(100);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + accountId + "/deposit?currency=" + invalidCurrency + "&amount=" + amount,
                null, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

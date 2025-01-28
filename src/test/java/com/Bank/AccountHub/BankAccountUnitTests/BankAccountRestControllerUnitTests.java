package com.Bank.AccountHub.BankAccountUnitTests;

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
public class BankAccountRestControllerUnitTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testDepositMoney() {
        // Arrange
        long accountId = 1L;
        DepositRequestDTO depositRequestDTO = new DepositRequestDTO(CurrencyType.USD, BigDecimal.valueOf(1));

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + accountId + "/deposit",
                depositRequestDTO, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Deposit successful");
    }

    @Test
    public void testDebitMoney() {
        // Arrange
        long accountId = 1L;
        DebitRequestDTO debitRequestDTO = new DebitRequestDTO(CurrencyType.USD, BigDecimal.valueOf(1));

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + accountId + "/debit",
                debitRequestDTO, String.class);

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
        ExchangeRequestDTO exchangeRequestDTO = new ExchangeRequestDTO(CurrencyType.USD, CurrencyType.EUR, BigDecimal.valueOf(1));

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/accounts/" + accountId + "/exchange",
                exchangeRequestDTO, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Exchange successful");
    }
}

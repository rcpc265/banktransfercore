package com.portfolio.banktransfercore.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class AccountEntity {

  @Id private UUID id;

  @Column(name = "account_number", unique = true, nullable = false)
  private String accountNumber;

  @Column(nullable = false)
  private BigDecimal balance;

  @Column(nullable = false)
  private String currency;

  @Column(nullable = false)
  private String status;

  protected AccountEntity() {}

  public AccountEntity(
      UUID id, String accountNumber, BigDecimal balance, String currency, String status) {
    this.id = id;
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.currency = currency;
    this.status = status;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}

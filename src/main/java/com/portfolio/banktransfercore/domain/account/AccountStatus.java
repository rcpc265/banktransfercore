package com.portfolio.banktransfercore.domain.account;

import com.portfolio.banktransfercore.domain.shared.money.Money;

/**
 * Lifecycle states for a bank {@link Account}.
 *
 * <p>Uses the State Pattern via enum constants, with a "Deny by Default" philosophy: every
 * operation and transition is blocked unless explicitly permitted by a state.
 *
 * <p>Valid transitions:
 *
 * <pre>
 *   ACTIVE              → UNDER_INVESTIGATION, FROZEN, DORMANT, CLOSED
 *   UNDER_INVESTIGATION → ACTIVE, FROZEN, CLOSED
 *   FROZEN              → ACTIVE, CLOSED
 *   DORMANT             → ACTIVE, CLOSED
 *   CLOSED              → (terminal, no transitions allowed)
 * </pre>
 */
public enum AccountStatus {

  /**
   * The account is fully operational. Allows all financial operations (debit and credit). Can
   * transition to any non-terminal state.
   */
  ACTIVE {
    @Override
    public void validateDebit(Money amount) {
      // Permitted.
    }

    @Override
    public void validateCredit(Money amount) {
      // Permitted.
    }

    @Override
    public AccountStatus onInvestigate() {
      return UNDER_INVESTIGATION;
    }

    @Override
    public AccountStatus onFreeze() {
      return FROZEN;
    }

    @Override
    public AccountStatus onMarkDormant() {
      return DORMANT;
    }

    @Override
    public AccountStatus onClose() {
      return CLOSED;
    }
  },

  /**
   * The account is under investigation for suspicious activity. Blocks outgoing money (debit), but
   * allows incoming deposits (credit). Can transition to ACTIVE, FROZEN, or CLOSED.
   */
  UNDER_INVESTIGATION {
    @Override
    public void validateCredit(Money amount) {
      // Deposits are still allowed during investigation.
    }

    @Override
    public AccountStatus onReactivate() {
      return ACTIVE;
    }

    @Override
    public AccountStatus onFreeze() {
      return FROZEN;
    }

    @Override
    public AccountStatus onClose() {
      return CLOSED;
    }
  },

  /**
   * The account is completely frozen (e.g. court order, KYC failure). No money can enter or leave.
   * Can transition to ACTIVE or CLOSED.
   */
  FROZEN {
    // Inherits Deny by Default for both debit and credit. Blocks everything.

    @Override
    public AccountStatus onReactivate() {
      return ACTIVE;
    }

    @Override
    public AccountStatus onClose() {
      return CLOSED;
    }
  },

  /**
   * The account has been inactive for an extended period (typically 12-24 months). Blocks all
   * financial operations until reactivated. Can transition to ACTIVE or CLOSED.
   */
  DORMANT {
    // Inherits Deny by Default for both debit and credit. Blocks everything.

    @Override
    public AccountStatus onReactivate() {
      return ACTIVE;
    }

    @Override
    public AccountStatus onClose() {
      return CLOSED;
    }
  },

  /**
   * The account is permanently closed. This is a terminal state. No financial operations are
   * allowed. No transitions are possible.
   */
  CLOSED {
    // Inherits Deny by Default for everything. Terminal state.
  };

  // --- Financial Operation Validations (Deny by Default) ---

  /**
   * Validates whether a debit operation is allowed in the current state. Deny by Default: throws
   * {@link IllegalStateException} unless overridden.
   *
   * @param amount the amount to debit
   * @throws IllegalStateException if debit is not allowed in this state
   */
  public void validateDebit(Money amount) {
    throw new IllegalStateException(
        "Cannot debit money from account in " + this.name() + " status.");
  }

  /**
   * Validates whether a credit operation is allowed in the current state. Deny by Default: throws
   * {@link IllegalStateException} unless overridden.
   *
   * @param amount the amount to credit
   * @throws IllegalStateException if credit is not allowed in this state
   */
  public void validateCredit(Money amount) {
    throw new IllegalStateException(
        "Cannot credit money to account in " + this.name() + " status.");
  }

  // --- State Transitions (Deny by Default) ---

  /**
   * Transitions the account to {@link #UNDER_INVESTIGATION}. Only valid from {@link #ACTIVE}.
   *
   * @throws IllegalStateException if the transition is not allowed from the current status
   */
  public AccountStatus onInvestigate() {
    throw new IllegalStateException("Cannot investigate account from " + this.name() + " status.");
  }

  /**
   * Transitions the account to {@link #FROZEN}. Valid from {@link #ACTIVE} or {@link
   * #UNDER_INVESTIGATION}.
   *
   * @throws IllegalStateException if the transition is not allowed from the current status
   */
  public AccountStatus onFreeze() {
    throw new IllegalStateException("Cannot freeze account from " + this.name() + " status.");
  }

  /**
   * Transitions the account to {@link #DORMANT}. Only valid from {@link #ACTIVE}.
   *
   * @throws IllegalStateException if the transition is not allowed from the current status
   */
  public AccountStatus onMarkDormant() {
    throw new IllegalStateException(
        "Cannot mark account as dormant from " + this.name() + " status.");
  }

  /**
   * Transitions the account to {@link #CLOSED}. One-way terminal transition. Valid from {@link
   * #ACTIVE}, {@link #UNDER_INVESTIGATION}, {@link #FROZEN}, or {@link #DORMANT}.
   *
   * @throws IllegalStateException if the transition is not allowed from the current status
   */
  public AccountStatus onClose() {
    throw new IllegalStateException("Cannot close account from " + this.name() + " status.");
  }

  /**
   * Transitions the account back to {@link #ACTIVE}. Valid from {@link #UNDER_INVESTIGATION},
   * {@link #FROZEN}, or {@link #DORMANT}.
   *
   * @throws IllegalStateException if the transition is not allowed from the current status
   */
  public AccountStatus onReactivate() {
    throw new IllegalStateException("Cannot reactivate account from " + this.name() + " status.");
  }
}

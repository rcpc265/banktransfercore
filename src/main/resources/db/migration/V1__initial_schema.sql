CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    balance DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE transfers (
    id UUID PRIMARY KEY,
    source_account_number VARCHAR(20) NOT NULL,
    destination_account_number VARCHAR(20) NOT NULL,
    amount DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

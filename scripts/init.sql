CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT PRIMARY KEY,
    balance DECIMAL(19, 2) NOT NULL CHECK (balance >= 0)
);

CREATE TABLE IF NOT EXISTS transfers (
    id UUID PRIMARY KEY,
    from_account_id BIGINT REFERENCES accounts(id),
    to_account_id BIGINT REFERENCES accounts(id),
    amount DECIMAL(19, 2) NOT NULL,
    status VARCHAR(50) NOT NULL
);
CREATE TABLE transactions
(
    transaction_id BINARY(16)   NOT NULL,
    created_date   datetime     NULL,
    processed_date datetime     NULL,
    amount         DECIMAL      NOT NULL DEFAULT 0,
    exchange       DECIMAL      NOT NULL DEFAULT 1,
    operation_type VARCHAR(255) NOT NULL,
    status         VARCHAR(255) NOT NULL,
    message        VARCHAR(255) NULL,
    currency_type  VARCHAR(255) NOT NULL,
    user_id        BIGINT       NOT NULL,
    destAccount    BIGINT       NULL,
    CONSTRAINT pk_transactions PRIMARY KEY (transaction_id)
);

CREATE INDEX idx_transactions_created_date
    ON transactions (created_date);

CREATE INDEX idx_transactions_filters
    ON transactions (created_date, operation_type, status, currency_type);

CREATE INDEX idx_transactions_user
    ON transactions (user_id, created_date);

CREATE INDEX idx_transactions_status
    ON transactions (status);

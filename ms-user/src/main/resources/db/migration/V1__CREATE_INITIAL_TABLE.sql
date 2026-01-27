CREATE TABLE customers
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255)          NOT NULL,
    email      VARCHAR(100)          NOT NULL,
    password   VARCHAR(255)          NOT NULL,
    created_at datetime              NULL,
    CONSTRAINT pk_customers PRIMARY KEY (id)
);

CREATE TABLE refresh_tokens
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    token      VARCHAR(255)          NOT NULL,
    email      VARCHAR(255)          NOT NULL,
    expires_in datetime              NOT NULL,
    created_at datetime              NOT NULL,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id)
);

ALTER TABLE customers
    ADD CONSTRAINT uc_customers_email UNIQUE (email);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT uc_refresh_tokens_email UNIQUE (email);

ALTER TABLE refresh_tokens
    ADD CONSTRAINT uc_refresh_tokens_token UNIQUE (token);
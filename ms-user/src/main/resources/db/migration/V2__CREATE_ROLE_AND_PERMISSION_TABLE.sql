CREATE TABLE roles
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE permissions
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_permissions PRIMARY KEY (id)
);

CREATE TABLE customer_roles
(
    customer_id BIGINT NOT NULL,
    role_id     BIGINT NOT NULL,
    CONSTRAINT pk_customer_roles PRIMARY KEY (customer_id, role_id)
);

CREATE TABLE roles_permissions
(
    role_id BIGINT NOT NULL,
    permissions_id BIGINT NOT NULL,
    CONSTRAINT pk_roles_permissions PRIMARY KEY (role_id, permissions_id)
);


ALTER TABLE permissions
    ADD CONSTRAINT uc_permissions_name UNIQUE (name);


ALTER TABLE roles
    ADD CONSTRAINT uc_roles_name UNIQUE (name);

ALTER TABLE customer_roles
    ADD CONSTRAINT fk_cusrol_on_customer_entity FOREIGN KEY (customer_id) REFERENCES customers (id);

ALTER TABLE customer_roles
    ADD CONSTRAINT fk_cusrol_on_role_entity FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE roles_permissions
    ADD CONSTRAINT fk_rolper_on_permission_entity FOREIGN KEY (permissions_id) REFERENCES permissions (id);

ALTER TABLE roles_permissions
    ADD CONSTRAINT fk_rolper_on_role_entity FOREIGN KEY (role_id) REFERENCES roles (id);
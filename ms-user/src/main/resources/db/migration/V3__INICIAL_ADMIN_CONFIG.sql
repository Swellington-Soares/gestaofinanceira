#senha = 123456
INSERT INTO customers(id, name, email, password, created_at) VALUES (1, 'Admin', 'admin@admin.dev', '$2a$12$a0DLRV6T0VFQ.XJdhKMn8eEBwWy6Ln5CnCAcz/4Mkgwt7zQHA5VLy', NOW());
INSERT INTO roles(name) VALUES ('ADMIN');
INSERT INTO roles(name) VALUES ('USER');
INSERT INTO customer_roles(customer_id, role_id) VALUES (1,1);
INSERT INTO customer_roles(customer_id, role_id) VALUES (1,2);


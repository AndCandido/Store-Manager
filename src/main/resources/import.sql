INSERT INTO TB_PRODUCTS (REF, NAME, PRICE , STOCK_QUANTITY, CREATED_AT) VALUES (3484782394, 'Calça Masc', 149.99, 5, now()), (43294082304, 'Bermuda Juv', 70, 10, now()), (3924872394, 'Saia Jeans', 110.05, 1, now()), (3892743278942, 'Carrinho de Brinquedo', 19.90, 15, now()), (54905349, 'Toalha', 22.5, 25, now());
INSERT INTO TB_CUSTOMERS (ID, NAME, NICKNAME, CPF, ADDRESS, PHONE, CREATED_AT) VALUES ('b7473980-d54c-49a8-8412-986ac5e659ad', 'Alberto Lima Castro', NULL, '021.419.780-84', 'Rua. Av. Bairro Santarém', NULL, now())
INSERT INTO TB_USERS (ID, ROLE, PASSWORD, USERNAME) VALUES (random_uuid(), 0, '$2a$10$Plew4urXjngsHiuI4rCen.WvBWI6l/TwGwaFngEUVDICP6hRWlw1G', 'and')
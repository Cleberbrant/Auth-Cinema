-- Inserir localidade padrão
INSERT INTO localidade (endereco, cep, referencia)
VALUES ('QR 312 CONJUNTO F CASA 03', '72542506', 'perto creche');

-- Inserir admin
INSERT INTO usuario (nome, data_nascimento, cpf, email, password, role, localidade_id)
VALUES (
           'Administrador',
           '2000-01-01',
           '11111111111',
           'admin@gmail.com',
           '$2a$12$H0x6cnrPoVUyvm9CqHh9jO4etIC6I103z5SU5KQQDf7sdNdDvEPGq', -- hash da senha admin
           'ROLE_ADMIN',
           1 -- id da localidade inserida acima
       );

-- Inserir usuário comum
INSERT INTO usuario (nome, data_nascimento, cpf, email, password, role, localidade_id)
VALUES (
           'Usuário Comum',
           '2000-01-01',
           '22222222222',
           'usuario@gmail.com',
           '$2a$12$ChsP7RhTHlnrS2bEtYpRieSdhFZC5oVF6abu4Q8wWhovbwE0mj.zG', -- hash da senha usuario
           'ROLE_USER',
           1
       );

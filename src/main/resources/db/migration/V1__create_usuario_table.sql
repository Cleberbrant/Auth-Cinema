-- Criação da tabela localidade
CREATE TABLE IF NOT EXISTS localidade (
                                          id SERIAL PRIMARY KEY,
                                          endereco VARCHAR(255) NOT NULL,
                                          cep VARCHAR(20) NOT NULL,
                                          referencia VARCHAR(255)
);

-- Criação da tabela usuario
CREATE TABLE IF NOT EXISTS usuario (
                                       id SERIAL PRIMARY KEY,
                                       nome VARCHAR(255) NOT NULL,
                                       data_nascimento DATE NOT NULL,
                                       cpf VARCHAR(14) NOT NULL UNIQUE,
                                       email VARCHAR(255) NOT NULL UNIQUE,
                                       password VARCHAR(255) NOT NULL,
                                       role VARCHAR(50) NOT NULL,
                                       localidade_id INTEGER NOT NULL REFERENCES localidade(id)
);
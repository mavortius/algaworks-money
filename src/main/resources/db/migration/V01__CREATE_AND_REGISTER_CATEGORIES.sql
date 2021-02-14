CREATE TABLE categories (
  id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

INSERT INTO categories (name) VALUES ('Lazer');
INSERT INTO categories (name) VALUES ('Alimentaçao');
INSERT INTO categories (name) VALUES ('Supermercado');
INSERT INTO categories (name) VALUES ('Farmacia');
INSERT INTO categories (name) VALUES ('Outros');
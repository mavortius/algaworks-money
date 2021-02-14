CREATE TABLE states
(
    id   BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO states (id, name)
VALUES (1, 'Acre');
INSERT INTO states (id, name)
VALUES (2, 'Alagoas');
INSERT INTO states (id, name)
VALUES (3, 'Amazonas');
INSERT INTO states (id, name)
VALUES (4, 'Amapá');
INSERT INTO states (id, name)
VALUES (5, 'Bahia');
INSERT INTO states (id, name)
VALUES (6, 'Ceará');
INSERT INTO states (id, name)
VALUES (7, 'Distrito Federal');
INSERT INTO states (id, name)
VALUES (8, 'Espírito Santo');
INSERT INTO states (id, name)
VALUES (9, 'Goiás');
INSERT INTO states (id, name)
VALUES (10, 'Maranhão');
INSERT INTO states (id, name)
VALUES (11, 'Minas Gerais');
INSERT INTO states (id, name)
VALUES (12, 'Mato Grosso do Sul');
INSERT INTO states (id, name)
VALUES (13, 'Mato Grosso');
INSERT INTO states (id, name)
VALUES (14, 'Pará');
INSERT INTO states (id, name)
VALUES (15, 'Paraíba');
INSERT INTO states (id, name)
VALUES (16, 'Pernambuco');
INSERT INTO states (id, name)
VALUES (17, 'Piauí');
INSERT INTO states (id, name)
VALUES (18, 'Paraná');
INSERT INTO states (id, name)
VALUES (19, 'Rio de Janeiro');
INSERT INTO states (id, name)
VALUES (20, 'Rio Grande do Norte');
INSERT INTO states (id, name)
VALUES (21, 'Rondônia');
INSERT INTO states (id, name)
VALUES (22, 'Roraima');
INSERT INTO states (id, name)
VALUES (23, 'Rio Grande do Sul');
INSERT INTO states (id, name)
VALUES (24, 'Santa Catarina');
INSERT INTO states (id, name)
VALUES (25, 'Sergipe');
INSERT INTO states (id, name)
VALUES (26, 'São Paulo');
INSERT INTO states (id, name)
VALUES (27, 'Tocantins');

CREATE TABLE cities
(
    id       BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    name     VARCHAR(50) NOT NULL,
    state_id BIGINT(20)  NOT NULL,
    FOREIGN KEY (state_id) REFERENCES states (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO cities (id, name, state_id)
VALUES (1, 'Belo Horizonte', 11);
INSERT INTO cities (id, name, state_id)
VALUES (2, 'Uberlândia', 11);
INSERT INTO cities (id, name, state_id)
VALUES (3, 'Uberaba', 11);
INSERT INTO cities (id, name, state_id)
VALUES (4, 'São Paulo', 26);
INSERT INTO cities (id, name, state_id)
VALUES (5, 'Campinas', 26);
INSERT INTO cities (id, name, state_id)
VALUES (6, 'Rio de Janeiro', 19);
INSERT INTO cities (id, name, state_id)
VALUES (7, 'Angra dos Reis', 19);
INSERT INTO cities (id, name, state_id)
VALUES (8, 'Goiânia', 9);
INSERT INTO cities (id, name, state_id)
VALUES (9, 'Caldas Novas', 9);

ALTER TABLE people
    DROP COLUMN city;
ALTER TABLE people
    DROP COLUMN state;
ALTER TABLE people
    ADD COLUMN city_id BIGINT(20);
ALTER TABLE people
    ADD CONSTRAINT fk_people_city FOREIGN KEY (city_id) REFERENCES cities (id);

UPDATE people
SET city_id = 2;

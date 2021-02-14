CREATE TABLE account_entries
(
    id           BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    description  VARCHAR(50)    NOT NULL,
    due_date     DATE           NOT NULL,
    payment_date DATE,
    value        DECIMAL(10, 2) NOT NULL,
    observation  VARCHAR(100),
    type         VARCHAR(20)    NOT NULL,
    category_id  BIGINT(20)     NOT NULL,
    person_id    BIGINT(20)     NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories (id),
    FOREIGN KEY (person_id) REFERENCES people (id)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Salário mensal', '2018-01-27', null, 6500.00, 'Distribuição de lucros', 'RECEITA', 1, 1);
INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Supermercado', '2018-03-10', '2018-03-01', 100.32, null, 'DESPESA', 2, 2);
INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Academia', '2018-04-10', null, 120, null, 'DESPESA', 3, 3);
INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Conta de luz', '2018-02-10', '2018-02-10', 110.44, null, 'DESPESA', 3, 4);
INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Conta de água', '2018-02-15', null, 200.30, null, 'DESPESA', 3, 5);
INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Restaurante', '2018-03-14', '2018-03-14', 1010.32, null, 'DESPESA', 4, 6);
INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Venda vídeo game', '2018-01-01', null, 500, null, 'RECEITA', 1, 7);
INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Clube', '2018-03-07', '2018-03-05', 400.32, null, 'DESPESA', 4, 8);
INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Impostos', '2018-04-10', null, 123.64, 'Multas', 'DESPESA', 3, 9);
INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Multa', '2018-04-10', null, 665.33, null, 'DESPESA', 5, 10);
INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Padaria', '2018-02-28', '2018-02-28', 8.32, null, 'DESPESA', 1, 5);
INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Papelaria', '2018-02-10', '2018-04-10', 2100.32, null, 'DESPESA', 5, 4);
INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Almoço', '2018-03-09', null, 1040.32, null, 'DESPESA', 4, 3);
INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Café', '2018-02-20', '2018-02-18', 4.32, null, 'DESPESA', 4, 2);
INSERT INTO account_entries (description, due_date, payment_date, value, observation, type, category_id,
                           person_id)
values ('Lanche', '2018-04-10', null, 10.20, null, 'DESPESA', 4, 1);
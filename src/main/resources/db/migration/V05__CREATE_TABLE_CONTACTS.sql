CREATE TABLE contacts
(
    id        BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    person_id BIGINT(20)   NOT NULL,
    name      VARCHAR(50)  NOT NULL,
    email     VARCHAR(100) NOT NULL,
    telephone VARCHAR(20)  NOT NULL,
    FOREIGN KEY (person_id) REFERENCES people (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

insert into contacts (id, person_id, name, email, telephone)
values (1, 1, 'Marcos Henrique', 'marcos@algamoney.com', '00 0000-0000');

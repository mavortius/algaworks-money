CREATE TABLE users
(
    id       BIGINT(20) PRIMARY KEY,
    name     VARCHAR(50)  NOT NULL,
    email    VARCHAR(50)  NOT NULL,
    password VARCHAR(150) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE permissions
(
    id          BIGINT(20) PRIMARY KEY,
    description VARCHAR(50) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE users_permissions
(
    user_id       BIGINT(20) NOT NULL,
    permission_id BIGINT(20) NOT NULL,
    PRIMARY KEY (user_id, permission_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (permission_id) REFERENCES permissions (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO users (id, name, email, password)
values (1, 'Administrador', 'admin@algamoney.com', '$2a$10$Zz2uITybQI7JWx6dQR5PbeDyiXYZ5W4TB7TdE7As3BJmUKc0RYcHC');
INSERT INTO users (id, name, email, password)
values (2, 'Maria Silva', 'maria@algamoney.com', '$2a$10$Zc3w6HyuPOPXamaMhh.PQOXvDnEsadztbfi6/RyZWJDzimE8WQjaq');

INSERT INTO permissions (id, description)
values (1, 'ROLE_REGISTER_CATEGORY');
INSERT INTO permissions (id, description)
values (2, 'ROLE_READ_CATEGORY');

INSERT INTO permissions (id, description)
values (3, 'ROLE_REGISTER_PERSON');
INSERT INTO permissions (id, description)
values (4, 'ROLE_DELETE_PERSON');
INSERT INTO permissions (id, description)
values (5, 'ROLE_READ_PERSON');

INSERT INTO permissions (id, description)
values (6, 'ROLE_REGISTER_ACCOUNT_ENTRY');
INSERT INTO permissions (id, description)
values (7, 'ROLE_DELETE_ACCOUNT_ENTRY');
INSERT INTO permissions (id, description)
values (8, 'ROLE_READ_ACCOUNT_ENTRY');

-- admin
INSERT INTO users_permissions (user_id, permission_id)
values (1, 1);
INSERT INTO users_permissions (user_id, permission_id)
values (1, 2);
INSERT INTO users_permissions (user_id, permission_id)
values (1, 3);
INSERT INTO users_permissions (user_id, permission_id)
values (1, 4);
INSERT INTO users_permissions (user_id, permission_id)
values (1, 5);
INSERT INTO users_permissions (user_id, permission_id)
values (1, 6);
INSERT INTO users_permissions (user_id, permission_id)
values (1, 7);
INSERT INTO users_permissions (user_id, permission_id)
values (1, 8);

-- maria
INSERT INTO users_permissions (user_id, permission_id)
values (2, 2);
INSERT INTO users_permissions (user_id, permission_id)
values (2, 5);
INSERT INTO users_permissions (user_id, permission_id)
values (2, 8);

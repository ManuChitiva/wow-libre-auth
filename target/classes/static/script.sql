/* ACCOUNT WEB */
/*
  DESCRIPTION : Responsible for managing the system's web services.
*/

CREATE TABLE auth.rol
(
    id     bigint AUTO_INCREMENT NOT NULL,
    name   varchar(50) NOT NULL,
    status boolean     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT name_uq UNIQUE (name)
);

INSERT INTO auth.rol (name, status)
VALUES ('ADMIN', TRUE);
INSERT INTO auth.rol (name, status)
VALUES ('CLIENT', TRUE);
INSERT INTO auth.rol (name, status)
VALUES ('SUPPORT', TRUE);



CREATE TABLE auth.account_web
(
    id            bigint auto_increment NOT NULL,
    country       varchar(30) NOT NULL,
    date_of_birth DATE        NOT NULL,
    first_name    varchar(30) NOT NULL,
    last_name     varchar(30) NOT NULL,
    cell_phone    varchar(20) NOT NULL,
    password      text        not null,
    status        boolean,
    verified      boolean,
    avatar_url    text,
    email         varchar(40) NOT NULL,
    CONSTRAINT uq_email UNIQUE (email),
    PRIMARY KEY (id)
);


ALTER TABLE AUTH.account_web
    ADD COLUMN rol_id BIGINT NOT NULL,
ADD CONSTRAINT fk_account_web_rol_id FOREIGN KEY (rol_id) REFERENCES AUTH.rol(id);

ALTER TABLE auth.account
    ADD COLUMN account_web bigint,
 ADD CONSTRAINT FOREIGN KEY (account_web) REFERENCES auth.account_web (id);


ALTER TABLE characters.guild
    ADD COLUMN public_access boolean DEFAULT TRUE;


CREATE TABLE characters.benefit
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    logo text,
    title       varchar(255) NOT NULL,
    sub_title   TEXT         NOT NULL,
    description TEXT,
    PRIMARY KEY (id),
    CONSTRAINT title_uq UNIQUE (title)
);



CREATE TABLE characters.guild_benefits
(
    id               bigint auto_increment NOT NULL,
    guild_id         bigint NOT NULL,
    acquisition_date DATE   NOT NULL,
    expiration_date  DATE   NOT NULL,
    benefit_id       bigint not null,
    PRIMARY KEY (id),
    constraint fk_benefit_id FOREIGN key (benefit_id) references characters.guild_benefits (id)
);

INSERT INTO characters.benefit (title,logo, sub_title, description)
VALUES ('Mas De 500 Miembros', "", "Mas De 500 Miembros", "Al unirte a esta hermandad podras reclamar el beneficio de los 500 miembros.");

INSERT INTO characters.benefit (title,logo, sub_title, description)
VALUES ('Banco Con Todas Las Casillas',"", "Banco Con Todas Las Casillas",
        "Al unirte a esta hermandad, podrás disfrutar del beneficio de contar con un compañero");

INSERT INTO characters.benefit (title,logo, sub_title, description)
VALUES ('Acumulador',"", "Acumulador", "Al unirte a esta hermandad, podras reclamar el beneficio de una montura");


alter table characters.guild
    add column banner_primary text,
	add column banner_secondary text,
    add column logo text;


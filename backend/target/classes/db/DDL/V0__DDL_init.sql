-- liquibase formatted sql
-- changeset sowjanyaguntupalli-vibullion:V0__DDL_init.sql
-- comment: V0__DDL_init.sql

CREATE TABLE LIVE_RATE_CONFIG (
                                  ID             BINARY(16) PRIMARY KEY,
                                  SPREAD_CHARGES    FLOAT NOT NULL,
                                  ASK            FLOAT DEFAULT 0,
                                  BUY            FLOAT DEFAULT 0,
                                  SYMBOL         VARCHAR(200) NOT NULL
);

CREATE TABLE USER_DETAILS (
                              ID BINARY(16) PRIMARY KEY,
                              PHONE_NUMBER VARCHAR(10) NOT NULL,
                              OTP INTEGER
);
-- liquibase formatted sql
-- changeset sowjanyaguntupalli-vibullion:V0__DML_init_data.sql
-- comment: V0__DML_init_data.sql

INSERT INTO USER_DETAILS (ID, PHONE_NUMBER, OTP) VALUES (UUID_TO_BIN(UUID()), '9059286366', NULL);
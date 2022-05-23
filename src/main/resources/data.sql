-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlDialectInspectionForFile

INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date) VALUES ('admin', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Nikola', 'Nikolic', 'admin@example.com', true, '2017-10-01');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date) VALUES ('user', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Marko', 'Markovic', 'user@example.com', true, '2017-10-01');

INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
INSERT INTO ROLE (name) VALUES ('ROLE_OWNER');
INSERT INTO ROLE (name) VALUES ('ROLE_RENTER');

INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 2); -- user-u dodeljujemo rolu ADMIN

INSERT INTO PRIVILEGE (name) VALUES ('READ_USER');
INSERT INTO PRIVILEGE (name) VALUES ('READ_USERS');
INSERT INTO PRIVILEGE (name) VALUES ('FIND_USER');
INSERT INTO PRIVILEGE (name) VALUES ('CREATE_USER');
INSERT INTO PRIVILEGE (name) VALUES ('READ_REAL_ESTATE');
INSERT INTO PRIVILEGE (name) VALUES ('READ_REAL_ESTATES');
INSERT INTO PRIVILEGE (name) VALUES ('FIND_REAL_ESTATE');
INSERT INTO PRIVILEGE (name) VALUES ('CREATE_REAL_ESTATE');

INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 1);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 2);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 3);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 4);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 5);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 6);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 7);
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 8);

INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (2, 7);

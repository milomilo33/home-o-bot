-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlDialectInspectionForFile

INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date) VALUES ('admin', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Nikola', 'Nikolic', 'admin@example.com', true, '2017-10-01');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date) VALUES ('owner1', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Marko', 'Markovic', 'user@example.com', true, '2017-10-01');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date) VALUES ('owner2', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Karen', 'Karenkovic', 'kk@example.com', true, '2017-10-01');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date) VALUES ('owner3', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'John', 'Johnson', 'john@example.com', true, '2017-10-01');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date) VALUES ('renter1', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Renter', 'Renterovic', 'renter@example.com', true, '2017-10-01');
INSERT INTO USERS (username, password, first_name, last_name, email, enabled, last_password_reset_date) VALUES ('renter2', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'Aleksandar', 'Vucic', 'av@example.com', true, '2017-10-01');

INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
INSERT INTO ROLE (name) VALUES ('ROLE_OWNER');
INSERT INTO ROLE (name) VALUES ('ROLE_RENTER');
-- INSERT INTO ROLE (name) VALUES ('ROLE_USER');

INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 1); -- user-u dodeljujemo rolu USER
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 2); -- user-u dodeljujemo rolu ADMIN
INSERT INTO USER_ROLE (user_id, role_id) VALUES (3, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (4, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (5, 3);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (6, 3);

INSERT INTO PRIVILEGE (name) VALUES ('READ_USER');
INSERT INTO PRIVILEGE (name) VALUES ('READ_USERS');
INSERT INTO PRIVILEGE (name) VALUES ('FIND_USER');
INSERT INTO PRIVILEGE (name) VALUES ('CREATE_USER');
INSERT INTO PRIVILEGE (name) VALUES ('UPDATE_USER');
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
INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (1, 9);

INSERT INTO ROLE_PRIVILEGE (role_id, privilege_id) VALUES (2, 7);

insert into real_estate (name) values ('Hilton Apartment');
insert into real_estate (name) values ('MRVN''s Paint Shop');
insert into real_estate (name) values ('Karen''s Residence');

insert into real_estate_device_names (real_estate_id, device_names) values (1, 'fridge-1');
insert into real_estate_device_names (real_estate_id, device_names) values (1, 'door-1');
insert into real_estate_device_names (real_estate_id, device_names) values (2, 'door-2');
insert into real_estate_device_names (real_estate_id, device_names) values (2, 'doorbell-2');
insert into real_estate_device_names (real_estate_id, device_names) values (3, 'fridge-3');
insert into real_estate_device_names (real_estate_id, device_names) values (3, 'door-3');
insert into real_estate_device_names (real_estate_id, device_names) values (3, 'doorbell-3');

create table user (
id int primary key,
name varchar(100),
password_id int
)

create or replace table  password (
id int primary key,
password varchar(100),
encryption varchar(10)
)
ALTER TABLE user  ADD CONSTRAINT user_password_id
   FOREIGN KEY(password_id) REFERENCES password (ID) ON DELETE SET DEFAULT;

drop table if exists users;
create table users (
user_id varchar(255) primary key,
name varchar(255),
email varchar(500) not null unique,
mobile varchar(10) not null unique,
current_status tinyint not null,
password VARCHAR(255) not null,
role varchar(20) not null,
created_at datetime
)

drop table if exists otp_data;
create table otp_data (
otp_id varchar(255) primary key,
otp varchar(6) not null,
user_id varchar(255) not null,
created_at datetime,
verification_attempts tinyint,
resend_attempts tinyint,
is_valid tinyint,
CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(user_id)  
)
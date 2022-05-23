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
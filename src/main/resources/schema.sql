create table  IF NOT EXISTS  TB_MEMBER (
    register_id int not null auto_increment,
    member_name varchar(255) not null,
    password varchar(255) not null,
    phone_number varchar(255) not null,
    pets varchar(255) not null,
    primary key (member_name)
);
create database if not exists messagedelivery;

USE messagedelivery;

drop table if exists batch;

drop table if exists message;

create table batch (
    id bigint not null auto_increment,
    creation_date datetime,
    creation_user varchar(255),
    deleted bit not null,
    last_modification_date datetime,
    last_modification_user varchar(255),
    intent integer not null,
    result varchar(255),
    status varchar(255),
    primary key (id)
);
    
create table message (
    id bigint not null auto_increment,
    creation_date datetime,
    creation_user varchar(255),
    deleted bit not null,
    last_modification_date datetime,
    last_modification_user varchar(255),
    code_reference varchar(255),
    content varchar(255),
    delivery_date datetime,
    from_account varchar(255),
    payload_id varchar(255),
    reply_to varchar(255),
    scheduled_delivery_date datetime,
    sent bit not null,
    source varchar(255),
    subject varchar(255),
    to_account varchar(255),
    type varchar(255),
    batch_id bigint,
    primary key (id)
);

ALTER TABLE message ADD CONSTRAINT FK_message_batch FOREIGN KEY (batch_id) REFERENCES batch(id);
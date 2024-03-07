create sequence cat_comment_seq start with 1 increment by 50;
create table cat (
    id varchar(255) not null,
    mime_type varchar(255),
    tags varchar(255),
    primary key (id)
);
create table cat_comment (
    id bigint not null,
    cat_id varchar(255),
    comment varchar(255),
    primary key (id)
);
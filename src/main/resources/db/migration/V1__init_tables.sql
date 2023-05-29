create table if not exists car
(
    id       bigserial
        primary key,
    brand    varchar(50),
    car_type varchar(50),
    color    varchar(50),
    model    varchar(50),
    price    numeric(38, 2)
);

create index if not exists idx_car_type
    on car (car_type);

create table if not exists t_user
(
    id        bigserial not null
        primary key,
    email     varchar(255)
        constraint uk_i6qjjoe560mee5ajdg7v1o6mi
            unique,
    firstname varchar(50),
    lastname  varchar(50),
    password  varchar(100),
    role      varchar(20)
);

create table if not exists token
(
    id         bigserial not null
        primary key,
    expired    boolean not null,
    revoked    boolean not null,
    token      varchar(255)
        constraint uk_pddrhgwxnms2aceeku9s2ewy5
            unique,
    token_type varchar(255),
    user_id    integer
        constraint fk9j8xaotyyn1neyhx4cb87oxjl
            references t_user
);
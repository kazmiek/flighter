create table airplane
(
    id       bigint       not null AUTO_INCREMENT primary key,
    capacity int          null,
    code     varchar(255) null,
    type     varchar(255) null,
    index airplane_code_idx(code)
);

create table airport
(
    id     bigint       not null AUTO_INCREMENT primary key,
    city   varchar(255) null,
    code   varchar(255) null unique,
    name   varchar(255) null,
    nation varchar(255) null
);

create table flight
(
    id                   bigint       not null AUTO_INCREMENT primary key,
    arrival_time         datetime(6)  null,
    departure_time       datetime(6)  null,
    duration             decimal(21)  null,
    number               varchar(255) null,
    status               varchar(255) null,
    airplane_id          bigint       not null,
    arrival_airport_id   bigint       not null,
    departure_airport_id bigint       not null,
    constraint flight_airplane_id foreign key (airplane_id) references airplane (id),
    constraint flight_departure_airport_id foreign key (departure_airport_id) references airport (id),
    constraint flight_arrival_airport_id foreign key (arrival_airport_id) references airport (id),
    index flight_departure_time_idx(departure_time)
);

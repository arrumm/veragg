alter table if exists auction
    drop constraint if exists auction_to_court_id;

alter table if exists auction
    drop constraint if exists auction_to_source_id;

alter table if exists auction_property_types
    drop constraint if exists auction_property_types_to_auction_id;

alter table if exists court
    drop constraint if exists court_to_state_id;

alter table if exists document
    drop constraint if exists document_to_auction_id;

alter table if exists state_zip_codes
    drop constraint if exists state_zip_codes_to_state_id;

drop table if exists auction cascade;

drop table if exists auction_property_types cascade;

drop table if exists auction_sources cascade;

drop table if exists court cascade;

drop table if exists document cascade;

drop table if exists state_zip_codes cascade;

drop table if exists states cascade;

drop sequence if exists hibernate_sequence;

create sequence hibernate_sequence start 1 increment 1;

create table auction
(
    id                            int8 not null,
    city                          varchar(255),
    flat                          varchar(255),
    number                        varchar(255),
    street                        varchar(255),
    zip_code                      varchar(255),
    amount                        int4,
    appointment                   timestamp,
    updated_on                    timestamp,
    created_on                    timestamp,
    auction_status                varchar(255),
    buy_limit                     varchar(255),
    expertise_description         text,
    outdoor_description           text,
    property_building_description text,
    property_plot_description     text,
    file_number                   varchar(255),
    source_url                    varchar(255),
    court_id                      int8,
    source_id                     int4,
    primary key (id)
);

create table auction_property_types
(
    auction_id     int8 not null,
    property_types varchar(255)
);

create table auction_sources
(
    id          int4 not null,
    source_type varchar(255),
    base_url    varchar(255),
    name        varchar(255),
    priority    int4,
    primary key (id)
);

create table court
(
    id       int8 not null,
    name     varchar(255),
    state_id varchar(255),
    city     varchar(255),
    flat     varchar(255),
    number   varchar(255),
    street   varchar(255),
    zip_code varchar(255),
    primary key (id)
);

create table document
(
    id                 int8 not null,
    document_type      varchar(255),
    file_path          varchar(255),
    file_type          varchar(255),
    original_file_name varchar(255),
    sort_order         int4,
    store_name         varchar(255),
    url                varchar(255),
    auction_id         int8,
    primary key (id)
);

create table state_zip_codes
(
    id                     SERIAL NOT NULL,
    location               varchar(255),
    location_addition      varchar(255),
    location_with_addition varchar(255),
    zip_code               varchar(255),
    state_id               varchar(255),
    primary key (id)
);

create table states
(
    id   varchar(255) not null,
    name varchar(255),
    primary key (id)
);

alter table if exists auction
    add constraint auction_to_court_id foreign key (court_id) references court;

alter table if exists auction
    add constraint auction_to_source_id foreign key (source_id) references auction_sources;

alter table if exists auction_property_types
    add constraint auction_property_types_to_auction_id foreign key (auction_id) references auction;

alter table if exists court
    add constraint court_to_state_id foreign key (state_id) references states;

alter table if exists document
    add constraint document_to_auction_id foreign key (auction_id) references auction;

alter table if exists state_zip_codes
    add constraint state_zip_codes_to_state_id foreign key (state_id) references states;

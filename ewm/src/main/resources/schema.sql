DROP TABLE if EXISTS users, events, categories, compilations, compilations_events, requests, uni_ips CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email VARCHAR NOT NULL,
    name VARCHAR NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY not null,
    annotation VARCHAR NOT NULL,
    category BIGINT NOT NULL,
    confirmed_requests BIGINT,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description VARCHAR NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator BIGINT NOT NULL,
    location_lat FLOAT NOT NULL,
    location_long FLOAT NOT NULL,
    paid boolean NOT NULL,
    participant_limit BIGINT NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation boolean NOT NULL,
    state VARCHAR,
    title VARCHAR NOT NULL,
    views BIGINT,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_events_category FOREIGN KEY (category) REFERENCES categories(id),
    CONSTRAINT fk_events_initiator FOREIGN KEY (initiator) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR NOT NULL,
    pinned boolean NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations_events (
    compilations_id BIGINT REFERENCES compilations (id) ON DELETE CASCADE,
    events_id BIGINT REFERENCES events (id) ON DELETE CASCADE,
	PRIMARY KEY(compilations_id, events_id),
    CONSTRAINT compilations_id FOREIGN KEY (compilations_id) REFERENCES compilations(id),
    CONSTRAINT events_id FOREIGN KEY (events_id) REFERENCES events(id)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event BIGINT NOT NULL,
    requester BIGINT NOT NULL,
    status VARCHAR NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requests_event FOREIGN KEY (event) REFERENCES events(id),
    CONSTRAINT fk_requests_requester FOREIGN KEY (requester) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS uni_ips (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    ip_address VARCHAR NOT NULL,
    event BIGINT NOT NULL,
    CONSTRAINT pk_uni_ips PRIMARY KEY (id),
    CONSTRAINT fk_uni_ips_event FOREIGN KEY (event) REFERENCES events(id)
);
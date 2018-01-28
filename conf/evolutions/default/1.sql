# --- !Ups

CREATE TABLE email (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR
);

# --- !Downs

DROP TABLE IF EXISTS email;

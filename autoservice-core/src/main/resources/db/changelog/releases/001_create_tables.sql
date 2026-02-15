-- liquibase formatted sql

-- changeset denis:1
CREATE TABLE IF NOT EXISTS garage_spot (
    id BIGSERIAL PRIMARY KEY,
    size DOUBLE PRECISION NOT NULL,
    has_lift BOOLEAN NOT NULL,
    has_pit BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS master (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    salary NUMERIC(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    description TEXT,
    master_id BIGINT NOT NULL,
    garage_spot_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    order_status VARCHAR(20) NOT NULL,
    price NUMERIC(10,2),
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_master FOREIGN KEY (master_id) REFERENCES master(id),
    CONSTRAINT fk_garage_spot FOREIGN KEY (garage_spot_id) REFERENCES garage_spot(id)
);
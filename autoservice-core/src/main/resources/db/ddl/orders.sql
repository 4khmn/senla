CREATE TABLE orders (
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
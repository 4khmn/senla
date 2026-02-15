-- liquibase formatted sql

-- changeset denis:2
INSERT INTO garage_spot (size, has_lift, has_pit) VALUES
    (20.5, true, false),
    (18.0, false, true);

INSERT INTO master (name, salary) VALUES
    ('Ivan Petrov', 50000.00),
    ('Alex Smirnov', 60000.00);

INSERT INTO orders (
    description,
    master_id,
    garage_spot_id,
    start_time,
    end_time,
    order_status,
    price,
    created_at
) VALUES
    (
        'Engine repair',
        1,
        1,
        '2026-01-02 10:00:00',
        '2026-01-02 14:00:00',
        'OPEN',
        15000.00,
        NOW()
    );
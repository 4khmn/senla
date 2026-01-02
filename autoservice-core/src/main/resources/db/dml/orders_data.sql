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
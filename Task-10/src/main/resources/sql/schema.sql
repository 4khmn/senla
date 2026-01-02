CREATE TABLE IF NOT EXISTS Product(
    maker VARCHAR(10) NOT NULL,
    model VARCHAR(50) PRIMARY KEY,
    type VARCHAR(50) CHECK (type IN ('PC','Laptop','Printer'))
);

CREATE TABLE IF NOT EXISTS PC(
    code SERIAL PRIMARY KEY,
    model VARCHAR(50) NOT NULL REFERENCES Product(model),
    speed SMALLINT NOT NULL,
    ram SMALLINT NOT NULL,
    hd REAL NOT NULL,
    cd VARCHAR(10) NOT NULL,
    price NUMERIC(10,2)
);

CREATE TABLE IF NOT EXISTS Laptop(
    code SERIAL PRIMARY KEY,
    model VARCHAR(50) NOT NULL REFERENCES Product(model),
    speed SMALLINT NOT NULL,
    ram SMALLINT NOT NULL,
    hd REAL NOT NULL,
    price NUMERIC(10,2),
    screen SMALLINT NOT NULL
);

CREATE TABLE IF NOT EXISTS Printer(
    code SERIAL PRIMARY KEY,
    model VARCHAR(50) NOT NULL REFERENCES Product(model),
    color CHAR(1) CHECK (color IN ('y','n')),
    type VARCHAR(10) CHECK (type IN ('Laser','Jet','Matrix')),
    price NUMERIC(10,2)
);

INSERT INTO Product (maker, model, type) VALUES
-- PC
('A', 'PC1', 'PC'),
('A', 'PC2', 'PC'),
('A', 'PC3', 'PC'),
('B', 'PC4', 'PC'),
('C', 'PC5', 'PC'),
('D', 'PC6', 'PC'),

-- Laptop
('A', 'L1', 'Laptop'),
('B', 'L2', 'Laptop'),
('C', 'L3', 'Laptop'),

-- Printer
('A', 'P1', 'Printer'),
('B', 'P2', 'Printer'),
('C', 'P3', 'Printer');

INSERT INTO PC (model, speed, ram, hd, cd, price) VALUES
('PC1', 400, 64, 10, '12x', 450),
('PC2', 750, 64, 20, '24x', 600),
('PC3', 750, 64, 20, '24x', 650),
('PC4', 800, 128, 40, '12x', 700),
('PC5', 900, 256, 80, '48x', 1200),
('PC6', 900, 256, 40, '24x', 1000);

INSERT INTO Laptop (model, speed, ram, hd, price, screen) VALUES
('L1', 750, 128, 100, 1100, 15),
('L2', 800, 256, 120, 1500, 17),
('L3', 300, 64, 80, 900, 14);

INSERT INTO Printer (model, color, type, price) VALUES
('P1', 'y', 'Laser', 200),
('P2', 'y', 'Jet', 150),
('P3', 'n', 'Matrix', 300);
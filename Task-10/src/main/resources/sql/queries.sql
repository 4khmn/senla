--1
SELECT code, speed, hd
    FROM PC
    WHERE price<500;
--2
SELECT DISTINCT maker
    FROM Product
    WHERE type='Printer';
--3
SELECT model, ram, screen
    FROM Laptop
    WHERE price>1000;
--4
SELECT *
    FROM Printer
    WHERE color='y';
--5
SELECT code, cd, hd
    FROM PC
    WHERE cd IN ('12x', '24x') and price < 600;
--6
SELECT p.maker, l.speed
    FROM Product AS p
    JOIN Laptop AS l ON p.model=l.model
    WHERE l.hd>=100;
--7
SELECT p.model, pc.price
FROM Product p
         JOIN PC pc ON pc.model = p.model
WHERE p.maker = 'B'

UNION ALL

SELECT p.model, l.price
FROM Product p
         JOIN Laptop l ON l.model = p.model
WHERE p.maker = 'B'

UNION ALL

SELECT p.model, pr.price
FROM Product p
         JOIN Printer pr ON pr.model = p.model
WHERE p.maker = 'B';
--8
SELECT DISTINCT maker
    FROM Product
    GROUP BY maker
    HAVING COUNT(*) FILTER (WHERE type = 'PC') > 0
    AND COUNT(*) FILTER (WHERE type = 'Laptop') = 0;
--9
SELECT DISTINCT maker
    FROM Product
    JOIN PC ON PC.model=Product.model
    WHERE speed >= 450;
--10
SELECT model, price
    FROM Printer
    WHERE price = (
        SELECT MAX(price)
            FROM Printer
    );
--11
SELECT AVG(speed)
    FROM PC;
--12
SELECT AVG(speed)
    FROM Laptop
    WHERE price > 1000;
--13
SELECT AVG(speed)
    FROM PC
    JOIN Product ON Product.model=PC.model
    WHERE Product.maker='A';
--14
SELECT speed, AVG(price)
    FROM PC
    GROUP BY speed;
--15
SELECT DISTINCT hd
    FROM PC
    GROUP BY hd
    HAVING COUNT(*)>=2;
--16
SELECT p1.model,
       p2.model,
       p1.speed,
       p1.ram
    FROM PC AS p1
    JOIN PC AS p2 ON p1.speed = p2.speed
    AND p1.ram=p2.ram
    AND p1.model > p2.model;

--17
SELECT 'Laptop', Laptop.model, Laptop.speed
    FROM Laptop
    WHERE speed < (
        SELECT MIN(speed)
            FROM PC
        );

--18
SELECT maker, price
    FROM Printer
    JOIN Product AS p ON p.model=Printer.model
    WHERE price = (
        SELECT MIN(price)
            FROM Printer
            WHERE color='y'
        )
    AND color='y';
--19
SELECT p.maker, AVG(l.screen)
    FROM Laptop AS l
    JOIN Product AS p ON p.model=l.model
    GROUP BY p.maker;
--20
SELECT DISTINCT maker, COUNT(*)
    FROM Product
    WHERE type = 'PC'
    GROUP BY maker
    HAVING (count(*) >=3);
--21
SELECT p.maker, MAX(PC.price)
    FROM Product AS p
    JOIN PC ON PC.model=p.model
    GROUP BY p.maker;
--22
SELECT speed, AVG(price)
    FROM PC
    WHERE speed > 600
    GROUP BY speed;
--23
SELECT DISTINCT maker
    FROM Product
    JOIN Laptop ON Laptop.model=Product.model
    WHERE Laptop.speed>=750
UNION
SELECT DISTINCT maker
    FROM Product
    JOIN PC ON PC.model=Product.model
    WHERE PC.speed>=750
--24
SELECT model
    FROM PC
    WHERE price = (
        SELECT MAX(price)
            FROM (
                SELECT price FROM PC
                UNION ALL
                SELECT price FROM Laptop
                UNION ALL
                SELECT price FROM Printer
         ) AS t
    )
UNION
SELECT model
    FROM Laptop
    WHERE price = (
    SELECT MAX(price)
        FROM (
             SELECT price FROM PC
             UNION ALL
             SELECT price FROM Laptop
             UNION ALL
             SELECT price FROM Printer
         ) AS t
    )
UNION
SELECT model
    FROM Printer
    WHERE price = (
    SELECT MAX(price)
        FROM (
             SELECT price FROM PC
             UNION ALL
             SELECT price FROM Laptop
             UNION ALL
             SELECT price FROM Printer
         ) AS t
    );
--25
SELECT DISTINCT p.maker
    FROM Product AS p
    JOIN PC ON p.model = PC.model
    WHERE PC.ram = (
        SELECT MIN(ram)
            FROM PC
        )
    AND PC.speed = (
        SELECT MAX(speed)
            FROM PC
            WHERE ram = (
                SELECT MIN(ram)
                    FROM PC
            )
        )
    AND EXISTS(
        SELECT 1
            FROM Product p2
            WHERE p2.maker = p.maker
            AND p2.type = 'Printer'
    );
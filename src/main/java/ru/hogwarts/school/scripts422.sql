-- Создание таблицы "Машина"
CREATE TABLE IF NOT EXISTS car (
    id BIGSERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    price DECIMAL(12, 2) NOT NULL
);

-- Создание таблицы "Человек"
CREATE TABLE IF NOT EXISTS person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER CHECK (age >= 0 AND age <= 150),
    has_driver_license BOOLEAN DEFAULT FALSE,
    car_id BIGINT,
    FOREIGN KEY (car_id) REFERENCES car(id) ON DELETE SET NULL
);

INSERT INTO car (brand, model, price) VALUES
('Toyota', 'Supra', 4500000.00),
('Honda', 'Civic', 1800000.00),
('BMW', 'X5', 5500000.00);

INSERT INTO person (name, age, has_driver_license, car_id) VALUES
('Артём Катушенко ', 30, TRUE, 1),
('Лев Скальдов', 25, TRUE, 1),
('Степан Полынь', 20, FALSE, NULL);

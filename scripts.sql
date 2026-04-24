-- 1. Получить всех студентов, возраст которых находится между 10 и 20
SELECT * FROM student WHERE age BETWEEN 10 AND 20;

-- 2. Получить всех студентов, но отобразить только список их имен
SELECT name FROM student;

-- 3. Получить всех студентов, у которых в имени присутствует буква "О" (русская)
SELECT * FROM student WHERE name LIKE '%О%';

-- 4. Получить всех студентов, у которых возраст меньше идентификатора
SELECT * FROM student WHERE age < id;

-- 5. Получить всех студентов упорядоченных по возрасту
SELECT * FROM student ORDER BY age;

-- 6. Получить количество всех студентов
SELECT COUNT(*) FROM student;

-- 7. Получить средний возраст студентов
SELECT AVG(age) FROM student;

-- 8. Получить последних 5 студентов (по ID)
SELECT * FROM student ORDER BY id DESC LIMIT 5;

-- 9. Получить студентов определенного факультета
SELECT * FROM student WHERE faculty_id = 1;

-- 10. Получить количество студентов на каждом факультете
SELECT f.name, COUNT(s.id)
FROM faculty f
LEFT JOIN student s ON f.id = s.faculty_id
GROUP BY f.id, f.name;
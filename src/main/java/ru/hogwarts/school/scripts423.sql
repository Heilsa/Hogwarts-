
-- ЗАПРОС 1: Все студенты с названиями факультетов
SELECT
    s.name AS student_name,
    s.age AS student_age,
    f.name AS faculty_name
FROM student s
LEFT JOIN faculty f ON s.faculty_id = f.id
ORDER BY s.name;

-- ЗАПРОС 2: Только студенты, у которых есть аватарки
SELECT
    s.id,
    s.name AS student_name,
    s.age AS student_age,
    f.name AS faculty_name,
    a.file_path AS avatar_path
FROM student s
INNER JOIN faculty f ON s.faculty_id = f.id
INNER JOIN avatar a ON s.id = a.student_id
ORDER BY s.name;

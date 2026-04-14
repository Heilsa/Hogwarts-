-- 1. Возраст студента не может быть меньше 16 лет.
ALTER TABLE student
ADD CONSTRAINT check_student_age CHECK (age >= 16);

-- 2. Имена студентов должны быть уникальными и не равны нулю
ALTER TABLE student
ADD CONSTRAINT unique_student_name UNIQUE (name);

ALTER TABLE student
ALTER COLUMN name SET NOT NULL;

-- 3. Пара "название" - "цвет факультета" должна быть уникальной
ALTER TABLE faculty
ADD CONSTRAINT unique_faculty_name_color UNIQUE (name, color);

-- 4. При создании студента без возраста автоматически присваивается 20 лет
ALTER TABLE student
ALTER COLUMN age SET DEFAULT 20;
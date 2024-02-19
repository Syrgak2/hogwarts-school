alter table student
add constraint check_age check (age >= 16),
alter column age set default 20,
add constraint unique_name unique(name),
alter column name set not null;


alter table faculty
add constraint unique_faculty_color unique (name, color);
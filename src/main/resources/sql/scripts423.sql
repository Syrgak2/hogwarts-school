select student."name" , student.age, faculty."name" from student
full join faculty on student.faculty_id = faculty.id;

select student."name" , student.age, faculty."name" from student
full join faculty on student.faculty_id = faculty.id
inner join student_avatar on student.id = student_avatar.student_id ;


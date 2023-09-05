SET lc_messages = 'ja_JP.UTF-8';

CREATE TABLE IF NOT EXISTS members
(
   	member_id SERIAL PRIMARY KEY,
   	username varchar(25) not null,
   	email varchar(25) not null unique,
	password varchar(255) not null,
	phone_number varchar(10) not null,
	role varchar(255) not null
);

INSERT INTO members (username, email, password, phone_number, role) 
VALUES 
	('Phan Thanh Dung', 'thanhdung@gmail.com', crypt('12345678', gen_salt('bf', 10)), '0987654321', 'ROLE_EDIT'),
	('Tran Caov Vy', 'caovy@gmail.com', crypt('12345678', gen_salt('bf', 10)), '0123498765', 'ROLE_ADMIN'),
	('Dao Khac Nhien', 'dknhien@gmail.com', crypt('12345678', gen_salt('bf', 10)), '3256507861', 'ROLE_VIEW'),
	('John Smith', 'johnsmith@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555555', 'ROLE_VIEW'),
    ('Mary Johnson', 'maryjohnson@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555556', 'ROLE_VIEW'),
    ('James Brown', 'jamesbrown@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555557', 'ROLE_VIEW'),
    ('Michael Davis', 'michaeldavis@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555558', 'ROLE_VIEW'),
    ('Jennifer Wilson', 'jenniferwilson@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555559', 'ROLE_VIEW'),
    ('William Taylor', 'williamtaylor@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555560', 'ROLE_VIEW'),
    ('Linda Anderson', 'lindaanderson@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555561', 'ROLE_VIEW'),
    ('Robert Lee', 'robertlee@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555562', 'ROLE_VIEW'),
    ('Karen Hall', 'karenhall@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555563', 'ROLE_VIEW'),
    ('David Clark', 'davidclark@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555564', 'ROLE_VIEW'),
    ('Susan Turner', 'susanturner@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555565', 'ROLE_VIEW'),
    ('Richard Harris', 'richardharris@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555566', 'ROLE_VIEW'),
    ('Nancy Moore', 'nancymoore@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555567', 'ROLE_VIEW'),
    ('Charles Martin', 'charlesmartin@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555568', 'ROLE_VIEW'),
    ('Margaret Hill', 'margarethill@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555569', 'ROLE_VIEW'),
    ('Joseph King', 'josephking@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555570', 'ROLE_VIEW'),
    ('Dorothy Baker', 'dorothybaker@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555571', 'ROLE_VIEW'),
    ('Thomas Wright', 'thomaswright@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555572', 'ROLE_VIEW'),
    ('Patricia Green', 'patriciagreen@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555573', 'ROLE_VIEW'),
    ('Daniel Adams', 'danieladams@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555574', 'ROLE_VIEW'),
    ('Betty Turner', 'bettyturner@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555575', 'ROLE_VIEW'),
    ('Paul Walker', 'paulwalker@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555576', 'ROLE_VIEW'),
    ('Sarah Scott', 'sarahscott@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555577', 'ROLE_VIEW'),
    ('George White', 'georgewhite@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555578', 'ROLE_VIEW'),
    ('Karen Lewis', 'karenlewis@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555579', 'ROLE_VIEW'),
    ('Edward Anderson', 'edwardanderson@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555580', 'ROLE_VIEW'),
    ('Lisa Rodriguez', 'luisrodriguez@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555581', 'ROLE_VIEW'),
    ('William Moore', 'williammoore@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555582', 'ROLE_VIEW'),
    ('Donna Hall', 'donnahall@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555583', 'ROLE_VIEW'),
    ('Kenneth Turner', 'kennethturner@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555584', 'ROLE_VIEW'),
    ('Carol Scott', 'carolscott@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555585', 'ROLE_VIEW'),
    ('Michael Taylor', 'michaeltaylor@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555586', 'ROLE_VIEW'),
    ('Sandra Wright', 'sandrawright@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555587', 'ROLE_VIEW'),
    ('David Johnson', 'davidjohnson@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555588', 'ROLE_VIEW'),
    ('Deborah Turner', 'deborahturner@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555589', 'ROLE_VIEW'),
    ('Charles Harris', 'charlesharris@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555590', 'ROLE_VIEW'),
    ('Linda Baker', 'lindabaker@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555591', 'ROLE_VIEW'),
    ('Joseph Adams', 'josephadams@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555592', 'ROLE_VIEW'),
    ('Karen Turner', 'karenturner@gmail.com', crypt('12345678', gen_salt('bf', 10)), '5555555593', 'ROLE_VIEW');

SELECT member_id, username, email, password, phone_number, role FROM members
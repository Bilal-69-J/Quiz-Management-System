CREATE DATABASE IF NOT EXISTS quizdb;
USE quizdb;

CREATE TABLE IF NOT EXISTS admin (
  admin_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS participant (
  participant_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100),
  email VARCHAR(100),
  contact VARCHAR(20),
  password VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS quiz (
  quiz_id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(100),
  category VARCHAR(50),
  time_limit INT,
  passing_marks INT,
  status VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS question (
  question_id INT AUTO_INCREMENT PRIMARY KEY,
  quiz_id INT,
  question_text TEXT,
  option_a VARCHAR(255),
  option_b VARCHAR(255),
  option_c VARCHAR(255),
  option_d VARCHAR(255),
  correct_option VARCHAR(1),
  FOREIGN KEY (quiz_id) REFERENCES quiz(quiz_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_attempt (
  attempt_id INT AUTO_INCREMENT PRIMARY KEY,
  participant_id INT,
  quiz_id INT,
  score INT,
  percentage DOUBLE,
  status VARCHAR(20),
  attempt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (participant_id) REFERENCES participant(participant_id) ON DELETE CASCADE,
  FOREIGN KEY (quiz_id) REFERENCES quiz(quiz_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS attempt_answers (
  attempt_id INT,
  question_id INT,
  selected_option VARCHAR(1),
  FOREIGN KEY (attempt_id) REFERENCES quiz_attempt(attempt_id) ON DELETE CASCADE,
  FOREIGN KEY (question_id) REFERENCES question(question_id) ON DELETE CASCADE
);

INSERT INTO admin (username, password)
VALUES ('admin', 'admin')
ON DUPLICATE KEY UPDATE username = username;

INSERT INTO quiz (title, category, time_limit, passing_marks, status) VALUES
('General Knowledge', 'General', 300, 5, 'ACTIVE'),
('Computer Basics', 'Technology', 300, 5, 'ACTIVE'),
('SQL Basics', 'Database', 300, 5, 'ACTIVE'),
('Mathematics', 'Math', 300, 5, 'ACTIVE'),
('Science', 'Science', 300, 5, 'ACTIVE')
ON DUPLICATE KEY UPDATE title = title;

INSERT INTO question (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES
((SELECT quiz_id FROM quiz WHERE title='General Knowledge' LIMIT 1),'Capital of France?','Paris','Rome','Berlin','Madrid','A'),
((SELECT quiz_id FROM quiz WHERE title='General Knowledge' LIMIT 1),'Largest ocean?','Atlantic','Indian','Arctic','Pacific','D'),
((SELECT quiz_id FROM quiz WHERE title='General Knowledge' LIMIT 1),'National animal of India?','Lion','Tiger','Elephant','Leopard','B'),
((SELECT quiz_id FROM quiz WHERE title='General Knowledge' LIMIT 1),'Tallest mountain?','K2','Everest','Kilimanjaro','Denali','B'),
((SELECT quiz_id FROM quiz WHERE title='General Knowledge' LIMIT 1),'Currency of Japan?','Yuan','Won','Yen','Dollar','C'),
((SELECT quiz_id FROM quiz WHERE title='General Knowledge' LIMIT 1),'Smallest continent?','Europe','Australia','Antarctica','Africa','B'),
((SELECT quiz_id FROM quiz WHERE title='General Knowledge' LIMIT 1),'Father of computers?','Newton','Einstein','Charles Babbage','Turing','C'),
((SELECT quiz_id FROM quiz WHERE title='General Knowledge' LIMIT 1),'Fastest land animal?','Lion','Tiger','Cheetah','Horse','C'),
((SELECT quiz_id FROM quiz WHERE title='General Knowledge' LIMIT 1),'Gas used in balloons?','Oxygen','Hydrogen','Nitrogen','Carbon','B'),
((SELECT quiz_id FROM quiz WHERE title='General Knowledge' LIMIT 1),'Largest desert?','Sahara','Gobi','Kalahari','Thar','A')
ON DUPLICATE KEY UPDATE question_text = question_text;

INSERT INTO question VALUES
(NULL,(SELECT quiz_id FROM quiz WHERE title='Computer Basics' LIMIT 1),'Full form of CPU?','Central Process Unit','Central Processing Unit','Control Process Unit','Computer Process Unit','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Computer Basics' LIMIT 1),'Brain of computer?','RAM','Hard Disk','CPU','Monitor','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Computer Basics' LIMIT 1),'Which is input device?','Printer','Monitor','Keyboard','Speaker','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Computer Basics' LIMIT 1),'Which is output device?','Mouse','Keyboard','Monitor','Scanner','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Computer Basics' LIMIT 1),'Binary system uses?','0 & 1','1 & 2','2 & 3','0 & 2','A'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Computer Basics' LIMIT 1),'Primary memory?','Hard Disk','Pen Drive','RAM','DVD','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Computer Basics' LIMIT 1),'Permanent memory?','RAM','Cache','ROM','Register','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Computer Basics' LIMIT 1),'OS example?','Windows','MS Word','Chrome','Excel','A'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Computer Basics' LIMIT 1),'Shortcut for copy?','Ctrl+X','Ctrl+C','Ctrl+V','Ctrl+Z','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Computer Basics' LIMIT 1),'Unit of speed?','Byte','Hertz','Pixel','Second','B')
ON DUPLICATE KEY UPDATE question_text = question_text;

INSERT INTO question VALUES
(NULL,(SELECT quiz_id FROM quiz WHERE title='SQL Basics' LIMIT 1),'Retrieve data command?','INSERT','SELECT','UPDATE','DELETE','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='SQL Basics' LIMIT 1),'Remove table command?','DROP','DELETE','TRUNCATE','REMOVE','A'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='SQL Basics' LIMIT 1),'Unique identifier?','Index','Primary Key','Foreign Key','Constraint','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='SQL Basics' LIMIT 1),'Add new record?','SELECT','INSERT','UPDATE','ALTER','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='SQL Basics' LIMIT 1),'Update existing record?','INSERT','ALTER','UPDATE','DROP','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='SQL Basics' LIMIT 1),'Delete all rows fast?','DELETE','DROP','TRUNCATE','REMOVE','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='SQL Basics' LIMIT 1),'Join tables using?','UNION','JOIN','MERGE','LINK','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='SQL Basics' LIMIT 1),'Sort results?','GROUP BY','ORDER BY','WHERE','HAVING','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='SQL Basics' LIMIT 1),'Filter rows?','WHERE','ORDER','GROUP','SORT','A'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='SQL Basics' LIMIT 1),'Default sort order?','Random','Descending','Ascending','None','C')
ON DUPLICATE KEY UPDATE question_text = question_text;

INSERT INTO question VALUES
(NULL,(SELECT quiz_id FROM quiz WHERE title='Mathematics' LIMIT 1),'10 + 20 = ?','20','30','40','50','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Mathematics' LIMIT 1),'15 × 2 = ?','20','25','30','35','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Mathematics' LIMIT 1),'Square root of 81?','7','8','9','10','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Mathematics' LIMIT 1),'50 ÷ 5 = ?','5','10','15','20','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Mathematics' LIMIT 1),'Value of π?','3.12','3.14','3.16','3.18','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Mathematics' LIMIT 1),'Triangle has how many sides?','2','3','4','5','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Mathematics' LIMIT 1),'100 − 45 = ?','45','55','65','75','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Mathematics' LIMIT 1),'Even number?','3','7','9','8','D'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Mathematics' LIMIT 1),'Cube of 3?','9','18','27','36','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Mathematics' LIMIT 1),'Perimeter of square formula?','4a','a²','2a','a³','A')
ON DUPLICATE KEY UPDATE question_text = question_text;

INSERT INTO question VALUES
(NULL,(SELECT quiz_id FROM quiz WHERE title='Science' LIMIT 1),'Boiling point of water?','50°C','100°C','0°C','150°C','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Science' LIMIT 1),'Gas for breathing?','CO₂','Oxygen','Hydrogen','Nitrogen','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Science' LIMIT 1),'Nearest planet to sun?','Earth','Venus','Mercury','Mars','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Science' LIMIT 1),'Human heart chambers?','2','3','4','5','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Science' LIMIT 1),'Force unit?','Joule','Newton','Watt','Pascal','B'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Science' LIMIT 1),'Photosynthesis occurs in?','Root','Stem','Leaf','Flower','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Science' LIMIT 1),'Largest organ?','Heart','Brain','Skin','Liver','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Science' LIMIT 1),'Blood color?','Blue','Green','Red','Yellow','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Science' LIMIT 1),'Energy unit?','Volt','Ampere','Joule','Ohm','C'),
(NULL,(SELECT quiz_id FROM quiz WHERE title='Science' LIMIT 1),'Sun is a?','Planet','Star','Comet','Asteroid','B')
ON DUPLICATE KEY UPDATE question_text = question_text;

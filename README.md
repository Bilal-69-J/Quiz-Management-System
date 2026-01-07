🎓 Quiz Competition & Management System

A JavaFX-based desktop application developed as a lab project for
Object Oriented Programming (CSC241) at
COMSATS University Islamabad, Abbottabad Campus.

The system allows administrators to manage quizzes and questions, while students can register, log in, attempt quizzes, and view results and leaderboards. The project demonstrates core Object-Oriented Programming principles, JavaFX GUI development, and database integration using JDBC.

📌 Project Overview

The Quiz Competition & Management System is a learning-oriented desktop application designed to automate quiz management in an academic environment.
It provides a structured workflow for quiz creation, participation, evaluation, and result tracking.

This project follows a clean architecture using:

Model classes for domain entities

DAO (Data Access Object) layer for database operations

JavaFX UI layer for user interaction

🚀 Features
👨‍🏫 Admin

Admin login

Create, update, and delete quizzes

Manage quiz questions

View quiz results

View leaderboard

👨‍🎓 Student

Student registration and login

View available quizzes

Attempt timed quizzes

Automatic scoring and percentage calculation

View attempt history and leaderboard

🛠️ Technologies Used
Technology	Purpose
Java (JDK 8+)	Core programming language
JavaFX	Graphical User Interface
MySQL	Database
JDBC	Database connectivity
Git & GitHub	Version control
🗂️ Project Structure
src/main/java/com/quiz
├── MainApp.java
├── AppNavigator.java
│
├── model
│   ├── Admin.java
│   ├── Participant.java
│   ├── Quiz.java
│   ├── Question.java
│   ├── QuizAttempt.java
│   └── AttemptAnswer.java
│
├── dao
│   ├── DBConnection.java
│   ├── AdminDAO.java
│   ├── ParticipantDAO.java
│   ├── QuizDAO.java
│   ├── QuestionDAO.java
│   └── QuizAttemptDAO.java
│
└── ui
    ├── UIUtil.java
    ├── WelcomeScreen.java
    ├── LoginScreen.java
    ├── StudentLoginScreen.java
    ├── DashboardScreen.java
    ├── StudentDashboard.java
    ├── QuizAttemptScreen.java
    └── Other UI Screens

📐 UML Diagrams

All UML diagrams are available in the /uml folder.

📌 Class Diagram

📌 Use Case Diagram

📌 Sequence Diagram

🖼️ Screenshots

Application screenshots are stored in the /screenshots folder.

Welcome Screen

Admin Dashboard

Quiz Attempt Screen

Leaderboard

📄 Documentation
🎯 Objectives

Automate quiz creation and evaluation

Reduce manual checking and result calculation

Provide real-time feedback to students

Demonstrate OOP concepts using Java

📦 Scope

Desktop-based Java application

Admin and Student roles

Local MySQL database

⚠ Limitations

Passwords are stored in plain text

Desktop-only application

No advanced authentication or encryption

Limited scalability (lab-level project)

🧾 Installation & Setup
🔹 Prerequisites

Java JDK 8 or higher

MySQL Server

IntelliJ IDEA / Eclipse

Git

🔹 Steps

Clone Repository

git clone https://github.com/Bilal-69-J/Quiz-Management-System.git


Create Database

Create a MySQL database

Import the provided SQL file (Quix.sql)

Configure Database
Create db.properties in src/main/resources:

db.url=jdbc:mysql://localhost:3306/quiz_db
db.user=root
db.password=your_password


Run Application

Run MainApp.java from your IDE

🔄 Git & GitHub Usage

This project uses Git & GitHub for version control.

✔ Public GitHub repository
✔ Frequent commits with meaningful messages
✔ Separate contributions by team members
✔ README with UML, screenshots, and documentation

Commit examples:

git commit -m "Add core model classes"
git commit -m "Implement DAO layer using JDBC"
git commit -m "Add JavaFX UI screens"
git commit -m "Add UML diagrams and README documentation"

🎤 Final Presentation (CSC241)

During the final presentation, the following will be demonstrated:

Project introduction and problem statement

Object decomposition and UML diagrams

OOP concepts used (Encapsulation, Aggregation, DAO pattern)

Live demo of the application

Challenges faced and lessons learned

🔮 Future Enhancements

Password hashing and encryption

Web-based version using Spring Boot

Export results to PDF/CSV

Improved UI and error handling

Role-based access control

👨‍💻 Contributors

Bilal-69-J — Project design, implementation, and documentation

(Additional contributors can be added here if applicable)

📄 License

This project is developed for academic purposes as part of the
CSC241 Object Oriented Programming Lab.

```markdown
# Quiz Competition & Management System (Java OOP + JavaFX + MySQL)

This is a full Java implementation of a Quiz Competition & Management System using:
- Java 11+ (works with 8+ in most code paths)
- JavaFX (no CSS, no FXML)
- JDBC (MySQL)
- MVC-style separation (models, DAOs, UI screens)
- Minimalist professional UI using JavaFX nodes only
- Student portal with plain-text passwords (per request) — WARNING: insecure for production

What’s included
- Admin portal: login, manage quizzes, questions, participants, start quiz, view results, leaderboard.
- Student portal: register/login (email+password), list active quizzes, start quiz with timer, view attempts.
- DAOs: AdminDAO, ParticipantDAO, QuizDAO, QuestionDAO, QuizAttemptDAO (all using PreparedStatement)
- AppNavigator helper for safe screen switching
- UI helper (UIUtil) for consistent minimalistic styling
- SQL schema with sample data: db/schema.sql

Quick start
1. Install JDK (11+ recommended), Maven and MySQL.
2. Create database and tables:
   - mysql -u root -p < db/schema.sql
3. Edit `src/main/resources/db.properties` with your MySQL connection details.
4. Build:
   - mvn clean package
5. Run:
   - mvn javafx:run
   - or run `com.quiz.MainApp` from your IDE.

Notes & next steps
- All SQL uses PreparedStatement. DAOs attempt to close resources correctly.
- Student passwords are stored in plain text per your request. I strongly recommend switching to PBKDF2 hashing before publishing or sharing the app.
- If you want I can:
  - convert passwords to hashed storage,
  - add more UI polish (icons, animations) while staying within constraints,
  - create sample unit tests,
  - or prepare exact git commands/patch to add these files to your repo.

If you want the git commands to commit these into branch feature/student-portal and open a PR, tell me and I’ll provide them.
```
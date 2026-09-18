# JDBC and SQL

## Goal

By the end of this lesson, you will write basic SQL (SELECT FROM WHERE INSERT UPDATE DELETE), explain the JDBC stack, and sketch a Java check that a user row exists after an API create.

## Why It Matters

UI says "user created." Database has no row. That is a bug. API returns 201 with an id the DB does not know. That is a bug. SDETs who only click cannot see this layer.

Interviews: "How do you validate a signup?" A strong answer includes API + DB, not only a welcome page.

## Real-Life Analogy

A warehouse.

```text
Store website (application)
Warehouse clerk (API/service)
Clipboard (SQL)
Shelves (tables/rows)
```

SELECT is walking the aisle and reading labels. INSERT is putting a box on a shelf. UPDATE is changing a label. DELETE is removing a box. WHERE is "only the aisle named users, box id 42."

JDBC is the official radio protocol between Java and the warehouse. The **driver** is the radio that speaks MySQL vs Postgres vs Oracle.

## Illustrated Explanation

Where data lives:

```text
Application
 ↓
API
 ↓
Database
```

How Java reaches it:

```text
Java
 ↓
JDBC
 ↓
Database Driver
 ↓
Database
```

SQL verbs:

```text
SELECT   read rows
FROM     which table
WHERE    filter
INSERT   add row
UPDATE   change row
DELETE   remove row
```

```text
users
┌────┬──────────┬──────────┐
│ id │ username │ email    │
├────┼──────────┼──────────┤
│ 1  │ aisha    │ a@x.com  │
│ 2  │ ben      │ b@x.com  │
└────┴──────────┴──────────┘
```

```sql
SELECT username FROM users WHERE id = 1;
```

Returns `aisha`.

JDBC flow:

```text
DriverManager.getConnection(url, user, password)
    ↓
Connection
    ↓
PreparedStatement  (never string-plus SQL with user input)
    ↓
ResultSet          (for SELECT)
    ↓
close resources    (try-with-resources)
```

## Syntax / Concept

SQL (training table `users`):

```sql
SELECT id, username, email
FROM users
WHERE username = 'aisha';
```

```sql
INSERT INTO users (username, email)
VALUES ('chen', 'c@x.com');
```

```sql
UPDATE users
SET email = 'new@x.com'
WHERE username = 'chen';
```

```sql
DELETE FROM users
WHERE username = 'chen';
```

In tests, prefer deleting *your* rows (unique usernames) so you do not wipe a shared QA database.

JDBC (shape; URL depends on vendor):

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {

    public boolean exists(String username) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/training";
        String sql = "SELECT 1 FROM users WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(url, "app", "from-config-not-source");
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
```

`?` placeholders: **PreparedStatement**. If you concatenate username into SQL, you invite SQL injection and broken quotes.

Passwords: environment or config, not `pom.xml`, not Git.

Maven driver example (PostgreSQL):

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.4</version>
</dependency>
```

MySQL uses `mysql-connector-j`. The JDBC *ideas* stay the same.

## Simple Example

```java
public class SqlExamples {
    public static void main(String[] args) {
        String select = "SELECT id FROM users WHERE username = ?";
        String insert = "INSERT INTO users (username, email) VALUES (?, ?)";
        String update = "UPDATE users SET email = ? WHERE username = ?";
        String delete = "DELETE FROM users WHERE username = ?";
        System.out.println(select);
        System.out.println(insert);
        System.out.println(update);
        System.out.println(delete);
    }
}
```

Typing SQL in Java strings is how many tests start. Later, keep SQL in one DAO class.

## Real-World Example

Banking: after `POST /transfers`, SELECT balance FROM accounts WHERE id = ?. Assert the cents. UI can round; the ledger is the truth.

E-commerce: after placing an order, SELECT status FROM orders WHERE id = ?. Expect `NEW` or `PAID` per contract.

## SDET Example

```text
API POST /users  →  201
JDBC exists(username)  →  true
```

If API 201 and DB false: API lied or committed elsewhere. Excellent bug.

```java
public class CreateUserDbIT {

    @Test
    void createdUserShouldExistInDatabase() throws Exception {
        String username = UserFactory.uniqueUsername("qa");
        userApi.create(username, "User");
        assertTrue(userDao.exists(username));
    }
}
```

Cleanup: DELETE your user in `@AfterMethod` so QA is not full of `qa1736...` forever.

## Break the Code

```java
String sql = "SELECT * FROM users WHERE username = '" + username + "'";
```

If username is `x' OR '1'='1`, you have a disaster. Tests should model production safety: always prepare.

```java
connection.close(); // skipped when exception thrown before this line
```

Use try-with-resources.

```sql
DELETE FROM users;
```

In a shared QA DB, this is a career-limiting statement. Scope DELETE with WHERE and your unique prefix.

## Debug

| Symptom | Check |
| --- | --- |
| `No suitable driver` | driver JAR missing in Maven |
| `password authentication failed` | config, not SQL |
| empty ResultSet | WHERE too strict; data not committed; wrong schema |
| lock / hang | uncommitted transaction; close connection |
| flaky exists() | API async; you queried too soon |

Read SQL in a GUI (DBeaver) first. Then put the same SQL in Java.

## Student Exercise

On paper, write SQL to: insert user `dina`, select her email, update email, delete her. Then write the JDBC stack diagram from memory.

## Challenge

Implement `UserDao.exists` against a local Docker Postgres *if* you have it. If not, write the class with a comment and a unit test that uses a fake `UserDirectory` (SOLID D) so you still practice the assertion shape.

## Knowledge Check

1. Draw Application → API → Database.
2. Draw Java → JDBC → Driver → Database.
3. Name the five SQL verbs.
4. What does WHERE do?
5. Why PreparedStatement?
6. Why try-with-resources?
7. Why unique usernames in tests?
8. Should DB passwords be in Git?
9. What bug does API 201 + missing row reveal?
10. Why not `DELETE FROM users` in QA?

## Interview Question

**Question:** How do you use a database in automation?

A strong answer:

> The application talks to an API which talks to a database. I validate critical flows at the data layer with SQL: SELECT FROM WHERE, and I clean up with scoped DELETE. In Java I use JDBC: DriverManager, PreparedStatement, ResultSet, closed with try-with-resources. I never concatenate user input into SQL. I never commit passwords. After POST /users I assert the row exists. UI alone is not enough for ledger and persistence bugs.

## Homework

Install nothing reckless on a work machine. Write `UserDao` as a compile-able class even if the URL is fake. Add `.gitignore` for `secrets.properties`. Document the JDBC diagram in notes.

---

## Answer Key

1. Application → API → Database
2. Java → JDBC → Driver → Database
3. SELECT INSERT UPDATE DELETE (FROM/WHERE are clauses; verbs: those four plus SELECT)
4. Filters rows
5. Safety and correct escaping; no injection
6. Connections close on success and failure
7. Avoid collisions; easy cleanup
8. No
9. Persistence bug or wrong data source
10. It deletes everyone else's data

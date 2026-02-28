# JDBC - Java Database Connectivity

## JDBC Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Java Application                          │
│                         │                                    │
│                    ┌────▼────┐                               │
│                    │ JDBC API │  (java.sql, javax.sql)       │
│                    └────┬────┘                               │
│                         │                                    │
│               ┌─────────▼──────────┐                        │
│               │  JDBC Driver Manager│                        │
│               └─────────┬──────────┘                        │
│                         │                                    │
│         ┌───────────────┼───────────────┐                   │
│         │               │               │                   │
│    ┌────▼────┐    ┌─────▼────┐   ┌─────▼─────┐            │
│    │ MySQL   │    │PostgreSQL│   │  Oracle   │             │
│    │ Driver  │    │ Driver   │   │  Driver   │             │
│    └────┬────┘    └─────┬────┘   └─────┬─────┘            │
│         │               │               │                   │
│    ┌────▼────┐    ┌─────▼────┐   ┌─────▼─────┐            │
│    │ MySQL DB│    │PostgreSQL│   │ Oracle DB │             │
│    └─────────┘    └──────────┘   └───────────┘            │
└─────────────────────────────────────────────────────────────┘
```

---

## JDBC Steps (6 Steps)

```java
// Step 1: Load driver (optional in Java 6+ — auto-loaded)
Class.forName("com.mysql.cj.jdbc.Driver");

// Step 2: Establish connection
Connection conn = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/mydb",  // URL
    "username",                           // User
    "password"                            // Password
);

// Step 3: Create statement
Statement stmt = conn.createStatement();

// Step 4: Execute query
ResultSet rs = stmt.executeQuery("SELECT * FROM users");

// Step 5: Process results
while (rs.next()) {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    System.out.println(id + ": " + name);
}

// Step 6: Close resources
rs.close();
stmt.close();
conn.close();
```

### Modern Way (try-with-resources)
```java
String url = "jdbc:mysql://localhost:3306/mydb";

try (Connection conn = DriverManager.getConnection(url, "user", "pass");
     PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE age > ?")) {
    
    stmt.setInt(1, 18);
    
    try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            System.out.println(rs.getString("name"));
        }
    }
}  // All resources auto-closed
```

---

## Statement vs PreparedStatement vs CallableStatement

### Statement (Avoid — SQL injection risk)
```java
// ❌ Vulnerable to SQL injection
Statement stmt = conn.createStatement();
String name = "'; DROP TABLE users; --";
ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE name = '" + name + "'");
// Executes: SELECT * FROM users WHERE name = ''; DROP TABLE users; --'
```

### PreparedStatement (ALWAYS use this)
```java
// ✅ Parameterized — prevents SQL injection
PreparedStatement pstmt = conn.prepareStatement(
    "SELECT * FROM users WHERE name = ? AND age > ?"
);
pstmt.setString(1, "John");  // Parameter 1
pstmt.setInt(2, 18);         // Parameter 2

ResultSet rs = pstmt.executeQuery();

// Insert
PreparedStatement insert = conn.prepareStatement(
    "INSERT INTO users (name, email, age) VALUES (?, ?, ?)"
);
insert.setString(1, "John");
insert.setString(2, "john@email.com");
insert.setInt(3, 30);
int rowsAffected = insert.executeUpdate();  // Returns row count

// Update
PreparedStatement update = conn.prepareStatement(
    "UPDATE users SET email = ? WHERE id = ?"
);
update.setString(1, "new@email.com");
update.setInt(2, 1);
update.executeUpdate();

// Delete
PreparedStatement delete = conn.prepareStatement("DELETE FROM users WHERE id = ?");
delete.setInt(1, 1);
delete.executeUpdate();
```

### CallableStatement (Stored Procedures)
```java
CallableStatement cs = conn.prepareCall("{call getUser(?, ?)}");
cs.setInt(1, 1);                        // Input parameter
cs.registerOutParameter(2, Types.VARCHAR); // Output parameter
cs.execute();
String name = cs.getString(2);           // Get output
```

### Comparison

| Feature | Statement | PreparedStatement | CallableStatement |
|---------|-----------|-------------------|-------------------|
| SQL Injection | Vulnerable | Safe (parameterized) | Safe |
| Performance | Compiled each time | Pre-compiled, cached | Pre-compiled |
| Use case | Simple static SQL | Dynamic queries | Stored procedures |
| Batch | Limited | ✅ Efficient | ✅ |

---

## Transactions

```java
Connection conn = DriverManager.getConnection(url, user, pass);

try {
    conn.setAutoCommit(false);  // Start transaction
    
    PreparedStatement debit = conn.prepareStatement(
        "UPDATE accounts SET balance = balance - ? WHERE id = ?"
    );
    debit.setDouble(1, 1000);
    debit.setInt(2, 1);
    debit.executeUpdate();
    
    PreparedStatement credit = conn.prepareStatement(
        "UPDATE accounts SET balance = balance + ? WHERE id = ?"
    );
    credit.setDouble(1, 1000);
    credit.setInt(2, 2);
    credit.executeUpdate();
    
    conn.commit();  // ✅ Both succeed — commit
    
} catch (SQLException e) {
    conn.rollback();  // ❌ Any failure — rollback both
    throw e;
} finally {
    conn.setAutoCommit(true);  // Reset
    conn.close();
}
```

### Transaction Isolation Levels
```java
conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED); // Dirty reads
conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);   // No dirty reads
conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);  // No non-repeatable reads
conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);     // Full isolation
```

| Level | Dirty Read | Non-Repeatable Read | Phantom Read |
|-------|-----------|--------------------|--------------| 
| READ_UNCOMMITTED | ✅ | ✅ | ✅ |
| READ_COMMITTED | ❌ | ✅ | ✅ |
| REPEATABLE_READ | ❌ | ❌ | ✅ |
| SERIALIZABLE | ❌ | ❌ | ❌ |

---

## Batch Processing

```java
PreparedStatement pstmt = conn.prepareStatement(
    "INSERT INTO users (name, email) VALUES (?, ?)"
);

conn.setAutoCommit(false);

for (User user : users) {
    pstmt.setString(1, user.getName());
    pstmt.setString(2, user.getEmail());
    pstmt.addBatch();        // Add to batch
}

int[] results = pstmt.executeBatch();  // Execute all at once
conn.commit();
// Much faster than executing individual inserts
```

---

## Connection Pooling

```java
// Without pooling: Creating new connection every time (SLOW)
Connection conn = DriverManager.getConnection(url, user, pass);  // ~200ms each!

// With pooling: Reuse pre-created connections (FAST)
// HikariCP (fastest, most popular)
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
config.setUsername("user");
config.setPassword("pass");
config.setMaximumPoolSize(10);            // Max 10 connections
config.setMinimumIdle(5);                 // Keep 5 idle
config.setConnectionTimeout(30000);       // 30 seconds timeout

HikariDataSource ds = new HikariDataSource(config);

// Get connection from pool (fast — reuses existing)
try (Connection conn = ds.getConnection()) {
    // Use connection
}  // Returns to pool (not closed)
```

### Why Connection Pooling?
- Creating DB connection is expensive (~100-200ms)
- Pool keeps connections open and ready
- Limits max connections (prevents DB overload)
- Spring Boot uses HikariCP by default

---

## ResultSet Types

```java
// Forward-only (default) — can only move forward
ResultSet rs = stmt.executeQuery(sql);

// Scrollable — can move forward and backward
Statement stmt = conn.createStatement(
    ResultSet.TYPE_SCROLL_INSENSITIVE,  // or TYPE_SCROLL_SENSITIVE
    ResultSet.CONCUR_READ_ONLY          // or CONCUR_UPDATABLE
);

ResultSet rs = stmt.executeQuery(sql);
rs.first();          // Move to first row
rs.last();           // Move to last row
rs.absolute(5);      // Move to row 5
rs.relative(-2);     // Move 2 rows back
rs.previous();       // Move to previous row
```

---

## Interview Q&A

**Q: What is JDBC?**
A: Java API for connecting to relational databases. Provides interfaces (Connection, Statement, ResultSet) implemented by database drivers.

**Q: Why use PreparedStatement over Statement?**
A: Prevents SQL injection (parameterized queries), better performance (pre-compiled, cached), cleaner code.

**Q: What is SQL injection?**
A: Malicious SQL in user input that alters query behavior. PreparedStatement prevents it by separating SQL from data.

**Q: What is connection pooling?**
A: Maintaining pre-created database connections for reuse. Avoids expensive connection creation overhead. HikariCP is most popular.

**Q: What is a transaction?**
A: Group of SQL operations that succeed or fail together (ACID). Use `setAutoCommit(false)`, `commit()`, `rollback()`.

**Q: What are ACID properties?**
A: Atomicity (all or nothing), Consistency (valid state), Isolation (concurrent transactions don't interfere), Durability (committed data persists).

**Q: executeQuery vs executeUpdate vs execute?**
A: `executeQuery()` for SELECT (returns ResultSet). `executeUpdate()` for INSERT/UPDATE/DELETE (returns row count). `execute()` for any SQL (returns boolean).

**Q: What is DriverManager vs DataSource?**
A: DriverManager creates new connections each time (simple). DataSource supports connection pooling (production use).

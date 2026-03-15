# Database & Caching Quick Reference

## 🔥 Most Asked Topics

| Topic | Frequency | Key Points |
|-------|-----------|------------|
| SQL Joins | ⭐⭐⭐⭐⭐ | INNER, LEFT, RIGHT, FULL OUTER |
| Indexing | ⭐⭐⭐⭐⭐ | B-tree, composite, covering indexes |
| Transactions | ⭐⭐⭐⭐⭐ | ACID, isolation levels, deadlocks |
| Query Optimization | ⭐⭐⭐⭐ | Explain plans, N+1 problem |
| NoSQL Types | ⭐⭐⭐⭐ | Document, key-value, column-family, graph |
| Caching Patterns | ⭐⭐⭐⭐⭐ | Cache-aside, write-through, write-behind |
| Redis | ⭐⭐⭐⭐ | Data structures, persistence, use cases |

---

## 📊 Comparison Tables

### SQL vs NoSQL

| Aspect | SQL | NoSQL |
|--------|-----|-------|
| Schema | Fixed schema | Flexible schema |
| Scaling | Vertical (scale up) | Horizontal (scale out) |
| Transactions | ACID guaranteed | Eventually consistent (usually) |
| Joins | Supported | Limited or none |
| Use Case | Complex queries, relationships | High scale, flexible data |

### NoSQL Database Types

| Type | Example | Data Model | Use Case |
|------|---------|------------|----------|
| Document | MongoDB | JSON documents | Flexible schemas, nested data |
| Key-Value | Redis, DynamoDB | Key-value pairs | Caching, session storage |
| Column-Family | Cassandra | Wide columns | Time-series, high write throughput |
| Graph | Neo4j | Nodes and edges | Social networks, recommendations |

### Transaction Isolation Levels

| Level | Dirty Read | Non-Repeatable Read | Phantom Read |
|-------|------------|---------------------|--------------|
| Read Uncommitted | Yes | Yes | Yes |
| Read Committed | No | Yes | Yes |
| Repeatable Read | No | No | Yes |
| Serializable | No | No | No |

### Caching Patterns

| Pattern | Write Flow | Read Flow | Use Case |
|---------|-----------|-----------|----------|
| Cache-Aside | Write to DB only | Read cache, then DB | Most common |
| Write-Through | Write to cache and DB | Read from cache | Read-heavy |
| Write-Behind | Write to cache, async to DB | Read from cache | Write-heavy |
| Read-Through | Write to DB | Cache loads from DB | Transparent caching |

---

## 🎯 Checklist

### SQL Fundamentals
- [ ] Write complex SELECT queries
- [ ] Use INNER, LEFT, RIGHT joins
- [ ] Understand GROUP BY and HAVING
- [ ] Use window functions
- [ ] Write subqueries

### Indexing
- [ ] Create B-tree indexes
- [ ] Use composite indexes
- [ ] Understand covering indexes
- [ ] Know when NOT to index
- [ ] Analyze index usage

### Transactions
- [ ] Understand ACID properties
- [ ] Know isolation levels
- [ ] Handle deadlocks
- [ ] Use optimistic locking
- [ ] Implement pessimistic locking

### Query Optimization
- [ ] Read EXPLAIN plans
- [ ] Solve N+1 query problem
- [ ] Optimize JOIN queries
- [ ] Use query hints
- [ ] Monitor slow queries

### NoSQL
- [ ] Choose right NoSQL type
- [ ] Understand CAP theorem
- [ ] Design document schemas
- [ ] Handle eventual consistency
- [ ] Implement sharding

### Caching
- [ ] Implement cache-aside pattern
- [ ] Set appropriate TTL
- [ ] Handle cache invalidation
- [ ] Use Redis data structures
- [ ] Monitor cache hit ratio

---

## ⚡ Code Snippets

### SQL Join Examples
```sql
-- INNER JOIN: Only matching rows
SELECT o.id, o.total, c.name
FROM orders o
INNER JOIN customers c ON o.customer_id = c.id;

-- LEFT JOIN: All from left, matching from right
SELECT c.name, o.id
FROM customers c
LEFT JOIN orders o ON c.id = o.customer_id;
```

### Creating Indexes
```sql
-- Single column index
CREATE INDEX idx_email ON users(email);

-- Composite index
CREATE INDEX idx_name_age ON users(last_name, first_name, age);

-- Covering index
CREATE INDEX idx_user_orders ON orders(user_id, order_date, total);
```

### Transaction Example
```sql
BEGIN TRANSACTION;

UPDATE accounts SET balance = balance - 100 WHERE id = 1;
UPDATE accounts SET balance = balance + 100 WHERE id = 2;

COMMIT;
-- Or ROLLBACK if error
```

### Spring Data JPA with Caching
```java
@Service
public class UserService {
    
    @Cacheable(value = "users", key = "#id")
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found"));
    }
    
    @CachePut(value = "users", key = "#user.id")
    public User update(User user) {
        return userRepository.save(user);
    }
    
    @CacheEvict(value = "users", key = "#id")
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
```

### Redis Cache-Aside Pattern
```java
@Service
public class ProductService {
    
    @Autowired
    private RedisTemplate<String, Product> redisTemplate;
    
    @Autowired
    private ProductRepository productRepository;
    
    public Product getProduct(Long id) {
        String key = "product:" + id;
        
        // Try cache first
        Product product = redisTemplate.opsForValue().get(key);
        
        if (product == null) {
            // Cache miss - load from database
            product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
            
            // Store in cache with TTL
            redisTemplate.opsForValue().set(key, product, 1, TimeUnit.HOURS);
        }
        
        return product;
    }
}
```

### HikariCP Configuration
```properties
# HikariCP connection pool settings
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

---

## 📝 Common Interview Questions

1. **Q**: Difference between INNER JOIN and LEFT JOIN?
   **A**: INNER returns only matching rows; LEFT returns all left rows plus matches

2. **Q**: What is the N+1 query problem?
   **A**: Loading parent entities, then separate query for each child; use JOIN FETCH

3. **Q**: Explain ACID properties
   **A**: Atomicity (all or nothing), Consistency (valid state), Isolation (concurrent), Durability (persisted)

4. **Q**: When to use NoSQL over SQL?
   **A**: High scale, flexible schema, simple queries, horizontal scaling needed

5. **Q**: What is cache invalidation?
   **A**: Removing stale data from cache; hardest problem in computer science

6. **Q**: Difference between Redis and Memcached?
   **A**: Redis has data structures, persistence, pub/sub; Memcached is simpler key-value

7. **Q**: What is a covering index?
   **A**: Index containing all columns needed for query; no table lookup required

---

## 🔧 Essential Redis Commands

```bash
# String operations
SET key value
GET key
SETEX key 3600 value  # Set with TTL

# Hash operations
HSET user:1 name "John"
HGET user:1 name
HGETALL user:1

# List operations
LPUSH mylist "item1"
RPUSH mylist "item2"
LRANGE mylist 0 -1

# Set operations
SADD myset "member1"
SMEMBERS myset

# Sorted set operations
ZADD leaderboard 100 "player1"
ZRANGE leaderboard 0 -1 WITHSCORES

# TTL operations
EXPIRE key 3600
TTL key
```

---

*Quick reference for last-minute review*
*PostgreSQL 15+ | MongoDB 6+ | Redis 7+*

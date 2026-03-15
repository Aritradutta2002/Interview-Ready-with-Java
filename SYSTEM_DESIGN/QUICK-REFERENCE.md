# System Design Quick Reference

## 🔥 Most Asked Topics

| Topic | Frequency | Key Points |
|-------|-----------|------------|
| CAP Theorem | ⭐⭐⭐⭐⭐ | Consistency, Availability, Partition tolerance |
| Load Balancing | ⭐⭐⭐⭐⭐ | Round-robin, least connections, health checks |
| Caching | ⭐⭐⭐⭐⭐ | Multi-level, invalidation, CDN |
| Database Scaling | ⭐⭐⭐⭐⭐ | Replication, sharding, read replicas |
| Microservices | ⭐⭐⭐⭐ | Service boundaries, communication, data |
| Message Queues | ⭐⭐⭐⭐ | Kafka, RabbitMQ, delivery guarantees |
| API Design | ⭐⭐⭐⭐ | REST, versioning, rate limiting |

---

## 📊 Comparison Tables

### CAP Theorem Trade-offs

| System | Consistency | Availability | Partition Tolerance | Example |
|--------|-------------|--------------|---------------------|---------|
| CA | ✅ | ✅ | ❌ | Traditional RDBMS (single node) |
| CP | ✅ | ❌ | ✅ | MongoDB, HBase, Redis |
| AP | ❌ | ✅ | ✅ | Cassandra, DynamoDB, Couchbase |

### Load Balancing Algorithms

| Algorithm | Description | Use Case | Session Affinity |
|-----------|-------------|----------|------------------|
| Round Robin | Distribute sequentially | Equal capacity servers | No |
| Least Connections | Send to server with fewest connections | Varying request times | No |
| IP Hash | Hash client IP to server | Session persistence | Yes |
| Weighted Round Robin | Distribute based on weights | Different capacity servers | No |

### Database Scaling Strategies

| Strategy | Description | Pros | Cons |
|----------|-------------|------|------|
| Replication | Copy data to multiple servers | Read scaling, HA | Write bottleneck |
| Sharding | Partition data across servers | Write scaling | Complex queries |
| Read Replicas | Separate read and write DBs | Read scaling | Replication lag |
| Vertical Scaling | Bigger server | Simple | Limited, expensive |

### Microservices Communication

| Pattern | Sync/Async | Use Case | Pros | Cons |
|---------|------------|----------|------|------|
| REST API | Sync | Simple CRUD | Easy, standard | Tight coupling |
| gRPC | Sync | High performance | Fast, typed | Complex |
| Message Queue | Async | Decoupling | Loose coupling | Eventual consistency |
| Event Bus | Async | Event-driven | Scalable | Complex debugging |

---

## 🎯 Checklist

### Design Fundamentals
- [ ] Understand CAP theorem
- [ ] Know scalability concepts
- [ ] Understand availability vs reliability
- [ ] Know performance metrics
- [ ] Consider security from start

### Scalability Patterns
- [ ] Implement load balancing
- [ ] Use multi-level caching
- [ ] Design for horizontal scaling
- [ ] Implement database sharding
- [ ] Use CDN for static content

### Microservices
- [ ] Define service boundaries
- [ ] Choose communication pattern
- [ ] Handle distributed transactions
- [ ] Implement service discovery
- [ ] Use API gateway

### Event-Driven
- [ ] Choose message broker
- [ ] Design event schemas
- [ ] Handle idempotency
- [ ] Implement dead letter queues
- [ ] Monitor message lag

### Common Designs
- [ ] Design URL shortener
- [ ] Design rate limiter
- [ ] Design distributed cache
- [ ] Design notification system
- [ ] Design chat application

---

## ⚡ System Design Template

### 1. Requirements Gathering
```
Functional Requirements:
- What features are needed?
- What are the core use cases?

Non-Functional Requirements:
- Scale: How many users? Requests per second?
- Performance: Latency requirements?
- Availability: Uptime requirements?
- Consistency: Strong or eventual?
```

### 2. Capacity Estimation
```
Traffic Estimates:
- Daily Active Users (DAU)
- Requests per second (RPS)
- Peak traffic multiplier

Storage Estimates:
- Data per user/request
- Total storage needed
- Growth rate

Bandwidth Estimates:
- Incoming data rate
- Outgoing data rate
```

### 3. API Design
```
REST API Example:
POST   /api/v1/users          - Create user
GET    /api/v1/users/{id}     - Get user
PUT    /api/v1/users/{id}     - Update user
DELETE /api/v1/users/{id}     - Delete user
GET    /api/v1/users          - List users
```

### 4. Database Schema
```
Users Table:
- id (PK)
- username
- email
- created_at

Posts Table:
- id (PK)
- user_id (FK)
- content
- created_at
```

### 5. High-Level Architecture
```
Client → Load Balancer → API Gateway → Services
                              ↓
                         Message Queue
                              ↓
                    [Service 1] [Service 2]
                         ↓           ↓
                    Database     Cache (Redis)
```

---

## 📝 Common Interview Questions

1. **Q**: Explain CAP theorem
   **A**: Can't have all three: Consistency, Availability, Partition tolerance; choose two

2. **Q**: How to scale a database?
   **A**: Replication (read scaling), sharding (write scaling), caching, read replicas

3. **Q**: Difference between horizontal and vertical scaling?
   **A**: Horizontal adds more machines; vertical adds more power to existing machine

4. **Q**: How to handle distributed transactions?
   **A**: Saga pattern, 2PC, eventual consistency, idempotent operations

5. **Q**: What is eventual consistency?
   **A**: System becomes consistent over time; not immediately after write

6. **Q**: How to design a rate limiter?
   **A**: Token bucket, leaky bucket, fixed window, sliding window algorithms

7. **Q**: What is circuit breaker pattern?
   **A**: Prevents cascading failures; opens circuit after threshold failures

---

## 🔧 Back-of-Envelope Calculations

### Common Numbers
```
1 million = 10^6
1 billion = 10^9
1 KB = 10^3 bytes
1 MB = 10^6 bytes
1 GB = 10^9 bytes

1 day = 86,400 seconds ≈ 100,000 seconds
1 million requests/day ≈ 12 requests/second
```

### Latency Numbers
```
L1 cache reference: 0.5 ns
L2 cache reference: 7 ns
RAM reference: 100 ns
SSD random read: 150 μs
Network within datacenter: 0.5 ms
Disk seek: 10 ms
Network cross-continent: 150 ms
```

### Estimation Example
```
Problem: 100M DAU, each user posts 2 times/day

Write RPS:
100M users × 2 posts/day = 200M posts/day
200M / 100,000 seconds = 2,000 writes/second

Storage (5 years):
200M posts/day × 365 days × 5 years = 365B posts
Assume 1KB per post = 365TB
```

---

*Quick reference for last-minute review*
*Focus: Backend Systems | Distributed Systems | Scalability*

# Docker for Java Backend Quick Reference

## 🔥 Most Asked Topics

| Topic | Frequency | Key Points |
|-------|-----------|------------|
| Dockerfile for Java | ⭐⭐⭐⭐⭐ | Base image, COPY, ENTRYPOINT, multi-stage |
| JVM Memory Settings | ⭐⭐⭐⭐⭐ | -Xmx, -Xms, container memory limits |
| Multi-Stage Builds | ⭐⭐⭐⭐ | Build stage, runtime stage, image size reduction |
| Layer Caching | ⭐⭐⭐⭐ | Dependency layer, code layer, build optimization |
| Docker Compose | ⭐⭐⭐ | Multi-container apps, networking, volumes |

---

## 📊 Comparison Tables

### Base Images for Java

| Image | Size | Use Case | Pros | Cons |
|-------|------|----------|------|------|
| openjdk:17 | ~470MB | Development | Full JDK, debugging tools | Large size |
| eclipse-temurin:17-jre | ~270MB | Production | Smaller, JRE only | No compilation tools |
| eclipse-temurin:17-jre-alpine | ~170MB | Production | Smallest size | Alpine compatibility issues |
| amazoncorretto:17 | ~400MB | AWS deployment | AWS optimized | Larger size |

### Container vs JVM Memory

| Aspect | Container Memory | JVM Heap |
|--------|------------------|----------|
| Setting | --memory flag | -Xmx flag |
| Includes | Heap + Non-heap + OS | Only heap memory |
| Recommendation | Set container 25-50% higher than heap | Set to 70-80% of container |

---

## 🎯 Checklist

### Docker Basics
- [ ] Understand images vs containers
- [ ] Know basic Docker commands
- [ ] Understand volumes and networking
- [ ] Know how to view logs

### Dockerfile for Java
- [ ] Choose appropriate base image
- [ ] Use multi-stage builds
- [ ] Optimize layer caching
- [ ] Set proper ENTRYPOINT

### JVM Configuration
- [ ] Set heap size (-Xmx, -Xms)
- [ ] Configure container memory limits
- [ ] Understand memory overhead
- [ ] Use appropriate GC settings

### Optimization
- [ ] Minimize image size
- [ ] Optimize build times
- [ ] Use .dockerignore
- [ ] Run as non-root user

### Docker Compose
- [ ] Define services
- [ ] Configure networking
- [ ] Set up volumes
- [ ] Use environment variables

---

## ⚡ Code Snippets

### Basic Dockerfile for Spring Boot
```dockerfile
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/myapp.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Multi-Stage Dockerfile
```dockerfile
# Build stage
FROM maven:3.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Optimized Dockerfile with JVM Settings
```dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/myapp.jar app.jar
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", \
    "-Xmx512m", \
    "-Xms256m", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=200", \
    "-jar", "app.jar"]
```

### Docker Compose for Java App + Database
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb
    depends_on:
      - db
    mem_limit: 1g
  
  db:
    image: postgres:15-alpine
    environment:
      - POSTGRES_DB=mydb
      - POSTGRES_PASSWORD=secret
    volumes:
      - db-data:/var/lib/postgresql/data

volumes:
  db-data:
```

### .dockerignore
```
target/
.git/
.idea/
*.iml
.DS_Store
README.md
```

---

## 📝 Common Interview Questions

1. **Q**: Why use multi-stage builds for Java?
   **A**: Separate build and runtime stages; smaller final image without build tools

2. **Q**: How to set JVM memory in containers?
   **A**: Use -Xmx for heap; set container memory 25-50% higher than heap

3. **Q**: How to optimize Docker build times?
   **A**: Order layers by change frequency; copy dependencies before code

4. **Q**: Difference between CMD and ENTRYPOINT?
   **A**: ENTRYPOINT is main command; CMD provides default arguments

5. **Q**: How to run Java app as non-root?
   **A**: Create user with RUN adduser, then USER username before ENTRYPOINT

---

## 🔧 Essential Docker Commands

```bash
# Build image
docker build -t myapp:1.0 .

# Run container
docker run -d -p 8080:8080 --name myapp myapp:1.0

# View logs
docker logs -f myapp

# Execute command in container
docker exec -it myapp sh

# Stop and remove
docker stop myapp && docker rm myapp

# Docker Compose
docker-compose up -d
docker-compose logs -f
docker-compose down
```

---

*Quick reference for last-minute review*
*Docker 20.x+ | Java 17+*

# Cloud & DevOps Quick Reference

## 🔥 Most Asked Topics

| Topic | Frequency | Key Points |
|-------|-----------|------------|
| AWS Core Services | ⭐⭐⭐⭐⭐ | EC2, S3, RDS, Lambda, API Gateway |
| Kubernetes Basics | ⭐⭐⭐⭐⭐ | Pods, Services, Deployments, ConfigMaps |
| CI/CD Pipelines | ⭐⭐⭐⭐ | Jenkins, GitHub Actions, deployment strategies |
| Monitoring | ⭐⭐⭐⭐ | Prometheus, Grafana, metrics, alerts |
| Logging | ⭐⭐⭐ | ELK Stack, centralized logging |
| Infrastructure as Code | ⭐⭐⭐ | Terraform, CloudFormation |

---

## 📊 Comparison Tables

### AWS Compute Services

| Service | Use Case | Pricing Model | Management |
|---------|----------|---------------|------------|
| EC2 | Full control VMs | Per hour/second | Self-managed |
| Lambda | Event-driven functions | Per invocation | Fully managed |
| ECS | Container orchestration | EC2 or Fargate pricing | AWS managed |
| EKS | Kubernetes on AWS | Control plane + nodes | Partially managed |

### Kubernetes Resources

| Resource | Purpose | Scope |
|----------|---------|-------|
| Pod | Smallest deployable unit | Namespace |
| Deployment | Manages replica sets | Namespace |
| Service | Network endpoint | Namespace |
| ConfigMap | Configuration data | Namespace |
| Secret | Sensitive data | Namespace |
| Ingress | External access | Namespace |

### Deployment Strategies

| Strategy | Downtime | Risk | Rollback Speed |
|----------|----------|------|----------------|
| Recreate | Yes | High | Fast |
| Rolling Update | No | Low | Medium |
| Blue-Green | No | Low | Instant |
| Canary | No | Very Low | Fast |

---

## 🎯 Checklist

### AWS Fundamentals
- [ ] Understand EC2 instance types
- [ ] Know S3 storage classes
- [ ] Understand RDS vs DynamoDB
- [ ] Know Lambda use cases
- [ ] Understand VPC and security groups

### Kubernetes
- [ ] Create and manage Pods
- [ ] Deploy applications with Deployments
- [ ] Expose services with Service resources
- [ ] Configure apps with ConfigMaps
- [ ] Manage secrets securely
- [ ] Understand Ingress for external access

### CI/CD
- [ ] Design pipeline stages
- [ ] Implement automated testing
- [ ] Configure deployment strategies
- [ ] Handle secrets in pipelines
- [ ] Implement rollback mechanisms

### Monitoring & Logging
- [ ] Set up Prometheus metrics
- [ ] Create Grafana dashboards
- [ ] Configure alerts
- [ ] Centralize logs with ELK
- [ ] Implement distributed tracing

---

## ⚡ Code Snippets

### Kubernetes Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: myapp
spec:
  replicas: 3
  selector:
    matchLabels:
      app: myapp
  template:
    metadata:
      labels:
        app: myapp
    spec:
      containers:
      - name: myapp
        image: myapp:1.0
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
```

### Kubernetes Service
```yaml
apiVersion: v1
kind: Service
metadata:
  name: myapp-service
spec:
  selector:
    app: myapp
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: LoadBalancer
```

### GitHub Actions Pipeline
```yaml
name: CI/CD Pipeline
on:
  push:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Build with Maven
      run: mvn clean package
    
    - name: Run tests
      run: mvn test
    
    - name: Build Docker image
      run: docker build -t myapp:${{ github.sha }} .
    
    - name: Push to registry
      run: docker push myapp:${{ github.sha }}
```

### Terraform AWS EC2
```hcl
resource "aws_instance" "app_server" {
  ami           = "ami-0c55b159cbfafe1f0"
  instance_type = "t3.medium"
  
  tags = {
    Name = "AppServer"
    Environment = "Production"
  }
  
  vpc_security_group_ids = [aws_security_group.app_sg.id]
}

resource "aws_security_group" "app_sg" {
  name = "app-security-group"
  
  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
```

---

## 📝 Common Interview Questions

1. **Q**: Difference between EC2 and Lambda?
   **A**: EC2 is always-on VMs; Lambda is event-driven serverless functions

2. **Q**: What is a Kubernetes Pod?
   **A**: Smallest deployable unit; one or more containers sharing network and storage

3. **Q**: Explain Blue-Green deployment
   **A**: Two identical environments; switch traffic from old (blue) to new (green)

4. **Q**: How does Prometheus collect metrics?
   **A**: Pull-based model; scrapes metrics from HTTP endpoints at intervals

5. **Q**: What is Infrastructure as Code?
   **A**: Managing infrastructure through code files (Terraform, CloudFormation)

---

## 🔧 Essential kubectl Commands

```bash
# Get resources
kubectl get pods
kubectl get deployments
kubectl get services

# Describe resource
kubectl describe pod <pod-name>

# View logs
kubectl logs <pod-name>
kubectl logs -f <pod-name>  # Follow logs

# Execute command in pod
kubectl exec -it <pod-name> -- /bin/sh

# Apply configuration
kubectl apply -f deployment.yaml

# Scale deployment
kubectl scale deployment myapp --replicas=5

# Delete resources
kubectl delete pod <pod-name>
kubectl delete -f deployment.yaml
```

---

*Quick reference for last-minute review*
*AWS | Kubernetes 1.28+ | Terraform 1.5+*

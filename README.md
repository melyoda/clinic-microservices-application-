# Clinic Management Microservices System

A distributed system built with Spring Boot microservices for clinic management.

## 🏗️ Architecture

The system consists of:
- **API Gateway**: Single entry point with Spring Cloud Gateway
- **Auth Service**: JWT-based authentication and user management
- **Patient Service**: Patient registration and medical records
- Each service has its own PostgreSQL database

## 🚀 Quick Start

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd clinic-microservices-project
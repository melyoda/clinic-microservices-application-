# 🏥 **Clinic Management Microservices System**

A **distributed microservices-based system** built using **Spring Boot**, **Spring Cloud Gateway**, **PostgreSQL**, and **RabbitMQ** — designed to modernize clinic and hospital management by enabling secure, modular, and scalable handling of patient, appointment, and treatment data.

---

## 🧩 **Architecture Overview**

The system follows a **Domain-Driven Design (DDD)** approach and is composed of independent Spring Boot microservices that communicate via **REST APIs** and **event-driven messaging**.

### **Core Services**

| Service | Description | Tech Stack |
|----------|--------------|------------|
| 🛡️ **Auth Service** | Handles user authentication, JWT token generation, and role-based access control. | Spring Boot, Spring Security, JWT, PostgreSQL |
| 👩‍⚕️ **Patient Service** | Manages patient registration, personal info, and medical history. | Spring Boot, JPA, WebClient, PostgreSQL |
| 📅 **Appointment Service** | Schedules appointments, updates status, and emits lifecycle events. | Spring Boot, JPA, RabbitMQ, WebClient, PostgreSQL |
| 🚪 **API Gateway** | Central entry point for routing and JWT validation. | Spring Cloud Gateway |
| 🐇 **RabbitMQ (Broker)** | Enables asynchronous event-driven communication between services. | RabbitMQ 3.12 Management |

Each service:
- Runs independently in its own Docker container  
- Has a **dedicated PostgreSQL database**  
- Communicates through the **API Gateway** or **RabbitMQ**

---

## ⚙️ **System Features**

✅ JWT-based authentication  
✅ Role-based access control (Doctor, Nurse, Admin)  
✅ Secure patient data management  
✅ Appointment scheduling and status tracking  
✅ RabbitMQ event publishing (`appointment.scheduled`, `appointment.cancelled`, etc.)  
📋 Future: Treatment and Reporting microservices  

---

## 🚀 **Quick Start**

### **1. Clone the repository**
```bash
git clone https://github.com/<your-username>/clinic-microservices-system.git
cd clinic-microservices-system

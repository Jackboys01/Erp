# ERP System

## Overview

The ERP System is a comprehensive Spring Boot application designed to manage various organizational operations, including employee management, department management, task assignment, and salary payments. The system features robust authentication and authorization mechanisms, dynamic filtering, and search capabilities.

## Features

- **Employee Management**
  - Create, read, update, and delete employee records.
  - Assign tasks to employees and manage their departments.
- **Department Management**
  - Create, read, update, and delete departments.
- **Task Management**
  - Assign tasks to employees with deadlines and descriptions.
- **Salary Management**
  - Add salary payments for employees.
- **Filtering and Search**
  - Dynamic filtering and search capabilities for employees based on name, email, role, and department.
- **Security**
  - JWT-based authentication for secure access.
- **Documentation**
  - API documentation using Swagger.
- **Testing**
  - Comprehensive unit tests using Mockito.
- **Deployment**
  - Deployed on AWS EC2 with database management using AWS RDS.
  - CI/CD pipeline setup using Jenkins and Docker images on ECR.

## Technology Stack

- **Backend**: Spring Boot
- **Database**: MySQL (AWS RDS)
- **Security**: JWT, Spring Security
- **Testing**: Mockito
- **Deployment**: AWS EC2, Jenkins, Docker, AWS ECR
- **Documentation**: Swagger

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven
- MySQL
- Docker (for containerization)
- AWS account (for deployment)

### Installation

1. **Clone the repository**
   ```sh
   git clone https://github.com/your-github/ERP-System.git
   cd ERP-System

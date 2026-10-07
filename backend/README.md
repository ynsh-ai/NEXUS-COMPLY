# Backend

This is the backend component of **NEXUS-COMPLY**.

- Java / Spring Boot application
- MongoDB as the data store
- Spring Security with JWT / RBAC
- Configuration files under src/main/resources

## How to run
`ash
cd backend
mvn spring-boot:run
`

## Environment variables
- MONGODB_URI – MongoDB connection string
- JWT_SECRET – secret for signing JWT tokens
- SERVER_PORT – port for the backend (default 8080)

## Project structure
`
backend/
+-- src/
¦   +-- main/
¦   ¦   +-- java/com/nexuscomply/...   # Java source
¦   ¦   +-- resources/                # application.yml, etc.
¦   +-- test/                        # Test sources
+-- pom.xml                         # Maven build file
+-- README.md                       # This file
`

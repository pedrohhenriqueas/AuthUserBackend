# User Spring JWT 🔒

This project is a Spring Boot-based CRUD application that includes JWT authentication and authorization.

## Features ✨

- **Authentication and Authorization**: Implemented using JSON Web Tokens (JWT).
- **Role-based Access Control**: Supports two roles:
  - `ROLE_USER`
  - `ROLE_ADMIN`
- **CRUD Operations**: Allows managing users with secure role assignments.
- **Endpoints**:
  - `/api/auth/**`: For authentication and user management.
  - `/api/test/**`: For role-based test content.
- **Secure Password Storage**: Uses `BCryptPasswordEncoder`.

## Technologies Used 🛠️

- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Token)**
- **Hibernate & JPA**
- **PostgreSQL**

## Prerequisites 📋

- Java 17 or higher
- Maven
- PostgreSQL

## Getting Started 🛠️

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/CrudJavaJwt.git
   cd CrudJavaJwt
   ```

2. Check out the master branch (default branch):

   ```bash
   git checkout master
   ```

3. Configure the database:

   - Update the `application.properties` file with your PostgreSQL configuration:

     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/testdb
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     spring.jpa.hibernate.ddl-auto=update
     ```

4. Build the project:

   ```bash
   mvn clean install
   ```

5. Run the application:

   ```bash
   mvn spring-boot:run
   ```

## API Endpoints 🌐

### Authentication Endpoints (`/api/auth`)

- **Sign Up**: `POST /api/auth/signup`
- **Log In**: `POST /api/auth/signin`

### Test Endpoints (`/api/test`)

- **Public Content**: `GET /api/test/all`
- **User Content** (Requires `ROLE_USER` or `ROLE_ADMIN`): `GET /api/test/user`
- **Admin Board** (Requires `ROLE_ADMIN`): `GET /api/test/admin`

## Directory Structure 📂

```
CrudJavaJwt/
├── src/main/java
│   ├── com/example/CrudJavaJwt
│   │   ├── controller
│   │   ├── model
│   │   ├── payload
│   │   │   ├── request
│   │   │   └── response
│   │   ├── repository
│   │   ├── security
│   │   │   ├── jwt
│   │   │   ├── services
│   │   └── CrudJavaJwtApplication.java
├── src/main/resources
│   ├── application.properties
│   └── static/
├── pom.xml
└── README.md
```

## Default Roles 🛡️

To initialize the roles (`ROLE_USER` and `ROLE_ADMIN`), ensure they are inserted into the database before running the application.

Example SQL:

```sql
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
```

## Contributing 🤝

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Author

Created by **Pedro Santos**. Contributions are welcome!

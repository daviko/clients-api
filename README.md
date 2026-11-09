# TestApplication

A Spring Boot REST API for managing clients, backed by Microsoft SQL Server and documented with Swagger UI (OpenAPI).

## Technology stack

- Java 17
- Spring Boot 3.0.6
- Maven
- Spring Web
- Spring Data JDBC and Spring Data JPA
- Microsoft SQL Server JDBC driver
- Springdoc OpenAPI / Swagger UI

## Prerequisites

Install or have access to the following:

1. **JDK 17** (not just a JRE).
2. **Maven 3.6.3 or later**, unless you use the Maven Wrapper included in the project.
3. **Microsoft SQL Server** with the application's database and required tables already created.
4. Network access from your computer to the configured SQL Server host and port.

Check your Java and Maven installations:

```bash
java -version
mvn -version
```

The Java version should report 17.

## Project configuration

The application listens on port `8080` and uses the application name `TestApplication`.

Its main configuration is normally located at:

```text
src/main/resources/application.properties
```

### Configure the database connection

Do not commit database passwords or other credentials to source control. Configure the connection using environment variables instead.

Example `application.properties`:

```properties
server.port=8080
spring.application.name=TestApplication

spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Set the environment variables for your environment:

| Variable | Description |
|---|---|
| `DB_URL` | JDBC URL for the SQL Server database |
| `DB_USERNAME` | SQL Server login |
| `DB_PASSWORD` | Password for that login |

For example, the JDBC URL has this general format:

```text
jdbc:sqlserver://HOST\\INSTANCE:PORT;databaseName=DATABASE;schema=dbo;encrypt=true;trustServerCertificate=true;
```

Replace the host, instance, port, and database with the values supplied by your database administrator. Confirm whether your SQL Server connection uses a named instance, a fixed TCP port, or both; the JDBC URL must match the server's network configuration.

`trustServerCertificate=true` encrypts the connection but does not validate the server certificate. Use the certificate-validation configuration required by your organization's security policy in production.

#### Set environment variables

**Windows PowerShell** (for the current terminal session):

```powershell
$env:DB_URL = "jdbc:sqlserver://HOST\\INSTANCE:PORT;databaseName=DATABASE;schema=dbo;encrypt=true;trustServerCertificate=true;"
$env:DB_USERNAME = "your_database_username"
$env:DB_PASSWORD = "your_database_password"
```

**Windows Command Prompt** (for the current terminal session):

```bat
set DB_URL=jdbc:sqlserver://HOST\INSTANCE:PORT;databaseName=DATABASE;schema=dbo;encrypt=true;trustServerCertificate=true;
set DB_USERNAME=your_database_username
set DB_PASSWORD=your_database_password
```

**macOS / Linux**:

```bash
export DB_URL='jdbc:sqlserver://HOST\\INSTANCE:PORT;databaseName=DATABASE;schema=dbo;encrypt=true;trustServerCertificate=true;'
export DB_USERNAME='your_database_username'
export DB_PASSWORD='your_database_password'
```

Keep these values out of Git. If credentials have ever been committed or shared unintentionally, ask the database administrator to rotate them.

## Build and run locally

Run these commands from the directory containing `pom.xml`.

### 1. Download dependencies and compile

```bash
mvn clean install
```

To compile without running tests:

```bash
mvn clean package -DskipTests
```

### 2. Start the application

Using Maven:

```bash
mvn spring-boot:run
```

Or run the packaged JAR after building:

```bash
java -jar target/test-0.0.1-SNAPSHOT.jar
```

If startup succeeds, Spring Boot should report that the application has started and is listening on port `8080`.

### 3. Stop the application

Press `Ctrl+C` in the terminal where the application is running.

## Swagger UI and OpenAPI documentation

Once the application is running, open:

- Swagger UI: <http://localhost:8080/swagger-ui.html>
- OpenAPI JSON: <http://localhost:8080/v3/api-docs>
- OpenAPI YAML: <http://localhost:8080/v3/api-docs.yaml>

Swagger UI displays the endpoints documented with Springdoc annotations and can be used to inspect and, where configured, invoke the API.

## Client API endpoints

The controller's base path is `/api`.

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/clients` | Retrieve all clients |
| `POST` | `/api/client` | Create a client |
| `DELETE` | `/api/client/{idClient}` | Delete a client by ID |

### Get all clients

```http
GET http://localhost:8080/api/clients
```

Example response:

```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Smith",
    "surname": "Johnson",
    "phone": "+1234567890"
  }
]
```

The response properties depend on the actual `ClientDto` definition.

### Create a client

```http
POST http://localhost:8080/api/client
Content-Type: application/json
```

Example request body (adjust the properties to match `ClientDto`):

```json
{
  "firstName": "John",
  "lastName": "Smith",
  "surname": "Johnson",
  "phone": "+1234567890"
}
```

### Delete a client

```http
DELETE http://localhost:8080/api/client/1
```

The controller is intended to return:

- `204 No Content` when the client is deleted successfully.
- `404 Not Found` when no client exists with the supplied ID.

**Controller mapping requirement:** for the DELETE URL above to work with `@PathVariable int idClient`, the method mapping must be:

```java
@DeleteMapping("/client/{idClient}")
public ResponseEntity<ServiceResponse> deleteClient(
        @PathVariable int idClient) {
    // ...
}
```

If the mapping remains `@DeleteMapping("/client")`, the `{idClient}` path segment is not part of the route.

## Troubleshooting

### Application cannot connect to SQL Server

- Verify that the SQL Server host and TCP port are reachable from your computer.
- Confirm the database name, login, password, and permissions.
- Check that TCP/IP is enabled on SQL Server and that firewall rules allow the connection.
- Confirm the JDBC URL's named-instance and port syntax with your database administrator.
- Review the application startup logs for the underlying JDBC exception.

### Port 8080 is already in use

Stop the process using port `8080`, or temporarily change the configuration:

```properties
server.port=8081
```

### Swagger UI returns 404

- Confirm the application started successfully.
- Verify that the Springdoc dependency is present.
- Try <http://localhost:8080/v3/api-docs> first. If it also returns 404, check the application logs and Springdoc configuration.

## Maven dependency notes

The provided `pom.xml` contains the Springdoc UI dependency twice, with versions `2.10.0` and `2.1.0`. Keep **only one** declaration.

For Spring Boot `3.0.6`, use the Springdoc `2.1.0` dependency line:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.1.0</version>
</dependency>
```

Also check the Lombok dependency in the provided POM. Its group ID appears to contain a typo (`org.project.lombo1k`). The usual Lombok coordinates are:

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

Make sure Lombok annotation processing is enabled in your IDE/build if the project uses Lombok annotations.

## Notes

- The database must be available and the expected schema/tables must exist before calling endpoints that access them.
- The sample API responses in this README are illustrative. Actual fields and response bodies are determined by the DTOs and controller implementation.
- Do not store production credentials in `application.properties`, source code, or version control.

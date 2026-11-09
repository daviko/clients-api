# my-app-exam

Angular 13 frontend application that consumes the Spring Boot Client Management API running locally.

## Technology stack

- Angular 13.3
- Angular CLI 13.3
- TypeScript 4.6
- RxJS 7.5
- Angular Material 13
- Node.js and npm

## Prerequisites

1. **Node.js compatible with Angular 13.** For this project, use Node.js 16.20.x as a practical local-development choice. Angular 13 is an older release and is not compatible with every current Node.js release.
2. **npm**, installed with Node.js.
3. The Java Spring Boot API project described in the backend README.

Check your installed versions:

```bash
node --version
npm --version
```

If you use a Node version manager, select a compatible Node 16 release before installing dependencies. Node 16 is end-of-life, so use it only as needed for this legacy project and avoid exposing the development environment to untrusted networks.

## Backend API requirements

Start the Spring Boot API before testing frontend features that make HTTP requests.

The backend should be available at:

```text
http://localhost:8080
```

The API endpoints are:

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `http://localhost:8080/api/clients` | Retrieve all clients |
| `POST` | `http://localhost:8080/api/client` | Create a client |
| `DELETE` | `http://localhost:8080/api/client/{idClient}` | Delete a client by ID |

The DELETE endpoint requires the backend controller mapping to include the path variable:

```java
@DeleteMapping("/client/{idClient}")
public ResponseEntity<ServiceResponse> deleteClient(
        @PathVariable int idClient) {
    // ...
}
```

## Install dependencies

Open a terminal in the Angular project directory (the directory containing `package.json`) and run:

```bash
npm install
```

If the repository contains a `package-lock.json`, npm uses it to resolve the project's dependency versions. For repeatable installs in a clean environment or CI pipeline, use:

```bash
npm ci
```

Use `npm ci` only when a valid `package-lock.json` is committed and consistent with `package.json`.

## Configure the API base URL

Find the Angular environment configuration. In a typical Angular CLI 13 project, these files are:

```text
src/environments/environment.ts
src/environments/environment.prod.ts
```

Set the local API URL in the development environment, for example:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

Then use that value in your Angular services rather than repeating the URL in multiple components. For example:

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private readonly apiUrl = `${environment.apiUrl}/clients`;

  constructor(private http: HttpClient) {}

  getClients() {
    return this.http.get(this.apiUrl);
  }
}
```

Adjust the import path, service methods, request DTOs, and endpoint paths to match the actual Angular project structure. The POST and DELETE calls should use `/client` and `/client/{idClient}` respectively.

If your project already has an API URL configuration, update that existing configuration instead of creating a second one.

## Run the Angular application locally

From the directory containing `package.json`, start the development server:

```bash
npm start
```

This runs the configured script:

```text
ng serve
```

By default, Angular CLI serves the application at:

```text
http://localhost:4200
```

You can also start it explicitly on port 4200:

```bash
npx ng serve --port 4200
```

Open <http://localhost:4200> in your browser.

To stop the development server, press `Ctrl+C` in the terminal.

## Run both projects

For local development, run the backend and frontend in separate terminals.

### Terminal 1 — Spring Boot backend

From the Java project directory:

```bash
mvn spring-boot:run
```

The API should be available at <http://localhost:8080>, and Swagger UI at <http://localhost:8080/swagger-ui.html>.

### Terminal 2 — Angular frontend

From the Angular project directory:

```bash
npm start
```

The frontend should be available at <http://localhost:4200>.

The browser loads the Angular application from port `4200`; Angular's HTTP requests are sent to the backend at port `8080`.

## CORS configuration

Because the frontend and backend use different ports, the browser treats them as different origins. The Spring Boot API must allow requests from:

```text
http://localhost:4200
```

Your controller currently includes method-level `@CrossOrigin` annotations for that origin. Confirm that the actual controller configuration permits the frontend origin for every endpoint the Angular app calls.

If the browser reports a CORS error, inspect the browser developer tools (Console and Network tabs) and the backend CORS configuration. CORS is enforced by browsers; a successful request from tools such as curl does not prove that browser CORS configuration is correct.

## Available npm scripts

| Command | Description |
|---|---|
| `npm start` | Starts the Angular development server (`ng serve`). |
| `npm run build` | Builds the application using the configured default build configuration. |
| `npm run watch` | Rebuilds when files change, using the development configuration. |
| `npm test` | Runs unit tests with the configured test runner. |
| `npm run ng -- version` | Runs Angular CLI commands through the npm script. |

You can also check the installed Angular CLI version with:

```bash
npx ng version
```

## Build the application

Create a production build with:

```bash
npm run build
```

Angular writes the compiled output to the configured output directory, usually under:

```text
dist/
```

The build output is static frontend content. It is separate from the Spring Boot API and must be hosted by a web server or deployment platform.

For a development watch build, run:

```bash
npm run watch
```

## Run unit tests

Execute the configured unit tests:

```bash
npm test
```

The project uses Jasmine and Karma dependencies. Depending on the local browser setup, you may need a compatible Chrome or Chromium installation for the test runner.

## Troubleshooting

### `ng` is not recognized

Install project dependencies first:

```bash
npm install
```

Then run the project script:

```bash
npm start
```

Using `npm start` or `npx ng` avoids depending on a globally installed Angular CLI.

### Node.js or TypeScript compatibility errors

Angular 13 and TypeScript 4.6 are older versions. Use a Node.js version compatible with Angular 13 (Node 16.20.x is a practical choice for this project) and install dependencies from the project's lockfile where available. Do not upgrade Angular, TypeScript, or Node independently without checking their compatibility.

### Port 4200 is already in use

Start Angular on another port:

```bash
npx ng serve --port 4201
```

If you change the frontend port, update the backend CORS allowlist to permit the new origin, such as `http://localhost:4201`.

### API requests fail or return a network error

- Confirm the Spring Boot application is running on port `8080`.
- Open <http://localhost:8080/swagger-ui.html> to confirm the API is available.
- Check that the Angular environment points to `http://localhost:8080/api`.
- Check the browser Network and Console tabs for the failed request and any CORS error.
- Confirm that the backend route and HTTP method match the Angular service call.
- Confirm that the backend can connect to SQL Server.

### API returns 404 for DELETE

Make sure the backend mapping is `@DeleteMapping("/client/{idClient}")` when the method parameter is annotated with `@PathVariable`.

## Notes

- This README assumes the Angular project is run locally at `http://localhost:4200` and the Spring Boot API at `http://localhost:8080`.
- The example environment and service snippets are illustrative; adapt them to the existing source files and DTOs in your project.
- Keep API URLs environment-specific so local, test, and production deployments can use different backend addresses.

# OpenLibrary
OpenLibrary is a application for libraries administrators, integrating distributed systems.

## Architecture
### Schema
### Explanations

## API Endpoints
#### Manage book records
- `-X GET /api/books`: Retrieve a list of all books
- `-X POST /api/books`: Add a new book
- `-X GET /api/books/{id}`: Retrieve details of a specific book
- `-X PUT /api/books/{id}`: Update details of a specific book
- `-X DELETE /api/books/{id}`: Delete a specific book
#### Manage book rentals
- `-X GET /api/rentings`: Retrieve a list of all rentings
- `-X POST /api/rentings`: Rent a book (add new Renting record. Set the book as not available)
- `-X GET /api/rentings/{id}`: Retrieve details of a specific renting
- `-X PUT /api/rentings/{id}`: Update details of a specific renting
- `-X DELETE /api/rentings/{id}`: Return a rented book (delete Renting record. Set the book as available)
#### Authentication with Keycloak
- `-X POST /auth/register`: Register a new user
- `-X POST /auth/login`: Login and obtain an access token

## Run the project
### Start the project
To start the project, execute from root the following command to start Docker service:
```bash
docker compose up --build
```
Each service runs in a Docker container.

### Get authenticated
To run endpoints below and manage books/rentals, a access token is needed. It can be obtained with authentication endpoints.

Start by registering a new user in Keycloak with the following command:
```bash
curl -X POST http://localhost:8080/auth/register \
-H "Content-Type: application/json" \
-d "{
  \"username\":\"johndoe\",
  \"password\":\"password\",
  \"email\":\"john.doe@email.com\",
  \"firstName\":\"John\",
  \"lastName\":\"Doe\"
}"
```
Then, login with the following command by using credentials precised above:
```bash
curl -X POST http://localhost:8080/auth/login \
-H "Content-Type: application/json" \
-d "{
  \"username\":\"johndoe\",
  \"password\":\"password\"
}"
```
Login will return a response containing the access token with the following format:
```json
{
    "access_token":"API_TOKEN",
    "refresh_token":"REFRESH_TOKEN",
    "expires_in":300,
    "refresh_expires_in":1800,
    "token_type":"Bearer"
}
```

### Curl commands to test the API
- Get all books:
  ```bash
  curl -X GET http://localhost:8080/api/books \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_API_TOKEN'
  ```
- Get all rentings:
  ```bash
  curl -X GET http://localhost:8080/api/rentings \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_API_TOKEN'
  ```
- Add a new book:
  ```bash
  curl -X POST http://localhost:8080/api/books \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_API_TOKEN' \
  -d '{"title":"Book Title","author":"Author Name","isbn":"1234567890"}'
  ```
- Rent a book:
  ```bash
  curl -X POST http://localhost:8080/api/rentings \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_API_TOKEN' \
  -d '{"bookId":1,"renterFirstName":"John", "renterLastName":"Doe", "renterEmail":"john.doe@email.com"}'
  ```
- Return a book:
  ```bash
  curl -X DELETE http://localhost:8080/api/rentings/{id} \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer YOUR_API_TOKEN'
  ```

:bulb: Don't forget to replace `YOUR_API_TOKEN` with a valid token obtained from authentication step.

## Demonstration of services

### Keycloak Authentication
We can see on screenshots below that the user John Doe, with the "john.doe@hft-stuttgart.de" email address, is registered in Keycloak by making an API call to `/auth/register` endpoints.

#### Curl command:
<img width="655" height="147" alt="Screenshot 2025-12-15 at 11 30 45" src="https://github.com/user-attachments/assets/3b2b3f88-150a-462f-a450-9a41b8a343e5" />

#### Keycloak pannel:
<img width="899" height="538" alt="image" src="https://github.com/user-attachments/assets/24c8c6e2-7c4c-4d90-b74e-4a44201731be" />


